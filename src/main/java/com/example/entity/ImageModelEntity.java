package com.example.entity;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import javax.persistence.*;

@Entity
@Data
@Table(name = "Image_Model")
public class ImageModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;
    @JsonIgnore// нe передают данные для клиента
    private Long userId;
    @JsonIgnore
    private Long postId;


}
