package com.fjnu.domain;

public class Accele {
	
	private int acceleration;
	private int id;
	private int time;
	
	public Accele(){
		
	}
	public Accele(int acceleration,int id,int time){
		this.acceleration = acceleration;
		this.id = id;
		this.time = time;
	}
	public int getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}


}
