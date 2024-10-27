package org.bohdanzhuvak.nicoai.features.images.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "images_data")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageData implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String type;
  private String path;
}
