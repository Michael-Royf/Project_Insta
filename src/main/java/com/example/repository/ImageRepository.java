package com.example.repository;

import com.example.entity.ImageModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ImageRepository  extends JpaRepository<ImageModelEntity, Long> {
    Optional<ImageModelEntity> findByUserId(Long userId);
    Optional<ImageModelEntity> findByPostId(Long postId);
}
