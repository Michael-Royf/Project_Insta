package com.example.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
@Table(name = "post")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String caption;
    private String location;
    private Integer likes;


    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> usersLiked = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createDate;

    @PrePersist // создается до создания этого обьекта
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }


}
