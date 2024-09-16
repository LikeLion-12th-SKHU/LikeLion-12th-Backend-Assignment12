package com.likelion.oauth2test.feed.domain;

import com.likelion.oauth2test.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "FEED_ID")
	private Long id;

	@Column(name = "FEED_TITLE")
	private String title;

	@Column(name = "FEED_IMAGE")
	private String image;

	@Column(name = "FEED_CONTENT")
	private String content;

	@ManyToOne
	private User user;

	@Builder
	private Feed(Long id, String title, String image, String content, User user) {
		this.id = id;
		this.title = title;
		this.image = image;
		this.content = content;
		this.user = user;
	}
}
