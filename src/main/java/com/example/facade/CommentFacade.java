package com.example.facade;

import com.example.dto.CommentDto;
import com.example.entity.CommentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {
    @Autowired
    private ModelMapper mapper;

    public CommentDto commentToCommentDTO(CommentEntity comment) {
        CommentDto commentDto = mapper.map(comment, CommentDto.class);
        return commentDto;
    }

    public CommentEntity commentDtoToCommentEntity(CommentDto commentDto){
        CommentEntity comment = mapper.map(commentDto, CommentEntity.class);
        return comment;
    }
}
