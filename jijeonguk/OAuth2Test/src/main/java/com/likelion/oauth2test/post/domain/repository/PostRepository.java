package com.likelion.oauth2test.post.domain.repository;

import com.likelion.oauth2test.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
