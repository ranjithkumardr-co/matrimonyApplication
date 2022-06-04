package com.matrimony.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import com.matrimony.dto.FilterRequestDto;
import com.matrimony.dto.FilterResponseDto;
import com.matrimony.entity.Profile;
import com.matrimony.entity.User;
import com.matrimony.repo.ProfileRepository;
import com.matrimony.repo.UserRepository;
import com.matrimony.service.impl.ProfileServiceImpl;

@SpringBootTest
class ProfileServiceTest {

	@Mock
	ProfileRepository profileRepository;

	@Mock
	UserRepository userRepository;

	@InjectMocks
	ProfileServiceImpl profileServiceImpl;

	FilterRequestDto filterRequestDto;

	FilterResponseDto filterResponseDto;

	List<FilterResponseDto> filterResponseDtoList;

	Profile profile;

	List<Profile> profileList;

	User user;

	@BeforeEach
	void setUp() {
		filterRequestDto = new FilterRequestDto();
		filterRequestDto.setAge(21);
		filterRequestDto.setHeight(5);
		filterRequestDto.setLocation("Banglore");
		filterRequestDto.setReligion("Hindu");

		filterResponseDto = new FilterResponseDto();
		BeanUtils.copyProperties(filterRequestDto, filterResponseDto);

		filterResponseDtoList = new ArrayList<FilterResponseDto>();
		filterResponseDtoList.add(filterResponseDto);

		profile = new Profile();
		BeanUtils.copyProperties(filterRequestDto, profile);

		profileList = new ArrayList<>();
		profileList.add(profile);

		user = new User();
		user.setUserId(1);
		user.setUserName("ranjithkumardr");
		user.setPassword("ranjith@123");
		user.setFirstName("Ranjith");
		user.setLastName("Kumar");
		user.setProfile(profile);

		profile.setUser(user);

	}

	@Test
	@DisplayName("Filter Search:Positive")
	void profile_Search_Positive() {
		// context

		when(profileRepository.findByAge(filterRequestDto.getAge())).thenReturn(profileList);
		when(profileRepository.findByLocation(filterRequestDto.getLocation())).thenReturn(profileList);
		when(profileRepository.findByReligion(filterRequestDto.getReligion())).thenReturn(profileList);
		when(profileRepository.findByHeight(filterRequestDto.getHeight())).thenReturn(profileList);
		when(userRepository.findByProfile(profile)).thenReturn(user);

		// event
		List<FilterResponseDto> result = profileServiceImpl.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.size());

	}

	@Test
	@DisplayName("Filter Search taking age as null:Positive")
	void profile_Searchfor_AgeAsNull_Positive() {
		// context

		when(profileRepository.findByAge(null)).thenReturn(profileList);
		when(profileRepository.findByLocation(filterRequestDto.getLocation())).thenReturn(profileList);
		when(profileRepository.findByReligion(filterRequestDto.getReligion())).thenReturn(profileList);
		when(profileRepository.findByHeight(filterRequestDto.getHeight())).thenReturn(profileList);
		when(userRepository.findByProfile(profile)).thenReturn(user);

		// event
		List<FilterResponseDto> result = profileServiceImpl.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.size());

	}

	@Test
	@DisplayName("Filter Search taking age and location as null:Positive")
	void profile_Searchfor_AgeAndLocationNull_Positive() {
		// context

		when(profileRepository.findByAge(null)).thenReturn(profileList);
		when(profileRepository.findByLocation(null)).thenReturn(profileList);
		when(profileRepository.findByReligion(filterRequestDto.getReligion())).thenReturn(profileList);
		when(profileRepository.findByHeight(filterRequestDto.getHeight())).thenReturn(profileList);
		when(userRepository.findByProfile(profile)).thenReturn(user);

		// event
		List<FilterResponseDto> result = profileServiceImpl.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.size());

	}

	@Test
	@DisplayName("Filter Search taking age and location and Religion null:Positive")
	void profile_Searchfor_AgeAndLocationAndReligionNull_Positive() {
		// context

		when(profileRepository.findByAge(null)).thenReturn(profileList);
		when(profileRepository.findByLocation(null)).thenReturn(profileList);
		when(profileRepository.findByReligion(null)).thenReturn(profileList);
		when(profileRepository.findByHeight(filterRequestDto.getHeight())).thenReturn(profileList);
		when(userRepository.findByProfile(profile)).thenReturn(user);

		// event
		List<FilterResponseDto> result = profileServiceImpl.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.size());

	}

}
