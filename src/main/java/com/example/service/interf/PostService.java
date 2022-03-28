package com.example.service.interf;

import com.example.dto.PostDto;
import com.example.entity.PostEntity;
import com.example.exceptions.domain.PostNotFoundException;

import java.security.Principal;
import java.util.List;

public interface PostService {
    PostEntity createPost(PostDto postDto, Principal principal);

    List<PostEntity> getAllPosts();

    PostEntity getPostById(Long postId, Principal principal) throws PostNotFoundException;

    List<PostEntity> getAllPostForUser(Principal principal);

    PostEntity likePost(Long postId, String username) throws PostNotFoundException;

    void deletePost(Long postId, Principal principal) throws PostNotFoundException;

}
