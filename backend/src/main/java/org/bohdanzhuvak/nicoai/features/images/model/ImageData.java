package org.bohdanzhuvak.nicoai.features.images.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images_data")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String type;
  private String path;
}
