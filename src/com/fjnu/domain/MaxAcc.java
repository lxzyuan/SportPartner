package com.fjnu.domain;

public class MaxAcc {
	
	private int acceleration;
	private int time;
	
	public MaxAcc(){
		
	}
	public MaxAcc(int acceleration,int time){
		this.acceleration = acceleration;
		this.time = time;
	}
	public int getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}


}
