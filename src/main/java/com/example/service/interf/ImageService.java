package com.example.service.interf;

import com.example.entity.ImageModelEntity;
import com.example.exceptions.domain.ImageNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface ImageService {
    ImageModelEntity uploadImageToUser(MultipartFile file, Principal principal) throws IOException;

    ImageModelEntity uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException;

    ImageModelEntity getImageToUser(Principal principal);

    ImageModelEntity getImageToPost(Long postId) throws ImageNotFoundException;


}
