package com.matrimony.dto;

import static com.matrimony.dto.Constants.*;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class FilterRequestDto {

	@Max(value = 120,message =VALID_AGE_MESSAGE )
	@Min(value = 1,message =VALID_AGE_MESSAGE )
	private Integer age;

	@Max(value = 10,message = VALID_HEIGHT_MESSAGE)
	@Min(value = 1,message = VALID_HEIGHT_MESSAGE)
	private Integer height;

	@Pattern(message = VALID_RELIGION_MESSAGE, regexp = "(^[a-zA-Z]+$)")
	private String religion;

	@Pattern(message = VALID_LOCATION_MESSAGE, regexp = "(^[a-zA-Z]+$)")
	private String location;

}
