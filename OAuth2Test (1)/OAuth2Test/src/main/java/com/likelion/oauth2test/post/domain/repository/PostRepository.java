package com.likelion.oauth2test.post.domain.repository;

import com.likelion.oauth2test.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findById(Post post);
    Optional<List<Post>> findByUserId(Long userId);
}
