package com.example.service;

import com.example.dto.PostDto;
import com.example.entity.ImageModelEntity;
import com.example.entity.PostEntity;
import com.example.entity.UserEntity;
import com.example.exceptions.PostNotFoundException;
import com.example.repository.ImageRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public PostEntity createPost(PostDto postDto, Principal principal) {
        UserEntity user = getUserByPrincipal(principal);
        PostEntity post = new PostEntity();
        post.setUser(user);
        post.setCaption(postDto.getCaption());
        post.setLocation(postDto.getLocation());
        post.setTitle(postDto.getTitle());
        post.setLikes(0);

        LOG.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAllByOrderByCreateDateDesc();
    }

    public PostEntity getPostById(Long postId, Principal principal) {
        UserEntity user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for username: " + user.getEmail()));
    }


    public List<PostEntity> getAllPostForUser(Principal principal) {
        UserEntity user = getUserByPrincipal(principal);
        return postRepository.findAllByOrderByCreateDateDesc();
    }

    public PostEntity likePost(Long postId, String username) {
        PostEntity post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        Optional<String> userLiked = post.getLikesUsers()
                .stream()
                .filter(u -> u.equals(username)).findAny();
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikesUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikesUsers().add(username);
        }
        return postRepository.save(post);
    }

    public  void deletePost(Long postId, Principal principal){
        PostEntity post = getPostById(postId, principal);
        Optional<ImageModelEntity> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }




    private UserEntity getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
    }
}
