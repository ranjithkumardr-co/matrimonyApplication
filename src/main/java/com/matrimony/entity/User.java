package com.matrimony.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer userId;
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private Long phoneNo;
	private String emailId;
	

	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JsonIgnoreProperties("user")
	private Profile profile;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_liked", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "liked_user_id"))
	@JsonIgnore
	private List<User> likedUserList;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "user_match", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "perfect_match_id"))
	@JsonIgnore
	private User perfectMatch;

	



}
