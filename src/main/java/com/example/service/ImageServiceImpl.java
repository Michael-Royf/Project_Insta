package com.example.service;

import com.example.entity.ImageModelEntity;
import com.example.entity.PostEntity;
import com.example.entity.UserEntity;
import com.example.exceptions.domain.ImageNotFoundException;
import com.example.repository.ImageRepository;
import com.example.repository.PostRepository;
import com.example.repository.UserRepository;
import com.example.service.interf.ImageService;
import com.example.utility.GetUserByPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final  ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GetUserByPrincipal getUserByPrincipal;


    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, UserRepository userRepository, PostRepository postRepository, GetUserByPrincipal getUserByPrincipal) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.getUserByPrincipal = getUserByPrincipal;
    }

    @Override
    public ImageModelEntity uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        log.info("Uploading image profile to user {}", user.getUsername());

        ImageModelEntity userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        ImageModelEntity imageModel = new ImageModelEntity();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }

    @Override
    public ImageModelEntity uploadImageToPost(MultipartFile file, Principal principal, Long postId) throws IOException {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);
        PostEntity post = user.getPosts()
                .stream()
                // .filter(p -> p.getId().equals(postId))
                .filter(post1 -> post1.getId() == postId)
                .collect(toSinglePostCollector());

        ImageModelEntity imageModel = new ImageModelEntity();
        imageModel.setPostId(post.getId());
        imageModel.setImageBytes(file.getBytes());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        log.info("Uploading image to Post {}", post.getId());
        return imageRepository.save(imageModel);
    }

    @Override
    public ImageModelEntity getImageToUser(Principal principal) {
        UserEntity user = getUserByPrincipal.getUserByPrincipal(principal);

        ImageModelEntity imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    @Override
    public ImageModelEntity getImageToPost(Long postId) {
        ImageModelEntity imageModel = imageRepository.findByPostId(postId)
                .orElseThrow(() -> new ImageNotFoundException("Cannot find image to Post: " + postId));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    private <T> Collector<T, ?, T> toSinglePostCollector() {//помогает вернуть единственный пост для юзера
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (DataFormatException | IOException e) {
            log.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }
}
