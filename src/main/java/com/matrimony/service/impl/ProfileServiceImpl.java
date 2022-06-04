package com.matrimony.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matrimony.dto.FilterRequestDto;
import com.matrimony.dto.FilterResponseDto;
import com.matrimony.entity.Profile;
import com.matrimony.repo.ProfileRepository;
import com.matrimony.repo.UserRepository;
import com.matrimony.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

	@Autowired
	ProfileRepository profileRepository;

	@Autowired
	UserRepository userRepository;

	@Override
	public List<FilterResponseDto> profileSearch(@Valid FilterRequestDto filterRequestDto) {

		List<FilterResponseDto> mergedFilterResponseDtos = new ArrayList<>();

		mergedFilterResponseDtos.addAll(generateList(profileRepository.findByLocation(filterRequestDto.getLocation())));
		mergedFilterResponseDtos.addAll(generateList(profileRepository.findByHeight(filterRequestDto.getHeight())));
		mergedFilterResponseDtos.addAll(generateList(profileRepository.findByReligion(filterRequestDto.getReligion())));
		mergedFilterResponseDtos.addAll(generateList(profileRepository.findByAge(filterRequestDto.getAge())));

		return mergedFilterResponseDtos.stream().distinct()
				.collect(Collectors.toList());
		

	}

	public List<FilterResponseDto> generateList(List<Profile> profiles) {
		List<FilterResponseDto> filterResponseDtos = new ArrayList<>();
		profiles.forEach(profile -> {
			var filterResponseDto = new FilterResponseDto();
			BeanUtils.copyProperties(profile, filterResponseDto);
			BeanUtils.copyProperties(userRepository.findByProfile(profile), filterResponseDto);
			filterResponseDtos.add(filterResponseDto);
		});
		return filterResponseDtos;

	}

}
