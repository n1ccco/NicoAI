package org.bohdanzhuvak.nicoai.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private ImageData imageData;
    public Image(String description, ImageData imageData) {
        this.description = description;
        this.imageData = imageData;
    }
}
