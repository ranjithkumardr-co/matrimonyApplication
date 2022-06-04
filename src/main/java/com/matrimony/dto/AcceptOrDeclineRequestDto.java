package com.matrimony.dto;

import static com.matrimony.dto.Constants.*;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AcceptOrDeclineRequestDto {
	
	@NotNull(message = USERID_NOTNULL_MESSAGE)
	private Integer userId;
	@NotNull(message = ACCEPT_FAVORITE_USERID_NOTNULL_MESSAGE)
	private Integer acceptFavoriteId;

}
