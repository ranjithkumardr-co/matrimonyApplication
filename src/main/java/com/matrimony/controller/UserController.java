package com.matrimony.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.dto.AcceptOrDeclineRequestDto;
import com.matrimony.dto.LikeUserRequestDto;
import com.matrimony.dto.UpdateRequestDto;
import com.matrimony.dto.UpdateResponseDto;
import com.matrimony.dto.UserLoginRequestDto;
import com.matrimony.dto.UserLoginResponseDto;
import com.matrimony.service.UserService;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(value = "/like")
	public ResponseEntity<String> likeUser(@Valid @RequestBody LikeUserRequestDto likeUserRequestDto) {

		return new ResponseEntity<>(userService.likeUser(likeUserRequestDto), HttpStatus.OK);

	}

	@PostMapping(value = "/login")
	public ResponseEntity<List<UserLoginResponseDto>> userLogin(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {

		return new ResponseEntity<>(userService.userLogin(userLoginRequestDto), HttpStatus.OK);

	}
	
	@PostMapping(value = "/favorite/accept")
	public ResponseEntity<String> acceptFavoriteRequest(@Valid @RequestBody AcceptOrDeclineRequestDto acceptOrDeclineRequestDto) {

		return new ResponseEntity<>(userService.acceptFavoriteRequest(acceptOrDeclineRequestDto), HttpStatus.OK);

	}
	
	@PostMapping(value = "/favorite/decline")
	public ResponseEntity<String> declineFavoriteRequest(@Valid @RequestBody AcceptOrDeclineRequestDto acceptOrDeclineRequestDto) {

		return new ResponseEntity<>(userService.declineFavoriteRequest(acceptOrDeclineRequestDto), HttpStatus.OK);

	}
	
	@PostMapping(value = "/profile/update")
	public ResponseEntity<UpdateResponseDto> updateUserProfile(@Valid @RequestBody UpdateRequestDto updateRequestDto) {

		return new ResponseEntity<>(userService.updateUserProfile(updateRequestDto), HttpStatus.OK);

	}
}
