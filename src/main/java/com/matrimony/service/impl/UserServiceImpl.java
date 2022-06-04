package com.matrimony.service.impl;

import static com.matrimony.dto.Constants.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matrimony.dto.AcceptOrDeclineRequestDto;
import com.matrimony.dto.LikeUserRequestDto;
import com.matrimony.dto.UpdateRequestDto;
import com.matrimony.dto.UpdateResponseDto;
import com.matrimony.dto.UserLoginRequestDto;
import com.matrimony.dto.UserLoginResponseDto;
import com.matrimony.entity.User;
import com.matrimony.exception.ArgumentNotValidException;
import com.matrimony.exception.IdNotFoundException;
import com.matrimony.exception.WrongCredentialsException;
import com.matrimony.repo.ProfileRepository;
import com.matrimony.repo.UserRepository;
import com.matrimony.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProfileRepository profileRepository;

	@Override
	public String likeUser(@Valid LikeUserRequestDto likeUserRequestDto) {

		Optional<User> optionalUser = userRepository.findById(likeUserRequestDto.getUserId());
		Optional<User> optionalLikeUser = userRepository.findById(likeUserRequestDto.getLikeUserId());
		if (optionalUser.isEmpty() || optionalLikeUser.isEmpty()) {

			throw new IdNotFoundException(ID_NOT_FOUND_MESSAGE);
		}
		if (optionalUser.get().getUserId().equals(optionalLikeUser.get().getUserId())) {
			throw new ArgumentNotValidException(NOT_VALID_ARGUMENT);
		}

		var user = optionalUser.get();
		var userWantTolike = optionalLikeUser.get();
		List<User> likedList = user.getLikedUserList();
		if (likedList.isEmpty()) {
			likedList.add(optionalLikeUser.get());
			user.setLikedUserList(likedList);
			userRepository.save(user);
			return LIKE_USER_MESSAGE + likeUserRequestDto.getLikeUserId() + SUCCESSFULLY;
		}
		return addToLikeForExistingList(user, likeUserRequestDto, likedList, userWantTolike);

	}

	@Override
	public List<UserLoginResponseDto> userLogin(@Valid UserLoginRequestDto userLoginRequestDto) {

		var user = userRepository.findByUserName(userLoginRequestDto.getUserName());
		List<UserLoginResponseDto> loginResponseDtos = new ArrayList<>();
		if (user.getUserName().equals(userLoginRequestDto.getUserName())
				&& user.getPassword().equals(userLoginRequestDto.getPassword())) {

			List<User> userList = userRepository.findByLikedUserList(user);
			userList.forEach(usern -> {
				var profile = profileRepository.findByUser(usern);
				var loginResponseDto = new UserLoginResponseDto();
				BeanUtils.copyProperties(usern, loginResponseDto);
				BeanUtils.copyProperties(profile, loginResponseDto);
				loginResponseDtos.add(loginResponseDto);
			});
			return loginResponseDtos;
		} else {
			throw new WrongCredentialsException(WRONG_CREDENTIAL_MESSAGE);
		}

	}

	@Override
	public String acceptFavoriteRequest(@Valid AcceptOrDeclineRequestDto acceptOrDeclineRequestDto) {

		Optional<User> optionalUser = userRepository.findById(acceptOrDeclineRequestDto.getUserId());
		Optional<User> userWhoLiked = userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId());
		if (optionalUser.isEmpty() || userWhoLiked.isEmpty()) {
			throw new IdNotFoundException(ID_NOT_FOUND_MESSAGE);
		}
		var user = optionalUser.get();
		var likedUser = userWhoLiked.get();
		if (user.getPerfectMatch() == null && likedUser.getPerfectMatch() == null) {
			List<User> usersLikedList = userRepository.findByLikedUserList(user);
			usersLikedList.forEach(userInLiked -> {
				if (userInLiked.getUserId().equals(acceptOrDeclineRequestDto.getAcceptFavoriteId())) {
					var acceptedUser = userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId()).get();
					user.setPerfectMatch(acceptedUser);
					user.getLikedUserList().remove(acceptedUser);
					userRepository.save(user);
					acceptedUser.setPerfectMatch(user);
					acceptedUser.getLikedUserList().remove(user);
					userRepository.save(acceptedUser);
				}
			});
			return generateMatchWithUsers(user, acceptOrDeclineRequestDto);
		}
		throw new ArgumentNotValidException(PERFECT_MATCH_EXIST_MESSAGE);
	}

	@Override
	public UpdateResponseDto updateUserProfile(@Valid UpdateRequestDto updateRequestDto) {

		Optional<User> optionalUser = userRepository.findById(updateRequestDto.getUserId());
		if (optionalUser.isPresent()) {
			var user = optionalUser.get();
			var profile = user.getProfile();
			BeanUtils.copyProperties(updateRequestDto, profile);
			user.setProfile(profile);
			userRepository.save(user);
			var updateResponseDto = new UpdateResponseDto();
			BeanUtils.copyProperties(user, updateResponseDto);
			BeanUtils.copyProperties(profile, updateResponseDto);
			return updateResponseDto;
		}
		throw new IdNotFoundException(ID_NOT_FOUND_MESSAGE);
	}

	private String generateMatchWithUsers(User user, AcceptOrDeclineRequestDto acceptOrDeclineRequestDto) {

		if (user.getPerfectMatch() != null) {
			return USER_ID + acceptOrDeclineRequestDto.getUserId() + GOT_A_PERFECT_MESSAGE
					+ acceptOrDeclineRequestDto.getAcceptFavoriteId() + AND_VICEVERSA;
		} else {
			throw new ArgumentNotValidException(FAVORITE_ID_NOT_FOUND_MESSAGE);
		}

	}

	private String addToLikeForExistingList(User user, LikeUserRequestDto likeUserRequestDto, List<User> likedList,
			User userWantTolike) {

		likedList.forEach(likedUser -> {
			if (likedUser.getUserId()
					.equals(userRepository.findById(likeUserRequestDto.getLikeUserId()).get().getUserId())) {
				throw new ArgumentNotValidException(USER_EXIST_MESSAGE);
			}

		});
		likedList.add(userWantTolike);
		user.setLikedUserList(likedList);
		userRepository.save(user);

		return LIKE_USER_MESSAGE + likeUserRequestDto.getLikeUserId() + SUCCESSFULLY;
	}

	@Override
	public String declineFavoriteRequest(@Valid AcceptOrDeclineRequestDto acceptOrDeclineRequestDto) {

		Optional<User> optionalUser = userRepository.findById(acceptOrDeclineRequestDto.getUserId());
		Optional<User> userWhoLiked = userRepository.findById(acceptOrDeclineRequestDto.getAcceptFavoriteId());
		if (optionalUser.isEmpty() || userWhoLiked.isEmpty()) {
			throw new IdNotFoundException(ID_NOT_FOUND_MESSAGE);
		}
		var user = userWhoLiked.get();
		var userWholiked = optionalUser.get();
		List<User> usersLikedList = user.getLikedUserList();
		boolean test = usersLikedList.remove(userWholiked);
		if (test) {
			user.setLikedUserList(usersLikedList);
			userRepository.save(user);
			return DECLINE_USER_MESSAGE;
		}
		throw new ArgumentNotValidException(FAVORITE_ID_NOT_FOUND_MESSAGE);

	}
}
