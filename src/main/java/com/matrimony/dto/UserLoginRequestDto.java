package com.matrimony.dto;

import static com.matrimony.dto.Constants.*;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserLoginRequestDto {

	@NotEmpty(message =USERNAME_EMPTY_MESSAGE)
	private String userName;

	@NotEmpty(message = PASSWORD_EMPTY_MESSAGE)
	private String password;

}
