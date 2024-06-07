package org.bohdanzhuvak.nicoai.model;

import java.util.HashSet;
import java.util.Set;

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
  @Builder.Default
  @JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "image_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> likes = new HashSet<User>();

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private PromptData promptData;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private ImageData imageData;
}
