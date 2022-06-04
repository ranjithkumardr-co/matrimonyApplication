package com.matrimony.dto;

import lombok.Data;

@Data
public class FilterResponseDto {
	
	private Integer userId;
	private String firstName;
	private String lastName;
	private Integer age;
	private Integer height;
	private String religion;
	private String location;
	private Integer weight;
	private String motherTongue;

}
