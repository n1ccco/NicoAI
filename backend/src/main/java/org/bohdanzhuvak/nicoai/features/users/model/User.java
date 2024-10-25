package org.bohdanzhuvak.nicoai.features.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.bohdanzhuvak.nicoai.features.images.model.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long Id;
  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "author")
  private List<Image> images;
  private String username;

  @JsonIgnore
  private String password;

  @ManyToMany(mappedBy = "likes")
  private List<Image> likes;

  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roles = new ArrayList<>();

  public boolean isAdmin() {
    return roles.contains("ROLE_ADMIN");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    User user = (User) o;
    return Id.equals(user.Id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Id);
  }
}
