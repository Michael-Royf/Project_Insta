package com.example.service.interf;

import com.example.dto.CommentDto;
import com.example.entity.CommentEntity;
import com.example.exceptions.domain.PostNotFoundException;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    CommentEntity saveComment(Long postId, CommentDto commentDto, Principal principal) throws PostNotFoundException;

    List<CommentEntity> getAllCommentsForPost(Long postId) throws PostNotFoundException;

    void deleteComment(Long commentID);

}




