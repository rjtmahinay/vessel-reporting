package com.karagathon.helper;

public class ModelStatistics {
	
	
	
	private Integer month;
	
	private Long value;

	public ModelStatistics(Integer month, Long value) {
		super();
		this.month = month;
		this.value = value;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ModelStatistics [month=" + month + ", value=" + value + "]";
	}
	
	
}
