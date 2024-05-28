package org.bohdanzhuvak.nicoai.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue
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

}
