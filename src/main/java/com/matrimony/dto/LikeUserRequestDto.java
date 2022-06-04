package com.matrimony.dto;


import static com.matrimony.dto.Constants.*;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class LikeUserRequestDto {

	
	@NotNull(message = USERID_NOTNULL_MESSAGE)
	private Integer userId;
	
	@NotNull(message =LIKEUSERID_NOTNULL_MESSAGE)
	private Integer likeUserId;
}
