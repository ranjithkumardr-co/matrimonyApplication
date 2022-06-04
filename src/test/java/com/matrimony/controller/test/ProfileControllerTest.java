package com.matrimony.controller.test;

import static com.matrimony.dto.Constants.VALID_AGE_MESSAGE;
import static com.matrimony.dto.Constants.VALID_HEIGHT_MESSAGE;
import static com.matrimony.dto.Constants.VALID_LOCATION_MESSAGE;
import static com.matrimony.dto.Constants.VALID_RELIGION_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.matrimony.controller.ProfileController;
import com.matrimony.dto.FilterRequestDto;
import com.matrimony.dto.FilterResponseDto;
import com.matrimony.service.ProfileService;

@SpringBootTest
class ProfileControllerTest {

	@Mock
	ProfileService profileService;

	@InjectMocks
	ProfileController profileController;

	FilterRequestDto filterRequestDto;

	FilterResponseDto filterResponseDto;

	List<FilterResponseDto> filterResponseDtoList;
	
	Validator validator;

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
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}
	@Test
	@DisplayName("Filter Search:Positive")
	void profile_Search_Positive() {
		// context

		when(profileService.profileSearch(filterRequestDto)).thenReturn(filterResponseDtoList);

		// event
		ResponseEntity<List<FilterResponseDto>> result = profileController.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.getBody().size());

	}

	@Test
	@DisplayName("Filter Search:Negative")
	void profile_Search_Negative() {
		// context
		filterRequestDto.setLocation(null);
		when(profileService.profileSearch(filterRequestDto)).thenReturn(filterResponseDtoList);

		// event
		ResponseEntity<List<FilterResponseDto>> result = profileController.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.getBody().size());

	}
	@Test
	@DisplayName("Filter Search:Negative2")
	void profile_Search_Negative2() {
		// context
		filterRequestDto.setLocation(null);
		filterRequestDto.setHeight(null);
		when(profileService.profileSearch(filterRequestDto)).thenReturn(filterResponseDtoList);

		// event
		ResponseEntity<List<FilterResponseDto>> result = profileController.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.getBody().size());

	}
	@Test
	@DisplayName("Filter Search:Negative3")
	void profile_Search_Negative3() {
		// context
		filterRequestDto.setLocation(null);
		filterRequestDto.setHeight(null);
		filterRequestDto.setAge(null);
		when(profileService.profileSearch(filterRequestDto)).thenReturn(filterResponseDtoList);

		// event
		ResponseEntity<List<FilterResponseDto>> result = profileController.profileSearch(filterRequestDto);

		// outcome
		assertEquals(1, result.getBody().size());

	}
	@Test
	@DisplayName("Age Test:Negative")
	void ageTestMax_Negative() {
		filterRequestDto.setAge(300);
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_AGE_MESSAGE,violation.getMessage());
		});

	}
	@Test
	@DisplayName("Age Test:Negative")
	void ageTestMin_Negative() {
		filterRequestDto.setAge(0);
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_AGE_MESSAGE,violation.getMessage());
		});

	}
	@Test
	@DisplayName("Height Test:Negative")
	void heightTestMax_Negative() {
		filterRequestDto.setHeight(11);
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_HEIGHT_MESSAGE,violation.getMessage());
		});

	}
	@Test
	@DisplayName("Height Test:Negative")
	void heightTestMin_Negative() {
		filterRequestDto.setHeight(0);
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_HEIGHT_MESSAGE,violation.getMessage());
		});

	}
	
	@Test
	@DisplayName("Location Test:Negative")
	void locationTest_Negative() {
		filterRequestDto.setLocation("54451");
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_LOCATION_MESSAGE,violation.getMessage());
		});

	}

	
	@Test
	@DisplayName("Religion Test:Negative")
	void religion_Negative() {
		filterRequestDto.setReligion("225441");
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_RELIGION_MESSAGE,violation.getMessage());
		});

	}
	
	@Test
	@DisplayName("All Field Test for FilterRequestDto:Positive")
	void allFieldTestForFilterRequestDto_Positive() {
		
		Set<ConstraintViolation<FilterRequestDto>> violations = validator.validate(filterRequestDto);

		assertTrue(violations.isEmpty());

	}
	
}
