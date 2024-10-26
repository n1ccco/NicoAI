package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByAuthorId(Long authorId);

  List<Image> findByVisibility(Visibility visibility, Sort sort);

  List<Image> findByAuthorIdAndVisibility(Long authorId, Visibility visibility);
}
