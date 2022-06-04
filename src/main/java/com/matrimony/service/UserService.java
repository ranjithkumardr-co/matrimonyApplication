package com.matrimony.service;

import java.util.List;

import javax.validation.Valid;

import com.matrimony.dto.AcceptOrDeclineRequestDto;
import com.matrimony.dto.LikeUserRequestDto;
import com.matrimony.dto.UpdateRequestDto;
import com.matrimony.dto.UpdateResponseDto;
import com.matrimony.dto.UserLoginRequestDto;
import com.matrimony.dto.UserLoginResponseDto;

public interface UserService {

	String likeUser(@Valid LikeUserRequestDto likeUserRequestDto);

	List<UserLoginResponseDto> userLogin(@Valid UserLoginRequestDto userLoginRequestDto);

	String acceptFavoriteRequest(@Valid AcceptOrDeclineRequestDto acceptOrDeclineRequestDto);

	UpdateResponseDto updateUserProfile(@Valid UpdateRequestDto updateRequestDto);

	String declineFavoriteRequest(@Valid AcceptOrDeclineRequestDto acceptOrDeclineRequestDto);

}
