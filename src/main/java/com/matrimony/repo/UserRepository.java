package com.matrimony.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.entity.Profile;
import com.matrimony.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer>{

	User findByUserName(String userName);

	List<User> findByLikedUserList(User user);
	
	User  findByProfile(Profile profile);

}
