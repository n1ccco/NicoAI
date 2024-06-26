package org.bohdanzhuvak.nicoai.repository;

import java.util.List;

import org.bohdanzhuvak.nicoai.model.Image;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByAuthorId(Long authorId);

  List<Image> findByIsPublic(boolean isPublic, Sort sort);

  List<Image> findByAuthorIdAndIsPublic(Long authorId, boolean isPublic);
}
