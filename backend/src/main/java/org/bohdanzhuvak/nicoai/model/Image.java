package org.bohdanzhuvak.nicoai.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn
  private User author;
  @Builder.Default
  private boolean isPublic = false;
  @ManyToMany
  @JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "image_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private List<User> likes;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private PromptData promptData;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private ImageData imageData;
}
