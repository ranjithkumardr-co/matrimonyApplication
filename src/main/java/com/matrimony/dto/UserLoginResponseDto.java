package com.matrimony.dto;

import lombok.Data;

@Data
public class UserLoginResponseDto {

	private Integer userId;
	private String firstName;
	private String lastName;
	private Long phoneNo;
	private String emailId;
	private Integer age;
	private Integer height;
	private String religion;
	private String location;
	private Integer weight;
	
}
