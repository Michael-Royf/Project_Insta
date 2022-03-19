package com.example.repository;

import com.example.entity.CommentEntity;
import com.example.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByPost(PostEntity post);
    CommentEntity findByIdAndUserId(Long commentId, Long userId);
}
