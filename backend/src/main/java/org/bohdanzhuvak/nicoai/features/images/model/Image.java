package org.bohdanzhuvak.nicoai.features.images.model;

import jakarta.persistence.*;
import lombok.*;
import org.bohdanzhuvak.nicoai.features.users.model.User;

import java.util.HashSet;
import java.util.Set;

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
  @Enumerated(EnumType.STRING)
  private Visibility visibility = Visibility.PRIVATE;
  @ManyToMany
  @Builder.Default
  @JoinTable(name = "user_likes", joinColumns = @JoinColumn(name = "image_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> likes = new HashSet<User>();
  @Column(name = "like_count")
  @Builder.Default
  private Long likeCount = 0L;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private PromptData promptData;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn
  private ImageData imageData;
}
