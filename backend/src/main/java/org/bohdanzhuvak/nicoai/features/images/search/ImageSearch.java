package org.bohdanzhuvak.nicoai.features.images.search;

import lombok.*;
import org.bohdanzhuvak.nicoai.features.images.model.BaseImage;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Collections;
import java.util.Set;

@Document(indexName = "images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageSearch implements BaseImage {
  @Id
  private Long id;

  private Long likeCount;
  @Field(type = FieldType.Text, analyzer = "standard")
  private String promptDescription;
  @Override
  public User getAuthor() {
    return null;
  }

  @Override
  public Visibility getVisibility() {
    return null;
  }

  @Override
  public Set<User> getLikes() {
    return Collections.emptySet();
  }
}