package com.example.service.interf;

import com.example.dto.CommentDto;
import com.example.entity.CommentEntity;

import java.security.Principal;
import java.util.List;

public interface CommentService {
    CommentEntity saveComment(Long postId, CommentDto commentDto, Principal principal);

    List<CommentEntity> getAllCommentsForPost(Long postId);

    void deleteComment(Long commentID);

}




