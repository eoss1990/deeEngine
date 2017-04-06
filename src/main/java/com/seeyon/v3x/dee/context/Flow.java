package com.seeyon.v3x.dee.context;

import com.seeyon.v3x.dee.*;
import com.seeyon.v3x.dee.adapter.Adapter;
import com.seeyon.v3x.dee.adapter.InitializingAdapter;
import com.seeyon.v3x.dee.config.EngineContext;
import com.seeyon.v3x.dee.debug.AdapterBeanLog;
import com.seeyon.v3x.dee.debug.AdapterDebug;
import com.seeyon.v3x.dee.debug.ScriptDebug;
import com.seeyon.v3x.dee.event.ErrorEvent;
import com.seeyon.v3x.dee.event.EventDispatcher;
import com.seeyon.v3x.dee.event.FlowEndEvent;
import com.seeyon.v3x.dee.event.FlowStartEvent;
import com.seeyon.v3x.dee.event.RedoErrorEvent;
import com.seeyon.v3x.dee.util.DocumentUtil;
import com.seeyon.v3x.dee.util.FileUtil;

//import com.seeyon.v3x.dee.client.service.DEEConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * DEE任务，DEE的执行单元，包含多个顺序执行的适配器
 */
public class Flow {
    private static Log log = LogFactory.getLog(Flow.class);

    public static final String ATTRIBUTE_KEY_TASKNAME = "ERRORTASKNAME";

    public static final String ATTRIBUTE_KEY_REDOID = "ATTRIBUTE_KEY_TASKNAME";

    private static final String PARAMETERS_KEY_DOCUMENT = "doc";

    private static final String ADAPTER_SUCCESS = "T";

    public static final String AdAPTER_FALSE = "F";

    /**
     * 任务ID
     */
    private String name;

    /**
     * 适配器列表
     */
    LinkedHashMap<String, Adapter> adapterMap = new LinkedHashMap<String, Adapter>();

    /**
     * 事件分发器
     */
    private EventDispatcher dispatcher = new EventDispatcher();

    public Flow(String name) {
        this.name = name;
    }

    public Document execute(Document input, TransformContext context) throws TransformException {
        return this.execute(input, context, new Parameters(), null);
    }

    public Document execute(Document input, TransformContext context, Parameters params) throws TransformException {
        return this.execute(input, context, params, null);
    }

    public Document execute(Document input, TransformContext context, Parameters params, String startWith)
            throws TransformException {
        logParams(params);

        Document document = newIfAbsent(input);
        document.setContext(context);

        if (startWith == null) {
            return executeInNormal(document, context, params);
        } else {
            return executeInRedo(document, context, params, startWith);
        }
    }

    public Document redo(Document input, String startWith, Parameters params,
                         EngineContext context,String redoID) throws TransformException {
        params.add("flow", this);
        TransformContext tContext = new TransformContextImpl(params, context);
        tContext.setAttribute(ATTRIBUTE_KEY_REDOID, redoID);
        return execute(input, tContext, params, startWith);
    }

