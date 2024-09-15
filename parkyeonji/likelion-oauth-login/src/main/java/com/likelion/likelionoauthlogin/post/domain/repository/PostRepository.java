package com.likelion.likelionoauthlogin.post.domain.repository;

import com.likelion.likelionoauthlogin.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
