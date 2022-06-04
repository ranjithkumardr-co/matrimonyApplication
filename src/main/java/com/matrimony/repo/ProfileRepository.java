package com.matrimony.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matrimony.entity.Profile;
import com.matrimony.entity.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {

	Profile findByUser(User user);
	
	List<Profile> findByAge(Integer age);
	
	List<Profile> findByLocation(String location);
	
	List<Profile> findByReligion(String religion);
	
	List<Profile> findByHeight(Integer height);
	
	
	

}



