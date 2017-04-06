package com.seeyon.v3x.dee.common.db2cfg;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.db.codelib.dao.CodeLibDao;
import com.seeyon.v3x.dee.common.db.codelib.model.CodeLibBean;
import com.seeyon.v3x.dee.common.db.download.dao.DownloadDAO;
import com.seeyon.v3x.dee.common.db.download.model.DownloadBean;
import com.seeyon.v3x.dee.common.db.flow.dao.FlowDAO;
import com.seeyon.v3x.dee.common.db.flow.dao.FlowSubDAO;
import com.seeyon.v3x.dee.common.db.flow.model.FlowBean;
import com.seeyon.v3x.dee.common.db.flow.model.FlowSubBean;
import com.seeyon.v3x.dee.common.db.metaflow.dao.MetaFlowDAO;
import com.seeyon.v3x.dee.common.db.metaflow.model.MetaFlowBean;
import com.seeyon.v3x.dee.common.db.resource.dao.DeeResourceDAO;
import com.seeyon.v3x.dee.common.db.resource.dao.DeeResourceTemplateDAO;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceTemplateBean;
import com.seeyon.v3x.dee.common.db.schedule.dao.ScheduleDAO;
import com.seeyon.v3x.dee.common.db.schedule.model.ScheduleBean;
import com.seeyon.v3x.dee.common.db2cfg.constant.GlobalConstant;
import com.seeyon.v3x.dee.context.AdapterKeyName;
import com.seeyon.v3x.dee.script.GroovyCache;
import com.seeyon.v3x.dee.util.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.script.ScriptException;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GenerationCfgUtil {
	private static Log log = LogFactory.getLog(GenerationCfgUtil.class);

	private MetaFlowDAO mfDao = new MetaFlowDAO();
	private FlowSubDAO fsDao = new FlowSubDAO();
	private DeeResourceDAO drDao = new DeeResourceDAO();
	private ScheduleDAO sDao = new ScheduleDAO();
	private FlowDAO fDao = new FlowDAO();
	private DownloadDAO dDao = new DownloadDAO();
	private DeeResourceTemplateDAO drtDao = new DeeResourceTemplateDAO();
	private CodeLibDao codeLibDao = new CodeLibDao();

	Map<String, DeeResourceBean> refMap = new HashMap<String, DeeResourceBean>();
	
	private static GenerationCfgUtil util;
	
	public static GenerationCfgUtil getInstance(){
		if(util==null)
			util = new GenerationCfgUtil();
		return util;
	}
	
	/**
	 * 获取元数据flow
	 * 
	 * @param 
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getMetaCfg() throws TransformException {
		StringBuffer metaXML = new StringBuffer();
		try {
			List<MetaFlowBean> metaflowList = mfDao.findAll();
			for (MetaFlowBean metaFlowBean : metaflowList) {
				if(metaFlowBean == null || metaFlowBean.getMetaflow_code() == null)
					continue;
				metaXML.append(metaFlowBean.getMetaflow_code().trim());
			}
			return metaXML.toString();
		} catch (Exception e) {
			log.error("获取元数据Flow出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}
	}
	/**
	 * 根据flowBean获取配置内容
	 *
	 * @param flowBean
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	private String getFlowCfgByBean(FlowBean flowBean) throws TransformException {
		AdapterKeyName adapterKeyName = AdapterKeyName.getInstance();
		if(flowBean != null){
			adapterKeyName.getFlowMap().put(flowBean.getFLOW_ID(), flowBean.getDIS_NAME());
		}
		StringBuffer flowXML = new StringBuffer();
		if(flowBean == null)
			return "";
		try {
			List<FlowSubBean> flowSubs = fsDao.getByFlowID(flowBean.getFLOW_ID());
			List<DeeResourceBean> resourceList = new ArrayList<DeeResourceBean>();
			for (FlowSubBean flowSubBean : flowSubs) {
				DeeResourceBean rBean = drDao.findById(flowSubBean.getResource_id());
				resourceList.add(rBean);
				String refid = rBean.getRef_id();
				if (refid != null && !"".equals(refid)) {
					refMap.put(refid, drDao.findById(refid));
				}
			}
			// 如果是空flow则返回空串
			if (resourceList.size() == 0)
				return "";

			flowXML.append("<flow name=\"" + flowBean.getFLOW_NAME() + "\">");
			//CDE-357 增加监听事件listener的实现
			if(StringUtils.isNotBlank(flowBean.getEXT1()) && !"-1".equals(flowBean.getEXT1())) {//增加listener的导出
				String[] listeners = flowBean.getEXT1().split(",");
				DeeResourceTemplateBean drtBean = null;
				for(String listener_id : listeners) {
					drtBean = drtDao.getOneById(listener_id);
					flowXML.append(drtBean.getTemplate().toString());
				}
			}
			for (DeeResourceBean deeResourceBean : resourceList) {
				if(deeResourceBean == null || deeResourceBean.getResource_code() == null)
					continue;
				flowXML.append(deeResourceBean.getResource_code().trim());
				adapterKeyName.getAdapterMap().put(deeResourceBean.getResource_id(), deeResourceBean.getDis_name());
			}
			flowXML.append("</flow>\r\n");
			return flowXML.toString();
		} catch (Exception e) {
			log.error("获取flow配置信息出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}
	}
	/**
	 * 根据多条flowid导出flow配置
	 *
	 * @param flowIds 以逗号分割
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getMultipleFlowCfg(String flowIds) throws TransformException {
		StringBuffer flowXML = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?><engine name=\"root-dee\">");
		if(flowIds == null || "".equalsIgnoreCase(flowIds))
			return "";
		try {
			clearRefResourceMap(); //清空引用源列表
			String[] ids = flowIds.split(",");
			for(String flowId:ids){
				FlowBean flowBean = fDao.get(flowId);
				flowXML.append(getFlowCfgByBean(flowBean));
			}
			// 添加引用源数据
			flowXML.append(getRefResourceCfg());
			// 添加定时器配置
			flowXML.append(getScheduleCfg());
			// 添加jdbc字典
			flowXML.append(getJDBCDictCfg());
			flowXML.append("</engine>");
			return flowXML.toString();
		} catch (Exception e) {
			log.error("获取flow配置信息出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}

	}
	/**
	 * 获取引用源的配置内容
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getRefResourceCfg() throws TransformException {
		StringBuilder refResXML = new StringBuilder();

		List<DeeResourceBean> resourceBeans = drDao.findAllDatasource();
		for (DeeResourceBean bean : resourceBeans) {
			if (bean == null || bean.getResource_code() == null) {
				continue;
			}
			refMap.remove(bean.getResource_id());
			refResXML.append(bean.getResource_code().trim()).append("\r\n");
		}

		try {
			// 获取ref引用的资源
			for (Entry<String, DeeResourceBean> refEntry : refMap.entrySet()) {
				if (refEntry.getValue() != null && refEntry.getValue().getResource_code() != null) {
					refResXML.append(refEntry.getValue().getResource_code().trim()).append("\r\n");
				}
			}
		} catch (Exception e) {
			log.error("获取ref源配置信息出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}

		return refResXML.toString();

		/*StringBuffer refResXML = new StringBuffer();
		if(refMap == null || refMap.size() < 1)
			return "";
		try {
			// 获取ref引用的资源
			for (Entry<String, DeeResourceBean> refEntry : refMap.entrySet()) {
				if (refEntry.getValue() == null || refEntry.getValue().getResource_code() == null)
					continue;
				refResXML.append(refEntry.getValue().getResource_code().trim()+"\r\n");
			}
			return refResXML.toString();
		} catch (Exception e) {
			log.error("获取ref源配置信息出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}*/
	}

	/**
	 * 获取定时器配置内容
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getScheduleCfg() throws TransformException {
		StringBuffer scheduleXML = new StringBuffer();
		try {
			// 获取ref引用的资源
			List<ScheduleBean> scdList = sDao.queryAll();
			for (ScheduleBean scheduleBean : scdList) {
				if(scheduleBean == null || !scheduleBean.getEnable() || scheduleBean.getSchedule_code() == null)
					continue;
				scheduleXML.append(scheduleBean.getSchedule_code().trim()+"\r\n");
			}
			return scheduleXML.toString();
		} catch (Exception e) {
			log.error("获取定时器配置信息出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}
	}

	/**
	 * 获取全部flow配置内容
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getAllFlowsCfg() throws TransformException {
		StringBuffer flowAllXML = new StringBuffer(
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?><engine name=\"root-dee\">");
		try {
			clearRefResourceMap(); //清空引用源列表
			// 添加获取元数据flow
			flowAllXML.append(getMetaCfg());
			// 添加全部flow配置
			List<FlowBean> flowList = fDao.findAll();
			for(FlowBean fBean:flowList){
				flowAllXML.append(getFlowCfgByBean(fBean));
			}
			// 添加引用源数据
			flowAllXML.append(getRefResourceCfg());
			// 添加定时器配置
			flowAllXML.append(getScheduleCfg());

			flowAllXML.append(getJDBCDictCfg());
			flowAllXML.append("</engine>");
			return flowAllXML.toString();
		} catch (Exception e) {
			log.error("获取全部flow配置信息出错：" + e.getMessage(), e);
			throw new TransformException(e);
		}
	}

	/**
	 * 生成静态字典
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getStaticDictCfg() throws TransformException {
		// 生成静态字典
		StringBuffer dXML = new StringBuffer();
		List<DeeResourceBean> resourceList = drDao.findAll();
		for(DeeResourceBean deeResourceBean : resourceList){
			if(deeResourceBean == null || deeResourceBean.getResource_code() == null)
				continue;
			if(GlobalConstant.RESOURCE_TEMPLATE_ID_STATICDICTIONARY
					.equalsIgnoreCase(deeResourceBean.getResource_template_id())){
				dXML.append(deeResourceBean.getResource_code().trim()+"\r\n");
			}
		}
		return dXML.toString();
	}
	/**
	 * 生成jdbc字典
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public String getJDBCDictCfg() throws TransformException {
		// 生成静态字典
		StringBuffer dXML = new StringBuffer();
		List<DeeResourceBean> resourceList = drDao.findAll();
		for(DeeResourceBean deeResourceBean : resourceList){
			if(deeResourceBean == null || deeResourceBean.getResource_code() == null)
				continue;
			if(GlobalConstant.RESOURCE_TEMPLATE_ID_JDBCDICTIONARY
					.equalsIgnoreCase(deeResourceBean.getResource_template_id())){
				dXML.append(deeResourceBean.getResource_code().trim()+"\r\n");
			}
		}
		return dXML.toString();
	}

	/**
	 * 生成外部配置文件
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void generationExtFile(String hfPath) throws TransformException {
		try {
			List<DeeResourceBean> resourceList = drDao.findAll();
			File dictFile = new File(hfPath, "dictionary.properties");
			StringBuffer diceStringBuffer = new StringBuffer();
			try {
				diceStringBuffer.append(getStaticDictCfg());

				FileOutputStream writer = new FileOutputStream(dictFile, false);
				writer.write(diceStringBuffer.toString().getBytes(GlobalConstant.CHARSET));
				writer.close();
			} catch (Exception e) {
				log.error("生成静态字典文件出错：" + e.getMessage(), e);
				throw new TransformException("生成静态字典文件出错：" + e.getMessage(), e);
			}
			for (DeeResourceBean deeResourceBean : resourceList) {
				if(deeResourceBean == null || deeResourceBean.getResource_code() == null)
					continue;
				if(!GlobalConstant.RESOURCE_TEMPLATE_ID_STATICDICTIONARY
						.equalsIgnoreCase(deeResourceBean.getResource_template_id()) &&
						!GlobalConstant.RESOURCE_TEMPLATE_ID_JDBCDICTIONARY
						.equalsIgnoreCase(deeResourceBean.getResource_template_id())){
					// 取得需要生成的外部文件
					DownloadBean dbean = dDao.getById(deeResourceBean.getResource_id());
					if (dbean != null){
						try {
							// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
							File tmpFile = new File(hfPath + "/"
									+ dbean.getFILENEME());
							if (!tmpFile.exists())
								tmpFile.createNewFile();
							FileOutputStream dwriter = new FileOutputStream(tmpFile,
									false);
							dwriter.write(dbean.getCONTENT().trim()
									.getBytes(GlobalConstant.CHARSET));
							dwriter.close();
						} catch (IOException e) {
							log.error("输出下载文件出错：" + e.getMessage(), e);
							throw new TransformException("输出下载文件出错：" + e.getMessage(),e);
						}
					}
				}
			}

		} catch (TransformException e) {
			log.error("输出外部文件出错：" + e.getMessage(), e);
			throw new TransformException("输出外部文件出错：" + e.getMessage(),e);
		}
	}


	/**
	 * 生成配置文件
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void generationMainFile(String homePath) throws TransformException {
		if(homePath == null || "".equalsIgnoreCase(homePath)){
			homePath = getDEEHome();
		}
		File pathFile = new File(homePath);
		if (pathFile.exists() && pathFile.isDirectory()) {
			try {
				// 部署静态字典文件
				generationExtFile(homePath + "/conf");
				// 部署代码库文件
				generationCodeLib();
				// 获取全部配置
				String xmlStr = getAllFlowsCfg();
				String filename = URLEncoder.encode("dee.xml", GlobalConstant.CHARSET);
				File confFile = new File(homePath + "/conf", filename);
				FileOutputStream fos = new FileOutputStream(confFile);
				byte[] utf = xmlStr.getBytes(GlobalConstant.CHARSET);
				fos.write(utf);
				fos.close();
			} catch (Exception e) {
				log.error("发布出错：" + e.getMessage(), e);
			}
		}
	}

	/**
	 * 清理引用列表
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */
	public void clearRefResourceMap(){
		refMap = new HashMap<String, DeeResourceBean>(); //清空引用源列表
	}

	/**
	 * 获取DeeHome路径
	 *
	 * @param
	 * @return
	 * @throws com.seeyon.v3x.dee.TransformException
	 */	
	public static String getDEEHome() {
		// Property优先
		String v = System.getProperty(GlobalConstant.ENV_DEE_HOME);
		if (v == null)
			v = System.getenv(GlobalConstant.ENV_DEE_HOME);
		return v;
	}

	/**
	 * 生成代码库文件
	 *
	 * @throws TransformException
	 */
	public void generationCodeLib() throws TransformException {
		Map<String, CodeLibBean> codeLibBeanMap = codeLibDao.findAll();

		String codeLibPath = getDEEHome() + "/codelib/";
		File dir = new File(codeLibPath);
		if (dir.exists() && dir.isDirectory()) {
			String [] files = dir.list();
			for (String file : files) {
				new File(dir, file).delete();
			}
		}

		for (Entry<String, CodeLibBean> entry : codeLibBeanMap.entrySet()) {
			CodeLibBean bean = entry.getValue();
			StringBuilder sb = new StringBuilder();
			sb.append("package ").append(bean.getPkgName()).append(";\n\n");
			sb.append(bean.getCode());
			write2CodeLib(bean.getPkgName(), bean.getClassName(), sb.toString());
		}
		//清除缓存,重新加载代码库
		refreshScript();
	}

	/**
	 * 写入代码库groovy文件
	 *
	 * @param code
	 * @throws TransformException
	 */
	private void write2CodeLib(String packageName, String className, String code) throws TransformException {
		String path = getDEEHome() + "/codelib/" + packageName + "." + className + ".groovy";
		BufferedWriter bw = null;
		try {
			boolean createSuccessFlag = true;

			File file = new File(path);
			if (!file.exists()) {
				createSuccessFlag = file.createNewFile();
			}

			if (!createSuccessFlag) {
				throw new TransformException(path + "创建失败！");
			}

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(code);
		} catch (Exception e) {
			throw new TransformException(e);
		} finally {
			FileUtil.close(bw);
		}
	}

	/**
	 * 载入用户代码库
	 *
	 * @throws TransformException
	 */
	private void importCodeLib(){
		String codePath = getDEEHome()+"/codelib";
		File dir = new File(codePath);
		if(dir.exists() && dir.isDirectory()){
			File[] imps = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					return pathname.getName().endsWith(".groovy");
				}
			});
			if(imps!=null && imps.length>0){
				try {
					GroovyCache.getInstance().loadCode(imps);
				} catch (ScriptException e) {
					log.error("groory 用户代码库载入失败",e);
				}
			}
		}
	}
	//清除缓存重新导入
	public void refreshScript(){
		//清除缓存
		GroovyCache.getInstance().clearGCache();
		//重新导入
		importCodeLib();
	}
}
