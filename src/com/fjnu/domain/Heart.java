package com.fjnu.domain;

public class Heart {

	/*
	 * [ {id:0,datetime:23432,data:67}, {id:1,datetime:25432,data:79} ]
	 */

	private int datetime;
	private int data;

	public Heart() {

	}

	public Heart(int datetime, int data) {
		this.datetime = datetime;
		this.data = data;

	}

	public int getDatetime() {
		return datetime;
	}

	public void setDatetime(int datetime) {
		this.datetime = datetime;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}



	
}
