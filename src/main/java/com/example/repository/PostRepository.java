package com.example.repository;

import com.example.entity.PostEntity;
import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllByUserOrderByCreateDateDesc(UserEntity user);

    List<PostEntity> findAllByOrderByCreateDateDesc();

    Optional<PostEntity> findPostByIdAndUser(Long id, UserEntity user);
}
