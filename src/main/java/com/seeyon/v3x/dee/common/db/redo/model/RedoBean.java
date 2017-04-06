package com.seeyon.v3x.dee.common.db.redo.model;

import com.seeyon.v3x.dee.Parameters;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Entity
@Table(name="dee_redo")
public class RedoBean implements Serializable {
	public static final String STATE_FLAG_FAILE ="0";
	public static final String STATE_FLAG_SUCESS ="1";
	public static final String STATE_FLAG_SKIP ="2";
	
	private String redo_id;
	private int redo_sid;
	private String writer_name;
	private String doc_code;
	private String flow_id;
	private Parameters para;
	private int counter;
	private String state_flag;
	private String sync_id;
	private SyncBean syncBean;
	private String errormsg;
	private byte[] param;
	@Id
	public String getRedo_id() {
		return redo_id;
	}
	public void setRedo_id(String redo_id) {
		this.redo_id = redo_id;
	}
	public int getRedo_sid() {
		return redo_sid;
	}
	public void setRedo_sid(int redo_sid) {
		this.redo_sid = redo_sid;
	}
	public String getWriter_name() {
		return writer_name;
	}
	public void setWriter_name(String writer_name) {
		this.writer_name = writer_name;
	}
	public String getDoc_code() {
		return doc_code;
	}
	public void setDoc_code(String doc_code) {
		this.doc_code = doc_code;
	}
	@Transient
	public String getFlow_id() {
		return flow_id;
	}
	public void setFlow_id(String flow_id) {
		this.flow_id = flow_id;
	}
	@Transient
	public Parameters getPara() {
		return para;
	}
	public void setPara(Parameters para) {
		this.para = para;
	}
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public String getState_flag() {
		return state_flag;
	}
	public void setState_flag(String state_flag) {
		this.state_flag = state_flag;
	}
	@Transient
	public String getSync_id() {
		return sync_id;
	}
	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="sync_id")
	public SyncBean getSyncBean() {
		return syncBean;
	}
	public void setSyncBean(SyncBean syncBean) {
		this.syncBean = syncBean;
	}
	@Column(name="para")
    public byte[] getParam() throws Exception {
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this.para!=null?this.para.remove("flow"):null);
        bos.close();
        oos.close();
        return bos.toByteArray();
    }
    public void setParam(byte[] param) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(param);
        ObjectInputStream ois = new ObjectInputStream(bis);
        this.para = (Parameters) ois.readObject();
        ois.close();
        bis.close();
        this.param = param;
    }
}
