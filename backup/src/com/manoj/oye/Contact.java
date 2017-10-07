package com.manoj.oye;


public class Contact {
	private String contactName;
	private String contactNumber;
	private int recvPoke;
	private int sentPoke;
	private long lastTime;
	private int newpokes;
	private String message;
	private String new_message;
	
	Contact(String name, String number, int rpoke,int spoke,long time,int newpokes, String message)
	{
		this.contactName=name;
		this.contactNumber=number;
		this.recvPoke=rpoke;
		this.sentPoke=spoke;
		this.lastTime = time;
		this.newpokes = newpokes;
		this.message = message;
		this.new_message = "no";
	}
	
	Contact(String name, String number, int rpoke,int spoke,long time)
	{
		this.contactName=name;
		this.contactNumber=number;
		this.recvPoke=rpoke;
		this.sentPoke=spoke;
		this.lastTime = time;
		this.message = "";
		this.new_message = "no";
	}
	Contact(String name, String number, int rpoke,int spoke,long time,int newpokes)
	{
		this.contactName=name;
		this.contactNumber=number;
		this.recvPoke=rpoke;
		this.sentPoke=spoke;
		this.lastTime = time;
		this.newpokes = newpokes;
		this.message = "";
		this.new_message = "no";
	}
	Contact(String name, String number)
	{
		this.contactName=name;
		this.contactNumber=number;
		this.recvPoke=0;
		this.sentPoke=0;
		this.message = "";
		this.lastTime = 0;
		this.new_message = "no";
	}
	
	Contact(String name, String number, int rpoke,int spoke,long time,int newpokes, String message, String new_message)
	{
		this.contactName=name;
		this.contactNumber=number;
		this.recvPoke=rpoke;
		this.sentPoke=spoke;
		this.lastTime = time;
		this.newpokes = newpokes;
		this.message = message;
		this.new_message = new_message;
	}
	
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public int getRecvPoke() {
		return recvPoke;
	}
	public void setRecvPoke(int recvPoke) {
		this.recvPoke = recvPoke;
	}
	public int getSentPoke() {
		return sentPoke;
	}
	public void setSentPoke(int sentPoke) {
		this.sentPoke = sentPoke;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	public int getNewpokes() {
		return newpokes;
	}
	public void setNewpokes(int newpokes) {
		this.newpokes = newpokes;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getNew_message() {
		return new_message;
	}

	public void setNew_message(String new_message) {
		this.new_message = new_message;
	}
	
}
