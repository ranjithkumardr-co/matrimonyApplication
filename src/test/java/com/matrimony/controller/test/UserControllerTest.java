package com.matrimony.controller.test;

import static com.matrimony.dto.Constants.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import com.matrimony.controller.UserController;
import com.matrimony.dto.AcceptOrDeclineRequestDto;
import com.matrimony.dto.LikeUserRequestDto;
import com.matrimony.dto.UpdateRequestDto;
import com.matrimony.dto.UpdateResponseDto;
import com.matrimony.dto.UserLoginRequestDto;
import com.matrimony.dto.UserLoginResponseDto;
import com.matrimony.exception.IdNotFoundException;
import com.matrimony.exception.WrongCredentialsException;
import com.matrimony.service.UserService;

@SpringBootTest
class UserControllerTest {

	@Mock
	UserService userservice;

	@InjectMocks
	UserController userController;

	LikeUserRequestDto likeUserRequestDto;

	UserLoginRequestDto loginRequestDto;

	UserLoginResponseDto loginResponseDto;

	List<UserLoginResponseDto> loginResponseDtoList;

	AcceptOrDeclineRequestDto acceptOrDeclineRequestDto;

	UpdateRequestDto updateRequestDto;

	UpdateResponseDto updateResponseDto;

	Validator validator;

	@BeforeEach
	void setup() {
		likeUserRequestDto = new LikeUserRequestDto();
		likeUserRequestDto.setUserId(1);
		likeUserRequestDto.setLikeUserId(2);

		loginRequestDto = new UserLoginRequestDto();
		loginRequestDto.setUserName("ranjithkumardr");
		loginRequestDto.setPassword("ranjith@123");

		loginResponseDto = new UserLoginResponseDto();
		loginResponseDto.setAge(26);
		loginResponseDto.setEmailId("ranjith@gmail.com");
		loginResponseDto.setFirstName("Ranjith");
		loginResponseDto.setHeight(5);
		loginResponseDto.setLastName("Kumar");
		loginResponseDto.setLocation("Banglore");
	

		loginResponseDtoList = new ArrayList<UserLoginResponseDto>();
		loginResponseDtoList.add(loginResponseDto);

		acceptOrDeclineRequestDto = new AcceptOrDeclineRequestDto();
		acceptOrDeclineRequestDto.setUserId(2);
		acceptOrDeclineRequestDto.setAcceptFavoriteId(1);

		updateRequestDto = new UpdateRequestDto();
		updateRequestDto.setAge(22);
		updateRequestDto.setHeight(5);
		updateRequestDto.setLocation("Chennai");
		updateRequestDto.setUserId(1);
		updateRequestDto.setWeight(50);

		updateResponseDto = new UpdateResponseDto();
		BeanUtils.copyProperties(loginResponseDto, updateResponseDto);

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	@Test
	@DisplayName("LikeUser:Positive")
	void likeUser_Positive() {
		// context

		when(userservice.likeUser(likeUserRequestDto)).thenReturn("User liked successfully");

		// event
		ResponseEntity<String> result = userController.likeUser(likeUserRequestDto);

		// outcome
		assertEquals("User liked successfully", result.getBody());

	}

	@Test
	@DisplayName("LikeUser:Negative")
	void likeUser_Negative() {
		// context

		when(userservice.likeUser(likeUserRequestDto)).thenThrow(new IdNotFoundException("User Id Not Found"));

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userController.likeUser(likeUserRequestDto);
		});

