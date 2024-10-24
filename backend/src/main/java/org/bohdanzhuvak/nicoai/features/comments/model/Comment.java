package org.bohdanzhuvak.nicoai.features.comments.model;

import jakarta.persistence.*;
import lombok.*;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id", referencedColumnName = "id")
  private Image image;

  private String body;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant createdAt;
}
