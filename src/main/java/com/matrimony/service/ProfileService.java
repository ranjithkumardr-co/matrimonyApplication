package com.matrimony.service;

import java.util.List;

import javax.validation.Valid;

import com.matrimony.dto.FilterRequestDto;
import com.matrimony.dto.FilterResponseDto;

public interface ProfileService {

	List<FilterResponseDto> profileSearch(@Valid FilterRequestDto filterRequestDto);

}