    private Document executeInNormal(Document document, TransformContext context, Parameters params)
            throws TransformException {
        //增加AdapterDebug单例来储存每个适配器是否调试成功；
        if(AdapterDebug.getInstance().getAdapterMap()!=null)
            AdapterDebug.getInstance().getAdapterMap().clear();
        dispatcher.fireEvent(new FlowStartEvent(this).setContext(context));
        boolean nextIsReader = false;
        String adapterName = null;
        Map<String,String> adapterDes = new HashMap<String,String>();
        List<AdapterBeanLog> adapterLog = new ArrayList<AdapterBeanLog>();
        int i = 0;
        try {
            Set<Map.Entry<String, Adapter>> entrySet = adapterMap.entrySet();
            for (Map.Entry<String, Adapter> entry : entrySet) {
                adapterName = entry.getKey();
                ScriptDebug.getInstance().setAdapterName(adapterName);
                Adapter adapter = entry.getValue();
                context.getParameters().add(PARAMETERS_KEY_DOCUMENT, document.toString());
                Document orgDoc = document;
                /**
                 * 预先处理Parameters，进行evalString操作
                 */
                parametersHandler(adapter,document.getContext().getParameters());
                Document retDocument = adapter.execute(document);
                if(nextIsReader && adapter.toString().contains("JDBCReader")){
                    retDocument = DocumentUtil.merge(orgDoc, retDocument);
                }
                retDocument.setContext(document.getContext());
                document = retDocument;
                nextIsReader = true;
                adapterDes.put(adapterName,ADAPTER_SUCCESS);
                if(i == 0){
                    adapterLog.add(new AdapterBeanLog("startData",-1,document.toString(),
                    		document.getContext().getParameters().toString()));
                }
                adapterLog.add(new AdapterBeanLog(adapterName,1,"",""));
                if(isInterrupt(retDocument, adapterLog, context, adapterDes)){
                	return document;
                }
                i++;
            }
        } catch (Exception e) {
            adapterDes.put(adapterName,AdAPTER_FALSE);
            if(i == 0){
                adapterLog.add(new AdapterBeanLog("startData",-1,document.toString(),
                		document.getContext().getParameters().toString()));
            }
            adapterLog.add(new AdapterBeanLog(adapterName,0,e.getMessage(),""));
            AdapterDebug.getInstance().setAdapterMap(adapterDes);
            context.setAttribute(ATTRIBUTE_KEY_TASKNAME, adapterName);
            dispatcher.fireEvent(new ErrorEvent(this).setDocument(document).setMessage(e.getLocalizedMessage()));
            adapterLog.add(new AdapterBeanLog("endData",-1,document.toString(),
            		document.getContext().getParameters().toString()));
            flowAdapterLog(adapterLog, context.getId(), context.getParameters().get("flowId").getValue().toString());
            if (e instanceof TransformException) {
                throw (TransformException) e;
            } else {
                throw new TransformException(e);
            }
        }
        adapterLog.add(new AdapterBeanLog("endData",-1,document.toString(),
        		document.getContext().getParameters().toString()));
        flowAdapterLog(adapterLog, context.getId(), context.getParameters().get("flowId").getValue().toString());
        AdapterDebug.getInstance().setAdapterMap(adapterDes);
        dispatcher.fireEvent(new FlowEndEvent(this).setContext(context));
        return document;
    }

    private Document executeInRedo(Document document, TransformContext context, Parameters params, String startWith)
            throws TransformException {
        String adapterName = null;
        List<AdapterBeanLog> adapterLog = new ArrayList<AdapterBeanLog>();
        int i = 0;
        try {
            boolean isFind = false;
            Set<Map.Entry<String, Adapter>> entrySet = adapterMap.entrySet();
            for (Map.Entry<String, Adapter> entry : entrySet) {
                adapterName = entry.getKey();
                if (startWith != null && adapterName.equals(startWith)) {
                    isFind = true;
                }
                if (!isFind) {
                    continue;
                }
                Adapter adapter = entry.getValue();
                context.getParameters().add(PARAMETERS_KEY_DOCUMENT, document.toString());
                /**
                 * 预先处理Parameters，进行evalString操作
                 */
                parametersHandler(adapter,document.getContext().getParameters());
                Document retDocument = adapter.execute(document);
                retDocument.setContext(document.getContext());
                document = retDocument;
                if(i == 0){
                    adapterLog.add(new AdapterBeanLog("startData",-1,document.toString(),
                    		document.getContext().getParameters().toString()));
                }
                adapterLog.add(new AdapterBeanLog(adapterName,1,"",""));
                if(isInterrupt(retDocument, adapterLog, context, null)){
                	return document;
                }
                i++;
            }
        } catch (Exception e) {
            context.setAttribute(ATTRIBUTE_KEY_TASKNAME, adapterName);
            dispatcher.fireEvent(new RedoErrorEvent(this).setContext(context));
            if(i == 0){
                adapterLog.add(new AdapterBeanLog("startData",-1,document.toString(),
                		document.getContext().getParameters().toString()));
            }
            adapterLog.add(new AdapterBeanLog(adapterName,0,e.getMessage(),""));
            adapterLog.add(new AdapterBeanLog("endData",-1,document.toString(),
            		document.getContext().getParameters().toString()));
            flowAdapterLog(adapterLog, params.get("oldSyscId").getValue().toString(), context.getParameters().get("flowId").getValue().toString());
            if (e instanceof TransformException) {
                throw (TransformException) e;
            } else {
                throw new TransformException(e);
            }
        }
        adapterLog.add(new AdapterBeanLog("endData",-1,document.toString(),
        		document.getContext().getParameters().toString()));
        flowAdapterLog(adapterLog, params.get("oldSyscId").getValue().toString(), context.getParameters().get("flowId").getValue().toString());
        dispatcher.fireEvent(new FlowEndEvent(this).setContext(context));
        return document;
    }