		// outcome
		assertEquals("User Id Not Found", e.getMessage());

	}

	@Test
	@DisplayName("UserLogin:Positive")
	void userLogin_Positive() {
		// context

		when(userservice.userLogin(loginRequestDto)).thenReturn(loginResponseDtoList);

		// event
		ResponseEntity<List<UserLoginResponseDto>> result = userController.userLogin(loginRequestDto);

		// outcome
		assertEquals(1, result.getBody().size());

	}

	@Test
	@DisplayName("User Login:Negative")
	void userLogin_Negative() {
		// context

		when(userservice.userLogin(loginRequestDto)).thenThrow(new WrongCredentialsException(WRONG_CREDENTIAL_MESSAGE));

		// event
		WrongCredentialsException e = assertThrows(WrongCredentialsException.class, () -> {
			userController.userLogin(loginRequestDto);
		});

		// outcome
		assertEquals(WRONG_CREDENTIAL_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Accept Favorite Request:Positive")
	void acceptFavoriteRequest_Positive() {
		// context

		when(userservice.acceptFavoriteRequest(acceptOrDeclineRequestDto)).thenReturn("User Request Accepted");

		// event
		ResponseEntity<String> result = userController.acceptFavoriteRequest(acceptOrDeclineRequestDto);

		// outcome
		assertEquals("User Request Accepted", result.getBody());

	}

	@Test
	@DisplayName("Accept Favorite Request:Negative")
	void acceptFavoriteRequest_Negative() {
		// context

		when(userservice.acceptFavoriteRequest(acceptOrDeclineRequestDto))
				.thenThrow(new IdNotFoundException(ID_NOT_FOUND_MESSAGE));

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userController.acceptFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}
	
	@Test
	@DisplayName("Decline Favorite Request:Positive")
	void declineFavoriteRequest_Positive() {
		// context

		when(userservice.declineFavoriteRequest(acceptOrDeclineRequestDto)).thenReturn(DECLINE_USER_MESSAGE);

		// event
		ResponseEntity<String> result = userController.declineFavoriteRequest(acceptOrDeclineRequestDto);

		// outcome
		assertEquals(DECLINE_USER_MESSAGE, result.getBody());

	}
	@Test
	@DisplayName("Decline Favorite Request:Negative")
	void declineFavoriteRequest_Negative() {
		// context

		when(userservice.declineFavoriteRequest(acceptOrDeclineRequestDto))
				.thenThrow(new IdNotFoundException(ID_NOT_FOUND_MESSAGE));

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userController.declineFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}


	@Test
	@DisplayName("Update User Profile:Positive")
	void updateUserProfile_Positive() {
		// context

		when(userservice.updateUserProfile(updateRequestDto)).thenReturn(updateResponseDto);

		// event
		ResponseEntity<UpdateResponseDto> result = userController.updateUserProfile(updateRequestDto);

		// outcome
		assertEquals(updateRequestDto.getHeight(), result.getBody().getHeight());

	}

	@Test
	@DisplayName("Update User Profile:Negative")
	void updateUserProfile_Negative() {
		// context

		when(userservice.updateUserProfile(updateRequestDto)).thenThrow(new IdNotFoundException(ID_NOT_FOUND_MESSAGE));

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userController.updateUserProfile(updateRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Username Test:Negative")
	void userNameTest_Negative() {
		loginRequestDto.setUserName("");
		Set<ConstraintViolation<UserLoginRequestDto>> violations = validator.validate(loginRequestDto);

		violations.forEach(violation -> {
			assertEquals(USERNAME_EMPTY_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Password Test:Negative")
	void passwordTest_Negative() {
		loginRequestDto.setPassword("");
		Set<ConstraintViolation<UserLoginRequestDto>> violations = validator.validate(loginRequestDto);

		violations.forEach(violation -> {
			assertEquals(PASSWORD_EMPTY_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("All Field Test for LoginDto:Positive")
	void allFieldTest_Positive() {

		Set<ConstraintViolation<UserLoginRequestDto>> violations = validator.validate(loginRequestDto);

		assertTrue(violations.isEmpty());

	}

	@Test
	@DisplayName("User Id Test:Negative")
	void userIdTest_Negative() {
		updateRequestDto.setUserId(null);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_USERID_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Age Test:Negative")
	void ageTestMax_Negative() {
		updateRequestDto.setAge(300);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_AGE_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Age Test:Negative")
	void ageTestMin_Negative() {
		updateRequestDto.setAge(0);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_AGE_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Height Test:Negative")
	void heightTestMax_Negative() {
		updateRequestDto.setHeight(11);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_HEIGHT_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Height Test:Negative")
	void heightTestMin_Negative() {
		updateRequestDto.setHeight(0);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_HEIGHT_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Weight Test:Negative")
	void weightTestMax_Negative() {
		updateRequestDto.setWeight(202);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_WEIGHT_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Weight Test:Negative")
	void weightTestMin_Negative() {
		updateRequestDto.setWeight(0);
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_WEIGHT_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Location Test:Negative")
	void location_Negative() {
		updateRequestDto.setLocation("225441");
		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		violations.forEach(violation -> {
			assertEquals(VALID_LOCATION_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("All Field Test for UpdateProfileDto:Positive")
	void allFieldTestForUpdateProfile_Positive() {

		Set<ConstraintViolation<UpdateRequestDto>> violations = validator.validate(updateRequestDto);

		assertTrue(violations.isEmpty());

	}

	@Test
	@DisplayName("Like user Dto Test:Negative")
	void likeUserDto_Negative() {
		likeUserRequestDto.setUserId(null);
		Set<ConstraintViolation<LikeUserRequestDto>> violations = validator.validate(likeUserRequestDto);

		violations.forEach(violation -> {
			assertEquals(USERID_NOTNULL_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("Like user Dto Test:Negative")
	void likeUserDto_Negative2() {
		likeUserRequestDto.setLikeUserId(null);
		Set<ConstraintViolation<LikeUserRequestDto>> violations = validator.validate(likeUserRequestDto);

		violations.forEach(violation -> {
			assertEquals(LIKEUSERID_NOTNULL_MESSAGE, violation.getMessage());
		});

	}

	@Test
	@DisplayName("All Field Test for LikeUserDto:Positive")
	void allFieldTestForLikeUserDto_Positive() {

		Set<ConstraintViolation<LikeUserRequestDto>> violations = validator.validate(likeUserRequestDto);

		assertTrue(violations.isEmpty());

	}

	@Test
	@DisplayName("Accept Favorite RequestDto Test:Negative")
	void acceptFavoriteRequestValidation_Negative() {
		acceptOrDeclineRequestDto.setUserId(null);
		Set<ConstraintViolation<AcceptOrDeclineRequestDto>> violations = validator.validate(acceptOrDeclineRequestDto);

		violations.forEach(violation -> {
			assertEquals(USERID_NOTNULL_MESSAGE, violation.getMessage());			

		});

	}
	@Test
	@DisplayName("Accept Favorite RequestDto Test:Negative")
	void acceptFavoriteRequestIdValidation_Negative() {
		acceptOrDeclineRequestDto.setAcceptFavoriteId(null);
		Set<ConstraintViolation<AcceptOrDeclineRequestDto>> violations = validator.validate(acceptOrDeclineRequestDto);

		violations.forEach(violation -> {
			assertEquals(ACCEPT_FAVORITE_USERID_NOTNULL_MESSAGE, violation.getMessage());			

		});

	}

	@Test
	@DisplayName("Accept Favorite RequestDto Test:Positive")
	void acceptFavoriteRequestValidation_Positive() {

		Set<ConstraintViolation<AcceptOrDeclineRequestDto>> violations = validator.validate(acceptOrDeclineRequestDto);

		assertTrue(violations.isEmpty());

	}

}
