package com.example.web;

import com.example.dto.PostDto;
import com.example.entity.PostEntity;
import com.example.exceptions.GlobalExceptionHandler;
import com.example.facade.PostFacade;
import com.example.payload.response.MessageResponse;
import com.example.service.PostServiceImpl;
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
@RequestMapping("api/post")
@CrossOrigin
public class PostController extends GlobalExceptionHandler {
    @Autowired
    private PostFacade postFacade;
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;


    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto,
                                             BindingResult bindingResult, Principal principal) {

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;

        PostEntity post = postService.createPost(postDto, principal);
        PostDto createdPost = postFacade.postToPostDto(post);
        return new ResponseEntity<>(createdPost, HttpStatus.OK);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PostDto>>  getAllPost(){
        List<PostDto > postDtoList = postService.getAllPosts()
                .stream()
                .map(postFacade::postToPostDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }
    @GetMapping("user/posts")
    public ResponseEntity<List<PostDto>> getAllPostForUser(Principal principal){
        List<PostDto> postDtoList = postService.getAllPostForUser(principal)
                .stream()
                .map(postFacade::postToPostDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    @PostMapping("/{postId}/{username}/like")
    public ResponseEntity<PostDto> likePost(@PathVariable ("postId") String postId,
                                            @PathVariable("username") String username){
        PostEntity post = postService.likePost(Long.parseLong(postId), username);
        PostDto postDto  = postFacade.postToPostDto(post);
        return  new ResponseEntity<>(postDto, HttpStatus.OK);
    }
    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<MessageResponse> deletePost(@PathVariable("postId") String postId, Principal principal){
        postService.deletePost(Long.parseLong(postId), principal);
        return  new ResponseEntity<>(new MessageResponse("Post wes deleted"), HttpStatus.OK);
    }




}
