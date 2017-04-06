package com.seeyon.v3x.dee.common.listener;

import com.seeyon.v3x.dee.Document;
import com.seeyon.v3x.dee.TransformException;
import com.seeyon.v3x.dee.common.db.redo.dao.DEERedoDAO;
import com.seeyon.v3x.dee.common.db.redo.dao.DEESyncDAO;
import com.seeyon.v3x.dee.common.db.redo.model.FormFlowBean;
import com.seeyon.v3x.dee.common.db.redo.model.RedoBean;
import com.seeyon.v3x.dee.common.db.redo.model.SyncBean;
import com.seeyon.v3x.dee.common.db.redo.util.SyncState;
import com.seeyon.v3x.dee.context.Flow;
import com.seeyon.v3x.dee.event.ErrorEvent;
import com.seeyon.v3x.dee.event.FlowEndEvent;
import com.seeyon.v3x.dee.event.FlowStartEvent;
import com.seeyon.v3x.dee.event.ListenEvent;
import com.seeyon.v3x.dee.event.RedoErrorEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 转换任务事件监听器<br>
 * 监听事件:<br>
 * FlowStartEvent任务开始<br>
 * FlowEndEvent任务结束<br>
 * ErrorEvent任务异常<br>
 * RedoErrorEvent重发异常事件
 * 
 * @author lilong
 * @date 2012-06-27
 */
public class SyncListener {

	private final static Log log = LogFactory.getLog(SyncListener.class);

	private DEERedoDAO redoDAO = new DEERedoDAO();
	private DEESyncDAO syncDAO = new DEESyncDAO();

	/**
	 * 监听带有需要记录同步日志的flow，即配置了listener的flow
	 * 
	 * @param event
	 */

	@ListenEvent(event = FlowStartEvent.class)
	public void insertSyncHistory(FlowStartEvent event) {
		try {
			SyncBean syncBean = new SyncBean();
			syncBean.setSync_state(SyncState.STATE_FLAG_FAILE.ordinal());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date now = new Date();
			String execTime = sdf.format(now);
			syncBean.setSync_time(execTime);
			syncBean.setFlow_id(((Flow) event.getSource()).getName());
			syncBean.setSync_id(event.getContext().getId()); 
			Map<String, String> formFlow_Data = (Map<String, String>) event.getContext().getParameters().getValue("formFlow_Data");
			if(formFlow_Data != null){
				if(formFlow_Data.size() != 0){
					FormFlowBean ffb = new FormFlowBean();
					ffb.setFlow_sync_id(event.getContext().getId());
					ffb.setForm_flow_name(formFlow_Data.get("name"));
					ffb.setForm_flow_id(formFlow_Data.get("id"));
					ffb.setOperate_person(formFlow_Data.get("person"));
					String action = formFlow_Data.get("action");
					if("takeback".equals(action)){
						action = "取回";
					}else if("repeal".equals(action)){
						action = "撤销";
					}else if("dealSaveWait".equals(action)){
						action = "暂存代办";
					}else if("stepstop".equals(action)){
						action = "终止";
					}else if("stepback".equals(action)){
						action = "回退";
					}else if("submit".equals(action)){
						action = "提交";
					}else if("start".equals(action)){
						action = "发起";
					}else if("流程发送".equals(action)){
						action = "首次条件满足";
					}else if("核定通过或者流程结束".equals(action)){
						action = "核定节点通过";
					}else if("发送或者流程结束".equals(action)){
						action = "流程结束";
					}
					ffb.setFlow_action(action);
					syncDAO.insertNewLogs(syncBean, ffb);
				}
			}else{
				syncDAO.insert(syncBean);
			}
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * 监听转换任务结束事件
	 * 
	 * @param event
	 */
	@ListenEvent(event = FlowEndEvent.class)
	public void updateSyncHistory(FlowEndEvent event) {
		SyncBean syncBean = null;
		try {
			//flow执行结束事件，判断如果带有重发标记带有重发id则将关联redo_id的同步日志去更新状态，否则将正常产生同步日志的状态更新
			String redoId = event.getContext().getAttribute(
					Flow.ATTRIBUTE_KEY_REDOID) + "";
//			String isRedo = (String)event.getContext().getAttribute("REDO"); //是否已增加了重复次数
			if (!"null".equals(redoId) && StringUtils.isNotBlank(redoId)) {
				syncBean = syncDAO.findSyncBeanByRedoId(redoId);
//				if(isRedo == null || !"1".equals(isRedo)){
//					redoDAO.updateCountById(redoId);
//					redoDAO.updateState(redoId, RedoBean.STATE_FLAG_SUCESS);
//				}
			} 
//			else {
//				syncBean = new SyncBean();
//				syncBean.setSync_id(event.getContext().getId());
//			}
			if(syncBean == null){
                syncBean = new SyncBean();
                syncBean.setSync_id(event.getContext().getId());
			}
			//检查redo中是否有错误记录，如果没有则修改日志状态
			RedoBean newBean = new RedoBean();
			newBean.setSync_id(syncBean.getSync_id());
			newBean.setState_flag(RedoBean.STATE_FLAG_FAILE);
			redoDAO.updateState(redoId, RedoBean.STATE_FLAG_SUCESS);
			if(redoDAO.findAll(newBean).size() == 0){
				syncBean.setSync_id(syncBean.getSync_id());
				syncBean.setSync_state(SyncState.STATE_FLAG_SUCESS.ordinal());
				syncDAO.update(syncBean);
			}
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * 监听flow转换中出错的事件
	 * 
	 * @param event
	 */
	@ListenEvent(event = ErrorEvent.class)
	public void insertRedo(ErrorEvent event) {
		try {
			Document document = event.getDocument();
			RedoBean redoBean = new RedoBean();
			redoBean.setWriter_name(document.getContext().getAttribute(
					Flow.ATTRIBUTE_KEY_TASKNAME)
					+ "");
			redoBean.setDoc_code(document.toString());
			redoBean.setPara(document.getContext().getParameters().remove("flow"));
			redoBean.setCounter(0);
			redoBean.setState_flag(SyncState.STATE_FLAG_FAILE.ordinal() + "");
			redoBean.setFlow_id(((Flow) event.getSource()).getName());
			redoBean.setSync_id(document.getContext().getId());
			redoBean.setErrormsg(event.getMessage());// 异常信息也同时写到redo表中
			// TODO 测试验证
			redoDAO.insert(redoBean);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
	}

	/*
	 * 监听重发过程中的异常事件
	 * <br>目前功能只增加重发计数
	 * @param event
	 */
	@ListenEvent(event = RedoErrorEvent.class)
	public void updateSyncRedo(RedoErrorEvent event) {
		String redoId = (String)event.getContext().getAttribute(Flow.ATTRIBUTE_KEY_REDOID);
		try {
			redoDAO.updateCountById(redoId);
		} catch (TransformException e) {
			log.error(e.getMessage(), e);
		}
	}

}
