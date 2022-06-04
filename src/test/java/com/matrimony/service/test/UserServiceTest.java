package com.matrimony.service.test;

import static com.matrimony.dto.Constants.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import com.matrimony.dto.AcceptOrDeclineRequestDto;
import com.matrimony.dto.LikeUserRequestDto;
import com.matrimony.dto.UpdateRequestDto;
import com.matrimony.dto.UpdateResponseDto;
import com.matrimony.dto.UserLoginRequestDto;
import com.matrimony.dto.UserLoginResponseDto;
import com.matrimony.entity.Profile;
import com.matrimony.entity.User;
import com.matrimony.exception.ArgumentNotValidException;
import com.matrimony.exception.IdNotFoundException;
import com.matrimony.exception.WrongCredentialsException;
import com.matrimony.repo.ProfileRepository;
import com.matrimony.repo.UserRepository;
import com.matrimony.service.impl.UserServiceImpl;

@SpringBootTest
class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@Mock
	ProfileRepository profileRepository;

	@InjectMocks
	UserServiceImpl userServiceImpl;

	LikeUserRequestDto likeUserRequestDto;

	UserLoginRequestDto loginRequestDto;

	UserLoginResponseDto loginResponseDto;

	List<UserLoginResponseDto> loginResponseDtoList;

	AcceptOrDeclineRequestDto acceptOrDeclineRequestDto;

	UpdateRequestDto updateRequestDto;

	UpdateResponseDto updateResponseDto;

	User user;

	Profile userProfile;

	User likedUser;

	User testForLiked;

	Profile likedUserProfile;

	List<User> likedList;

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
		acceptOrDeclineRequestDto.setUserId(1);
		acceptOrDeclineRequestDto.setAcceptFavoriteId(2);

		updateRequestDto = new UpdateRequestDto();
		updateRequestDto.setAge(22);
		updateRequestDto.setHeight(6);
		updateRequestDto.setLocation("Chennai");
		updateRequestDto.setUserId(1);
		updateRequestDto.setWeight(50);

		updateResponseDto = new UpdateResponseDto();
		BeanUtils.copyProperties(loginResponseDto, updateResponseDto);

		user = new User();
		user.setUserId(1);
		user.setUserName("ranjithkumardr");
		user.setPassword("ranjith@123");
		user.setFirstName("Ranjith");
		user.setLastName("Kumar");

		userProfile = new Profile();
		userProfile.setProfileId(1);
		userProfile.setAge(26);
		userProfile.setHeight(5);
		userProfile.setWeight(56);
		userProfile.setLocation("Banglore");
		userProfile.setMotherTongue("Telugu");

		user.setProfile(userProfile);
		userProfile.setUser(user);

		likedUser = new User();
		likedUser.setUserId(2);
		likedUser.setUserName("sagardeepdr");
		likedUser.setPassword("sagar@123");
		likedUser.setFirstName("Sagar");
		likedUser.setLastName("Deep");

		likedUserProfile = new Profile();
		userProfile.setProfileId(2);
		userProfile.setAge(26);
		userProfile.setHeight(5);
		userProfile.setLocation("Banglore");
		userProfile.setMotherTongue("Telugu");

		likedUser.setProfile(likedUserProfile);
		likedUserProfile.setUser(likedUser);

		likedList = new ArrayList<>();

		testForLiked = new User();
		testForLiked.setUserId(4);

	}

	@Test
	@DisplayName("LikeUser:Positive")
	void likeUser_Positive() {
		// context
		user.setLikedUserList(likedList);

		when(userRepository.findById(likeUserRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(likeUserRequestDto.getLikeUserId())).thenReturn(Optional.of(likedUser));

		// event
		String result = userServiceImpl.likeUser(likeUserRequestDto);

		// outcome
		assertEquals("you Liked the User " + likeUserRequestDto.getLikeUserId() + " Successfully", result);

	}

	@Test
	@DisplayName("LikeUser if Already Some user Exist:Positive")
	void likeUserIfAlreadySomeUserExist_Positive() {
		// context
		likedList.add(testForLiked);
		user.setLikedUserList(likedList);

		when(userRepository.findById(likeUserRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(likeUserRequestDto.getLikeUserId())).thenReturn(Optional.of(likedUser));

		// event
		String result = userServiceImpl.likeUser(likeUserRequestDto);

		// outcome
		assertEquals("you Liked the User " + likeUserRequestDto.getLikeUserId() + " Successfully", result);

	}

	@Test
	@DisplayName("LikeUser if Already Exist in LikeList:Positive")
	void likeUserIfAlreadyExist_Positive() {
		// context
		likedList.add(likedUser);
		user.setLikedUserList(likedList);

		when(userRepository.findById(likeUserRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(likeUserRequestDto.getLikeUserId())).thenReturn(Optional.of(likedUser));

		// event
		ArgumentNotValidException e = assertThrows(ArgumentNotValidException.class, () -> {
			userServiceImpl.likeUser(likeUserRequestDto);
		});
		// outcome
		assertEquals(USER_EXIST_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("LikeUser:Negative")
	void likeUserforEmptyUser_Negative() {
		// context

		when(userRepository.findById(likeUserRequestDto.getUserId())).thenReturn(Optional.empty());
		when(userRepository.findById(likeUserRequestDto.getLikeUserId())).thenReturn(Optional.empty());

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.likeUser(likeUserRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("LikeUser:Negative")
	void likeUserforEmptyUserYouWantToLike_Negative() {
		// context

		when(userRepository.findById(likeUserRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(likeUserRequestDto.getLikeUserId())).thenReturn(Optional.empty());

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.likeUser(likeUserRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("LikeUser:Negative")
	void likeUserforSameUserId_Negative() {
		// context

		user.setUserId(1);
		likedUser.setUserId(1);
		when(userRepository.findById(likeUserRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(likeUserRequestDto.getLikeUserId())).thenReturn(Optional.of(likedUser));

		// event
		ArgumentNotValidException e = assertThrows(ArgumentNotValidException.class, () -> {
			userServiceImpl.likeUser(likeUserRequestDto);
		});

		// outcome
		assertEquals(NOT_VALID_ARGUMENT, e.getMessage());

	}

	@Test
	@DisplayName("User Login:Positive")
	void userLogin_Positive() {
		// context

		likedList.add(user);
		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);
		when(userRepository.findByUserName(loginRequestDto.getUserName())).thenReturn(user);
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);
		when(profileRepository.findByUser(likedUser)).thenReturn(likedUserProfile);

		// event
		List<UserLoginResponseDto> result = userServiceImpl.userLogin(loginRequestDto);

		// outcome
		assertEquals(1, result.size());

	}

	@Test
	@DisplayName("User Login:Negative")
	void userLoginPassword_Negative() {
		// context

		user.setPassword("wrongPassword");
		when(userRepository.findByUserName(loginRequestDto.getUserName())).thenReturn(user);

		// event
		WrongCredentialsException e = assertThrows(WrongCredentialsException.class, () -> {
			userServiceImpl.userLogin(loginRequestDto);
		});

		// outcome
		assertEquals(WRONG_CREDENTIAL_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("User Login:Negative")
	void userLoginUsername_Negative() {
		// context

		user.setUserName("wrongusername");
		;
		when(userRepository.findByUserName(loginRequestDto.getUserName())).thenReturn(user);

		// event
		WrongCredentialsException e = assertThrows(WrongCredentialsException.class, () -> {
			userServiceImpl.userLogin(loginRequestDto);
		});

		// outcome
		assertEquals(WRONG_CREDENTIAL_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Accept Favorite Request:Negative")
	void acceptFavoriteRequest_Negative() {
		// context

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.empty());
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.acceptFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Accept Favorite Request:Negative")
	void acceptFavoriteRequestforDifferentfavoriteId_Negative() {
		// context

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId())).thenReturn(Optional.empty());

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.acceptFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Accept Favorite Request If already Perfect Match Exist:Negative")
	void acceptFavoriteRequestIfAlreadyHavePerfectMatchId_Negative() {
		// context

		likedUser.setPerfectMatch(user);
		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));

		// event
		ArgumentNotValidException e = assertThrows(ArgumentNotValidException.class, () -> {
			userServiceImpl.acceptFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(PERFECT_MATCH_EXIST_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Accept Favorite Request If already Perfect Match Exist:Negative")
	void acceptFavoriteRequestIfAlreadyHavePerfectMatchId2_Negative() {
		// context

		user.setPerfectMatch(likedUser);
		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));

		// event
		ArgumentNotValidException e = assertThrows(ArgumentNotValidException.class, () -> {
			userServiceImpl.acceptFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(PERFECT_MATCH_EXIST_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Accept Favorite Request:Positive")
	void acceptFavoriteRequest_Positive() {
		// context

		likedList.add(user);
		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);

		List<User> newLikedListforUser = new ArrayList<>();
		newLikedListforUser.add(likedUser);
		user.setLikedUserList(newLikedListforUser);

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);

		// event
		String result = userServiceImpl.acceptFavoriteRequest(acceptOrDeclineRequestDto);

		// outcome
		assertEquals("user id " + acceptOrDeclineRequestDto.getUserId() + " got a perfect Match with "
				+ acceptOrDeclineRequestDto.getAcceptFavoriteId() + " and vice versa", result);

	}

	@Test
	@DisplayName("Decline Favorite Request:Positive")
	void declineFavoriteRequest_Positive() {
		// context

		likedList.add(user);
		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);

		List<User> newLikedListforUser = new ArrayList<>();
		newLikedListforUser.add(likedUser);
		user.setLikedUserList(newLikedListforUser);

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);

		// event
		String result = userServiceImpl.declineFavoriteRequest(acceptOrDeclineRequestDto);

		// outcome
		assertEquals(DECLINE_USER_MESSAGE, result);

	}

	@Test
	@DisplayName("Decline Favorite Request:Negative")
	void declineFavoriteRequest_Negative() {
		// context

		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);

		List<User> newLikedListforUser = new ArrayList<>();
		newLikedListforUser.add(likedUser);
		user.setLikedUserList(newLikedListforUser);

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);

		// event
		ArgumentNotValidException e = assertThrows(ArgumentNotValidException.class, () -> {
			userServiceImpl.declineFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(FAVORITE_ID_NOT_FOUND_MESSAGE, e.getMessage());
	}
	
	@Test
	@DisplayName("Decline Favorite Request for Empty User:Negative")
	void declineFavoriteRequestForEmptyUser_Negative() {
		// context

		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);

		List<User> newLikedListforUser = new ArrayList<>();
		newLikedListforUser.add(likedUser);
		user.setLikedUserList(newLikedListforUser);

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.empty());
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.declineFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());
	}
	
	@Test
	@DisplayName("Decline Favorite Request for Empty Liked User:Negative")
	void declineFavoriteRequestForLikedUser_Negative() {
		// context

		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);

		List<User> newLikedListforUser = new ArrayList<>();
		newLikedListforUser.add(likedUser);
		user.setLikedUserList(newLikedListforUser);

		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.empty());
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.declineFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());
	}

	@Test
	@DisplayName("Accept Favorite Request:Positive")
	void acceptFavoriteRequestifNotInFavoriteList_Positive() {
		// context

		likedList.add(user);
		likedUser.setLikedUserList(likedList);

		List<User> newLikedList = new ArrayList<>();
		newLikedList.add(likedUser);
		acceptOrDeclineRequestDto.setAcceptFavoriteId(3);
		when(userRepository.findById(acceptOrDeclineRequestDto.getUserId())).thenReturn(Optional.of(user));
		when(userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()))
				.thenReturn(Optional.of(likedUser));
		when(userRepository.findByLikedUserList(user)).thenReturn(newLikedList);

		// event
		ArgumentNotValidException e = assertThrows(ArgumentNotValidException.class, () -> {
			userServiceImpl.acceptFavoriteRequest(acceptOrDeclineRequestDto);
		});

		// outcome
		assertEquals(FAVORITE_ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

	@Test
	@DisplayName("Update User Profile:Positive")
	void updateUserProfile_Positive() {
		// context

		when(userRepository.findById(updateRequestDto.getUserId())).thenReturn(Optional.of(user));

		// event
		UpdateResponseDto result = userServiceImpl.updateUserProfile(updateRequestDto);

		// outcome
		assertEquals(1, result.getUserId());
		assertEquals(50, result.getWeight());
		assertEquals("Chennai", result.getLocation());

	}

	@Test
	@DisplayName("Update User Profile:Negative")
	void updateUserProfile_Negative() {
		// context

		when(userRepository.findById(updateRequestDto.getUserId())).thenReturn(Optional.empty());

		// event
		IdNotFoundException e = assertThrows(IdNotFoundException.class, () -> {
			userServiceImpl.updateUserProfile(updateRequestDto);
		});

		// outcome
		assertEquals(ID_NOT_FOUND_MESSAGE, e.getMessage());

	}

}
