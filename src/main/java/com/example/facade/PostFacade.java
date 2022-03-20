package com.example.facade;

import com.example.dto.PostDto;
import com.example.entity.PostEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {
    @Autowired
    private ModelMapper mapper;

    public PostDto postToPostDto(PostEntity post){
//        PostDto postDto = mapper.map(post, PostDto.class);
//        return postDto;
        PostDto postDto = new PostDto();
        postDto.setUsername(post.getUser().getUsername());
        postDto.setId(post.getId());
        postDto.setCaption(post.getCaption());
        postDto.setLikes(post.getLikes());
        postDto.setUsersLiked(post.getUsersLiked());
        postDto.setLocation(post.getLocation());
        postDto.setTitle(post.getTitle());
        return postDto;
    }

    public PostEntity postDtoToPostEntity(PostDto postDto){
        PostEntity post = mapper.map(postDto, PostEntity.class);
        return post;
    }
}
