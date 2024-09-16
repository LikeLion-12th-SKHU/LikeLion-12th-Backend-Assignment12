package com.likelion.oauth2test.post.domain.repository;

import com.likelion.oauth2test.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 사용자 ID로 게시글 조회
    Optional<List<Post>> findByUserId(Long userId);

    // 다른 사용자 정의 메서드를 추가할 수 있습니다.
}
