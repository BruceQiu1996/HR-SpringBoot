package org.sang.bean;

public class TrainDetails {
	private int id;
	private int empTrainID;
	private int step;
	private String stepContent;
	public int getEmpTrainID() {
		return empTrainID;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmpTrainID(int empTrainID) {
		this.empTrainID = empTrainID;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getStepContent() {
		return stepContent;
	}
	public void setStepContent(String stepContent) {
		this.stepContent = stepContent;
	}
	
}
