package com.matrimony.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "profileId")
@Data
public class Profile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer profileId;

	private LocalDateTime createdAt;

	private Integer age;

	private Integer height;

	private String religion;

	private String location;

	private Integer weight;

	private String motherTongue;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "userId", nullable = false)
	@JsonIgnoreProperties("profile")
	@JsonIgnore
	private User user;


}
