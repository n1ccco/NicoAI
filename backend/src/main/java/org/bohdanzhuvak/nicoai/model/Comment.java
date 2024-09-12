package org.bohdanzhuvak.nicoai.model;

import jakarta.persistence.*;
import lombok.*;
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
  @JoinColumn(name = "authorId", referencedColumnName = "id")
  private User author;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "imageId", referencedColumnName = "id")
  private Image image;

  private String body;

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Instant createdAt;
}
