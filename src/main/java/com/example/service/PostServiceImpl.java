package com.example.service;

import com.example.dto.PostDto;
import com.example.entity.ImageModelEntity;
import com.example.entity.PostEntity;
import com.example.entity.UserEntity;
import com.example.exceptions.domain.PostNotFoundException;
import com.example.repository.ImageRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import com.example.service.interf.PostService;
import com.example.utility.GetUserByPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final GetUserByPrincipal getUserByPrincipal;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository, GetUserByPrincipal getUserByPrincipal) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.getUserByPrincipal = getUserByPrincipal;
    }

    @Override
    public PostEntity createPost(PostDto postDto, Principal principal) {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        PostEntity post = new PostEntity();
        post.setUser(user);
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        post.setTitle(postDto.getTitle());
        post.setLikes(0);
        log.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }
    @Override
    public List<PostEntity> getAllPosts() {
        return postRepository.findAllByOrderByCreateDateDesc();
    }

    @Override
    public PostEntity getPostById(Long postId, Principal principal) {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
    }

    @Override
    public List<PostEntity> getAllPostForUser(Principal principal) {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        return postRepository.findAllByOrderByCreateDateDesc();
    }

    @Override
    public PostEntity likePost(Long postId, String username) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        Optional<String> userLiked = post.getUsersLiked()
                .stream()
                .filter(u -> u.equals(username)).findAny();
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getUsersLiked().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getUsersLiked().add(username);
        }
        return postRepository.save(post);
    }

    @Override
    public void deletePost(Long postId, Principal principal) {
        PostEntity post = getPostById(postId, principal);
        Optional<ImageModelEntity> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }
}
