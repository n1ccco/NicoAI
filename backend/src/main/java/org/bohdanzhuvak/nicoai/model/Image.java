package org.bohdanzhuvak.nicoai.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private User author;
    private boolean isPublic;
    @ManyToMany
    @JoinTable(
        name = "user_likes",
        joinColumns = @JoinColumn(name = "image_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likes;

    private String prompt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private ImageData imageData;
    public Image(String prompt, ImageData imageData, User author) {
        this.prompt = prompt;
        this.imageData = imageData;
        this.author = author;
        this.isPublic = false;
        this.likes = new ArrayList<User>();
    }
}
