package com.example.service;

import com.example.dto.CommentDto;
import com.example.entity.CommentEntity;
import com.example.entity.PostEntity;
import com.example.entity.UserEntity;
import com.example.exceptions.domain.PostNotFoundException;
import com.example.repository.CommentRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import com.example.service.interf.CommentService;
import com.example.utility.GetUserByPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.example.constant.UserImplConstant.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final GetUserByPrincipal getUserByPrincipal;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, GetUserByPrincipal getUserByPrincipal) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.getUserByPrincipal = getUserByPrincipal;
    }

    @Override
    public CommentEntity saveComment(Long postId, CommentDto commentDto, Principal principal) throws PostNotFoundException {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(NO_POST_FOUND_BY_USERNAME+ user.getUsername()));
        CommentEntity comment = new CommentEntity();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDto.getMessage());
        LOG.info("Saving comment for post: {}", post.getId());
        return commentRepository.save(comment);
    }

    @Override
    public List<CommentEntity> getAllCommentsForPost(Long postId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(NO_POST_FOUND_BY_ID + postId));
        List<CommentEntity> comments = commentRepository.findAllByPost(post);
        return comments;
    }

    @Override
    public void deleteComment(Long commentID) {
        Optional<CommentEntity> comment = commentRepository.findById(commentID);
        comment.ifPresent(commentRepository::delete);
    }
}
