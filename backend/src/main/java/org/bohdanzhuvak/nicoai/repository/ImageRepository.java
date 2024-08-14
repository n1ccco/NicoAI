package org.bohdanzhuvak.nicoai.repository;

import java.util.List;

import org.bohdanzhuvak.nicoai.model.Image;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByAuthorId(Long authorId);

  List<Image> findByIsPublic(boolean isPublic, Sort sort);

  List<Image> findByAuthorIdAndIsPublic(Long authorId, boolean isPublic);

  @Query("SELECT i FROM Image i WHERE i.isPublic = true ORDER BY SIZE(i.likes) ASC, i.id DESC")
  List<Image> findByIsPublicOrderByLikesSizeAsc();

  @Query("SELECT i FROM Image i WHERE i.isPublic = true ORDER BY SIZE(i.likes) DESC, i.id DESC")
  List<Image> findByIsPublicOrderByLikesSizeDesc();
}
