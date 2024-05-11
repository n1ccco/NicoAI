package org.bohdanzhuvak.nicoai.model;

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
    private String prompt;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private ImageData imageData;
    public Image(String prompt, ImageData imageData, User author) {
        this.prompt = prompt;
        this.imageData = imageData;
        this.author = author;
    }
}
