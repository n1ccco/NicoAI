package org.bohdanzhuvak.nicoai.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images_data")
@Getter
@NoArgsConstructor
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String path;

    public ImageData(String name, String type, String path) {
        this.name = name;
        this.type = type;
        this.path = path;
    }
}
