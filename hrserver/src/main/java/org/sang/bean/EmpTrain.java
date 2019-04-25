package org.sang.bean;

import java.util.Date;
import java.util.List;

public class EmpTrain {
	private Long eid;
	private String name;
	private String depname;
	private int id;
	private Date trainDate;
	private Date trainendDate;
	private String trainContent;
	private String remark;
	private String steps;
	private int state;
	private int[] emps;
	
	
	
	public int[] getEmps() {
		return emps;
	}
	public void setEmps(int[] emps) {
		this.emps = emps;
	}
	public String getSteps() {
		return steps;
	}
	public void setSteps(String steps) {
		this.steps = steps;
	}
	public Long getEid() {
		return eid;
	}
	public void setEid(Long eid) {
		this.eid = eid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepname() {
		return depname;
	}
	public void setDepname(String depname) {
		this.depname = depname;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getTrainDate() {
		return trainDate;
	}
	public void setTrainDate(Date trainDate) {
		this.trainDate = trainDate;
	}
	public Date getTrainendDate() {
		return trainendDate;
	}
	public void setTrainendDate(Date trainendDate) {
		this.trainendDate = trainendDate;
	}
	public String getTrainContent() {
		return trainContent;
	}
	public void setTrainContent(String trainContent) {
		this.trainContent = trainContent;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
