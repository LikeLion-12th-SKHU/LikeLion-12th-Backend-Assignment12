package com.likelion.oauth2test.feed.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.likelion.oauth2test.feed.domain.Feed;
import com.likelion.oauth2test.user.domain.User;

public interface FeedRepository extends JpaRepository<Feed,Long> {
	List<Feed> findAllByUser(User user);
}
