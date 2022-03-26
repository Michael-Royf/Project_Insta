package com.example.web;

import com.example.dto.CommentDto;
import com.example.entity.CommentEntity;
import com.example.exceptions.GlobalExceptionHandler;
import com.example.facade.CommentFacade;
import com.example.payload.response.MessageResponse;
import com.example.service.CommentServiceImpl;
import com.example.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController extends GlobalExceptionHandler {
    @Autowired
    private CommentServiceImpl commentService;
    @Autowired
    private CommentFacade commentFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;


    @PostMapping("/{postId}/create")
       public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable("postId") String postId, BindingResult bindingResult,
                                                Principal principal) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        CommentEntity comment = commentService.saveComment(Long.parseLong(postId), commentDto, principal);
        CommentDto createdComment = commentFacade.commentToCommentDTO(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/all")
     public ResponseEntity<List<CommentDto>> getALlCommentToPost(@PathVariable("postId") String postId) {
        List<CommentDto> commentDtoList = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDTO)
                .collect(Collectors.toList());
        return  new ResponseEntity<>(commentDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}/delete")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId){
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Post was deleted"), HttpStatus.OK);
    }



}


