package com.seeyon.v3x.dee.client.service;

import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.bean.JDBCReaderBean;
import com.seeyon.v3x.dee.common.base.page.Page;
import com.seeyon.v3x.dee.common.db.code.dao.FlowTypeDAO;
import com.seeyon.v3x.dee.common.db.code.model.FlowTypeBean;
import com.seeyon.v3x.dee.common.db.flow.dao.FlowDAO;
import com.seeyon.v3x.dee.common.db.flow.dao.FlowModuleDAO;
import com.seeyon.v3x.dee.common.db.flow.model.FlowBean;
import com.seeyon.v3x.dee.common.db.flow.model.FlowModuleBean;
import com.seeyon.v3x.dee.common.db.parameter.dao.FlowParameterDAO;
import com.seeyon.v3x.dee.common.db.parameter.model.ParameterBean;
import com.seeyon.v3x.dee.common.db.redo.dao.DEERedoDAO;
import com.seeyon.v3x.dee.common.db.redo.dao.DEESyncDAO;
import com.seeyon.v3x.dee.common.db.redo.model.RedoBean;
import com.seeyon.v3x.dee.common.db.redo.model.SyncBean;
import com.seeyon.v3x.dee.common.db.redo.model.SyncBeanLog;
import com.seeyon.v3x.dee.common.db.resource.dao.DeeResourceDAO;
import com.seeyon.v3x.dee.common.db.resource.model.DeeResourceBean;
import com.seeyon.v3x.dee.common.db.resource.util.DeeResourceEnum;
import com.seeyon.v3x.dee.common.db.schedule.dao.ScheduleDAO;
import com.seeyon.v3x.dee.common.db.schedule.model.ScheduleBean;
import com.seeyon.v3x.dee.config.EngineConfig;
import com.seeyon.v3x.dee.context.EngineController;
import com.seeyon.v3x.dee.context.Flow;
import com.seeyon.v3x.dee.datasource.XMLDataSource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DEEConfigService {
	FlowDAO fdao = new FlowDAO();
	FlowTypeDAO ftdao = new FlowTypeDAO();
	FlowParameterDAO paradao = new FlowParameterDAO();
	FlowModuleDAO fmdao = new FlowModuleDAO();
	DEERedoDAO rddao = new DEERedoDAO();
	DEESyncDAO syDao = new DEESyncDAO();
	DeeResourceDAO drDao = new DeeResourceDAO();
	ScheduleDAO sdDao = new ScheduleDAO();

	private static DEEConfigService fls = new DEEConfigService();
	private static Log log = LogFactory.getLog(DEEConfigService.class);

	public static final String MODULENAME_FORM = "A8表单控件";
	public static final String MODULENAME_DATA = "A8数据触发";
	public static final String MODULENAME_NC = "NC-OA";
	public static final String MODULENAME_PORTAL = "Portal栏目";
	public static final String MODULENAME_OTHER = "其他";

	public static final String MAP_KEY_TOTALCOUNT = "MAP_KEY_TOTALCOUNT";
	public static final String MAP_KEY_RESULT = "MAP_KEY_RESULT";

	public static final String PARAM_PAGESIZE = "Paging_pageSize";
	public static final String PARAM_PAGENUMBER = "Paging_pageNumber";

	private DEEConfigService() {
	}

	public static DEEConfigService getInstance() {
		if (fls == null) {
			fls = new DEEConfigService();
		}
		return fls;
	}

	/**
	 * 根据条件获取任务列表 （分页）
	 * 
	 * @param flowType
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getFlowList(String flowType, String moduleName,
			String flowName, int pageNum, int pageSize) {
		Page<FlowBean> page = new Page<FlowBean>();
		page.setPageNo(pageNum);
		page.setPageSize(pageSize);
		FlowBean fb = new FlowBean();
		fb.setMODULE_IDS(moduleName);
		fb.setFLOW_TYPE_ID(flowType);
		fb.setDIS_NAME(flowName);
		try {
			page = fdao.query(page, fb);
			long total = page.getTotalCount();
			List<FlowBean> reList = (List<FlowBean>) page.getResult();
            // 进行转码
            for (FlowBean flowBean : reList) {
                if (flowBean != null && flowBean.getDIS_NAME() != null) {
                    flowBean.setDIS_NAME(StringEscapeUtils.unescapeHtml(flowBean.getDIS_NAME()));
                }
            }
			HashMap<String, Object> reMap = new HashMap<String, Object>();
			reMap.put(MAP_KEY_TOTALCOUNT, total);
			reMap.put(MAP_KEY_RESULT, reList);
			return reMap;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 根据任务id获取任务列表的数据源
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FlowBean> getFlowList(List<FlowBean> list) {
		for(FlowBean f : list){
			try {
				String str = fdao.findSourceById(f.getFLOW_ID());
				String[] strs = str.split("逗号,");
				String dbNames = "";
				int i = 0;
				for(String code : strs){
					if("".equals(code) || code == null){
						continue;
					}
					JDBCReaderBean jb = new JDBCReaderBean(code);
					String s = jb.getDataSource();
					String name = fdao.findSourceNameById(s);
					if(i == 0){
						dbNames = name;
					}else{
						dbNames = dbNames + "," + name;
					}
					i++;
				}
				f.setEXT3(dbNames);
				if(f.getEXT4() == null || "".equals(f.getEXT4())){
					f.setEXT4(f.getCREATE_TIME());
				}
			} catch (TransformException e) {
				log.error(e.getMessage(), e);
			}
		}
		return list;
	}

	public static void main(String[] str){
		DEEConfigService service = DEEConfigService.getInstance();
		Map<String,Object> resultMap = service.getFlowList(null, DEEConfigService.MODULENAME_OTHER, null, 1, 20);
		List<FlowBean> list = (List<FlowBean>)resultMap.get(MAP_KEY_RESULT);
		System.out.println(list.size());
	}
	/**
	 * 获取转换任务结果数据集描述
	 * 
	 * @param flowId
	 * @return
	 */
	public String getFlowMeta(String flowId) {
		try {
			FlowBean fb = fdao.get(flowId);
			return fb.getFLOW_META();
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 根据flowid获取flow
	 * 
	 * @param flowId
	 * @return
	 */
	public FlowBean getFlow(String flowId) {
		try {
			FlowBean fb = fdao.get(flowId);
            if (fb != null && fb.getDIS_NAME() != null) {
                fb.setDIS_NAME(StringEscapeUtils.unescapeHtml(fb.getDIS_NAME()));
            }
			return fb;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 提供flow类型list，包括节点关系信息
	 * 
	 * @return
	 */
	public List<FlowTypeBean> getFlowTypeList() {
		try {
			return ftdao.findAll();
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 获取flow参数列表
	 * 
	 * @param flowid
	 * @return
	 */
	public List<ParameterBean> getFlowPara(String flowid) {
		try {
			return paradao.getById(flowid);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	//*************************DeeResource begin***************//
	/**
	 * 获取全部数据源列表
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeeResourceBean> getAllDataResList(){
		List<DeeResourceBean> retDr = new ArrayList<DeeResourceBean>();
		try {
			List<DeeResourceBean> tempDr = drDao.findAll();
			if(tempDr == null)
				return null;
            for (DeeResourceBean drBean : tempDr) {

                if (drBean != null &&
                        (Integer.toString(DeeResourceEnum.JDBCDATASOURCE.ordinal()).equals(drBean.getResource_template_id()) ||
                        Integer.toString(DeeResourceEnum.JNDIDataSource.ordinal()).equals(drBean.getResource_template_id()) ||
                        Integer.toString(DeeResourceEnum.A8MetaDatasource.ordinal()).equals(drBean.getResource_template_id()))) {
                    retDr.add(drBean);
                }
            }
			return retDr;
//			return drDao.findByTemplateId("");
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 获取全部资源项列表
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeeResourceBean> getAllResList(){
		try {
			return drDao.findAll();
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 根据条件资源项列表
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DeeResourceBean getResByResId(String resourceId){
		try {
			return drDao.findById(resourceId);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * 根据任务号获取资源项列表
	 * 
	 * @param flowId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DeeResourceBean> getResListByFlowId(String flowId){
		try {
			return drDao.findResourceList(flowId);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 根据资源号更新资源信息
	 * 
	 * @param drb
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean updateRes(DeeResourceBean drb){
		Boolean retFlag = false;
		try { 
			drDao.update(drb);
			retFlag = true;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return retFlag;
	}
	
	//*************************DeeResource end*****************//
	//*************************Schedule begin******************//
	/**
	 * 获取所有定时器列表
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ScheduleBean> getAllScheduleList(){
		try {
			return sdDao.queryAll();
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 根据任务号获取定时器
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ScheduleBean getScheduleByFlowId(String flowId){
		try {
			return sdDao.get(flowId);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * 判断任务是否引用定时器
	 * 
	 * @param
	 * @return
	 */
	public Boolean getFlowWithSchedule(List<String> flowList){
        try {
            return sdDao.select(flowList);
        } catch (TransformException e) {
            log.error(e.getMessage(), e);
        }
        return false;
	}
	
	
	/**
	 * 根据定时器号更新定时器
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean updateSchedule(ScheduleBean sdBean){
		Boolean retFlag = false;
		try { 
			sdDao.update(sdBean);
			retFlag = true;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return retFlag;
	}
	
	//*************************Schedule end********************//
	/**
	 * 取得模块开关状态
	 * 
	 * @param moduleName
	 * @return
	 */
	public boolean getModuleState(String moduleName) {
		FlowModuleBean fmb;
		try {
			fmb = fmdao.getModuleByName(moduleName);
			return fmb != null ? fmb.getService_flag() : false;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	//*************************Redo begin**********************//
	public Map<String,Object> getRedoList(String syncId, String stateFlag,
			int pageNum, int pageSize) {
		Page<RedoBean> page = new Page<RedoBean>();
		page.setPageNo(pageNum);
		page.setPageSize(pageSize);
		
		RedoBean redoBean = new RedoBean();
//		redoBean.setFlow_id(flow_id);
		redoBean.setSync_id(syncId);
		redoBean.setState_flag(stateFlag);
		try {
			page = rddao.query(page, redoBean);
			long total = page.getTotalCount();
			List<RedoBean> reList = (List<RedoBean>) page.getResult();
			HashMap<String, Object> reMap = new HashMap<String, Object>();
			reMap.put(MAP_KEY_TOTALCOUNT, total);
			reMap.put(MAP_KEY_RESULT, reList);
			return reMap;
		} catch (TransformException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	public Map<String,Object> getSyncLogList(String flow_id,
			int pageNum, int pageSize) {
		Page<SyncBean> page = new Page<SyncBean>();
		page.setPageNo(pageNum);
		page.setPageSize(pageSize);
		
		SyncBean syBean = new SyncBean();
		syBean.setFlow_id(flow_id);
		try {
			page = syDao.query(page, syBean);
			long total = page.getTotalCount();
			List<SyncBean> reList = (List<SyncBean>) page.getResult();
			HashMap<String, Object> reMap = new HashMap<String, Object>();
			reMap.put(MAP_KEY_TOTALCOUNT, total);
			reMap.put(MAP_KEY_RESULT, reList);
			return reMap;
		} catch (TransformException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	public Map<String,Object> getSyncLogList(Map<String, Object> param,
			int pageNum, int pageSize) {
		Page<SyncBeanLog> page = new Page<SyncBeanLog>();
		page.setPageNo(pageNum);
		page.setPageSize(pageSize);
		
		SyncBeanLog syBean = new SyncBeanLog();
		syBean.setSync_state(-1);
		if(!"".equals(String.valueOf(param.get("name"))) && param.get("name") != null){
			syBean.setFlow_dis_name(String.valueOf(param.get("name")));
		}else if(!"".equals(String.valueOf(param.get("state"))) && param.get("state") != null){
			syBean.setSync_state(Integer.parseInt(String.valueOf(param.get("state"))));
		}else if(!"".equals(String.valueOf(param.get("time"))) && param.get("time") != null){
			syBean.setSync_time(String.valueOf(param.get("time")));
		}
		try {
			page = syDao.queryByCondition(page, syBean);
			long total = page.getTotalCount();
			List<SyncBeanLog> reList = (List<SyncBeanLog>) page.getResult();
			HashMap<String, Object> reMap = new HashMap<String, Object>();
			reMap.put(MAP_KEY_TOTALCOUNT, total);
			reMap.put(MAP_KEY_RESULT, reList);
			return reMap;
		} catch (TransformException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 获取SyncBean列表
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SyncBean getSyncBySyncId(String syncId){
		try {
			return syDao.findById(syncId);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 获取RedoList列表
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RedoBean> getAllRedoList(String syncId, String stateFlag){
		try {
			RedoBean bean = new RedoBean();
			bean.setSync_id(syncId);
			bean.setState_flag(stateFlag);
			return rddao.findAll(bean);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 获取RedoBean
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RedoBean getRedoByRedoId(String redoId){
		try {
			return rddao.findById(redoId);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	public void redoByFlowid(String flowid){
		try {
			RedoBean bean = new RedoBean();
			bean.setFlow_id(flowid);
			List<RedoBean> list = rddao.findAll(bean);
			for (RedoBean redoBean : list) {
				redo(redoBean.getRedo_id());
			}
		} catch (TransformException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	public HashMap<String, String> redo(String[] redo_ids){
		HashMap<String, String> errMap = new HashMap<String, String>();
		for (int i = 0; i < redo_ids.length; i++) {
			errMap.put(redo_ids[i], redo(redo_ids[i]));
		}
		return errMap;
	}
	
	public String redo(String redo_id){
		try {
//			DEEClient client = new DEEClient();
			RedoBean redoBean = rddao.findById(redo_id);
			if(redoBean == null)
				return "未找到该条错误记录";
			Flow flow = null;
			try {
				flow = (Flow) EngineController.getInstance(null).lookup(redoBean.getFlow_id());
				if(flow != null){
					redoBean.getPara().add("oldSyscId", redoBean.getSync_id());
					flow.redo(new XMLDataSource(redoBean.getDoc_code()).parse(), redoBean.getWriter_name(), redoBean.getPara(), EngineConfig.getInstance().parse(),redoBean.getRedo_id());
				}else{
					throw new TransformException("转换任务:"+redoBean.getFlow_id()+"不存在！");
				}
			} 
			catch (Throwable e) {
				// TODO Auto-generated catch block
				throw new TransformException(e);
			}
			return "";
		} catch (TransformException e) {
			log.error(e.getMessage(),e);
			return e.getMessage();
		}
	}
	
	/**
	 * 根据Redo信息
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Boolean updateRedoBean(RedoBean rb){
		Boolean retFlag = false;
		try { 
			try {
				rddao.update(rb);
				retFlag = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage(), e);
			}
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return retFlag;
	}
	public void skipRedo(String redo_id){
		try {
			rddao.updateState(redo_id, RedoBean.STATE_FLAG_SKIP);
		} catch (TransformException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	public void skipRedo(String[] redo_ids){
		for (int i = 0; i < redo_ids.length; i++) {
			skipRedo(redo_ids[i]);
		}
	}
	
	public Boolean delSyncBySyncId(String syncId){
		Boolean retFlag = false;
		try { 
			if(syncId == null)
				return retFlag;
			rddao.delAll(syncId);
			retFlag = true;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return retFlag;
	}
	
	public Boolean delSyncByRedoId(String syncId,String redoId){
		Boolean retFlag = false;
		try { 
			if(syncId == null || redoId == null)
				return retFlag;
			rddao.delSelect(syncId, redoId);
			retFlag = true;
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
		return retFlag;
	}
	//*************************Redo end**********************//

    /**
     * 根据flow_id，删除flow
     *
     * @param flow_id
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public void delFlow(String flow_id) throws TransformException {
        fdao.delFlowInfoForV5(flow_id);
    }

    /**
     * 根据id数据，删除资源
     *
     * @param ids id数组
     * @throws com.seeyon.v3x.dee.TransformException
     */
    public void delResources(String[] ids) throws TransformException {
        drDao.deleteByIds(ids);
    }
    /**
     * 根据日期删除日志
     */
    public void delLogs(String date) throws TransformException {
    	rddao.delLogs(date);
    }
    /**
     * 查询删除需要删除日志
     */
    public String findLogByDate(String date) throws TransformException {
    	return rddao.findLogByDate(date);
    }
}
