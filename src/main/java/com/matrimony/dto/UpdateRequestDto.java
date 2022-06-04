package com.matrimony.dto;

import static com.matrimony.dto.Constants.*;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class UpdateRequestDto {
	
	@NotNull(message = VALID_USERID_MESSAGE )
	private Integer userId;
	
	@Max(value = 120,message =VALID_AGE_MESSAGE )
	@Min(value = 1,message =VALID_AGE_MESSAGE )
	private Integer age;

	@Max(value = 10,message = VALID_HEIGHT_MESSAGE)
	@Min(value = 1,message = VALID_HEIGHT_MESSAGE)
	private Integer height;

	@Pattern(message = VALID_LOCATION_MESSAGE, regexp = "(^[a-zA-Z]+$)")
	private String location;
	
	@Max(value = 200,message = VALID_WEIGHT_MESSAGE)
	@Min(value = 1,message = VALID_WEIGHT_MESSAGE)
	private Integer weight;

}
