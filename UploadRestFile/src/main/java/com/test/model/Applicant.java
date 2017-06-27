package com.test.model;

import java.io.File;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;;


@Component
public class Applicant {
	
	String file;
	String fname;
	String City;
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	String phone_num;
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}

	
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	
	

}