    private void logParams(Parameters params) {
        log.debug("Flow[" + name + "]开始执行");
        if (params == null) {
            log.debug("Flow[" + name + "]---无参数信息");
        } else {
            for (Parameter p : params) {
                log.debug("参数:" + p.getName() + "==" + p.getValue());
            }
        }
    }
    
    /**
     * 记录任务详细日志日志
     */
    private void flowAdapterLog(final List<AdapterBeanLog> adapterLog, final String sysId, final String flowId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
            	String path = "";
                AdapterKeyName adapterKeyName = AdapterKeyName.getInstance();
                boolean isA8 = FileUtil.isA8Home();
                if(isA8){
                	path = adapterKeyName.getA8Home() + "/base/dee/flowLogs/" + adapterKeyName.getFlowMap().get(flowId) + "_" + flowId + "/";
                }else{
                	path = adapterKeyName.getDeeHome() + "/flowLogs/" + adapterKeyName.getFlowMap().get(flowId) + "_" + flowId + "/";
                }
            	Properties prop = new Properties();
                try {
                	File file = new File(path);
                    if (!file.exists()){
                    	file.mkdirs();
                    }
                    path = path + sysId + ".properties";
                    file = new File(path);
                    if (!file.exists()){
                    	file.createNewFile();
                    }
                    InputStream fis = new FileInputStream(file);
                    prop.load(fis);
                    fis.close();
                    OutputStream fos = new FileOutputStream(path);
                    int i = 1;
                	for(AdapterBeanLog log : adapterLog){
                		if("startData".equals(log.getName())){
                			prop.setProperty("startData.data", log.getData());
                			prop.setProperty("startData.parm", log.getParms());
                		} else if("endData".equals(log.getName())){
                			prop.setProperty("endData.data", log.getData());
                			prop.setProperty("endData.parm", log.getParms());
                		} else {
                			prop.setProperty("adapter_" + i + "," + adapterKeyName.getAdapterMap().get(log.getName()) + ".state", Integer.toString(log.getState()));
                			prop.setProperty("adapter_" + i + "," + adapterKeyName.getAdapterMap().get(log.getName()) + ".data", log.getData());
                			i++;
                		}
                	}
            		prop.store(fos, "FlowLog");
                    fos.close();
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
            }
        });
        thread.start();
    }
    
    private boolean isInterrupt(Document document, List<AdapterBeanLog> adapterLog, 
    		TransformContext context, Map<String,String> adapterDes){
    	String isInterruptTask = (String) document.getContext().getParameters().getValue("isInterruptTask");
        if(isInterruptTask != null){
        	if("true_1".equals(isInterruptTask)){
				adapterLog.add(new AdapterBeanLog("endData", -1, document.toString(),
						document.getContext().getParameters().toString()));
				flowAdapterLog(adapterLog, context.getId(),
						context.getParameters().get("flowId").getValue().toString());
				if(adapterDes != null){
					AdapterDebug.getInstance().setAdapterMap(adapterDes);
				}
				dispatcher.fireEvent(new FlowEndEvent(this).setContext(context));
        		return true;
        	}
        }
        return false;
    }

    /**
     * document为空，则新建一个；document不为空，则返回原参数
     *
     * @param document document
     * @return 处理后的对象
     */
    private Document newIfAbsent(Document document) {
        if (document != null) {
            return document;
        }
        return TransformFactory.getInstance().newDocument("root");
    }

    private final void parametersHandler(Adapter adapter,Parameters parameters) throws Exception {
        if (adapter instanceof InitializingAdapter)
            ((InitializingAdapter) adapter).evalParaBeforeExe(parameters);
    }

    public void addListener(Class<?> clazz) {
        dispatcher.parse(clazz);
    }

    public String getName() {
        return name;
    }

    public void putAdapter(String name, Adapter adapter) {
        adapterMap.put(name, adapter);
    }

    public Map<String, Adapter> getAdapterMap() {
        return adapterMap;
    }
}
