package com.feixun.bean;

public class UserBean implements java.io.Serializable {
	private static final long serialVersionUID = 1285974782046676189L;
	
	private String username;
	
	public UserBean(){}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
