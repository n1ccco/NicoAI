package org.bohdanzhuvak.nicoai.features.images.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bohdanzhuvak.nicoai.features.users.model.User;

@Entity
@Table(name = "user_likes")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Like {

  @EmbeddedId
  private LikeId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("imageId")
  @JoinColumn(name = "image_id")
  private Image image;

}