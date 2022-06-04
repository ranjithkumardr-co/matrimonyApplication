package com.matrimony.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.matrimony.dto.FilterRequestDto;
import com.matrimony.dto.FilterResponseDto;
import com.matrimony.service.ProfileService;

@RestController
public class ProfileController {

	
	@Autowired
	ProfileService profileService;
	
	
	@PostMapping(value = "/profile/search")
	public ResponseEntity<List<FilterResponseDto>> profileSearch(@Valid @RequestBody FilterRequestDto filterRequestDto) {

		return new ResponseEntity<>(profileService.profileSearch(filterRequestDto), HttpStatus.OK);

	}
	
}
