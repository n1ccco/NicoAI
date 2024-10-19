package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
  List<Image> findByAuthorId(Long authorId);

  List<Image> findByVisibility(Visibility visibility, Sort sort);

  List<Image> findByAuthorIdAndVisibility(Long authorId, Visibility visibility);

  @Query("SELECT i FROM Image i WHERE i.visibility = 'PUBLIC' ORDER BY SIZE(i.likes) ASC, i.id DESC")
  List<Image> findByIsPublicOrderByLikesSizeAsc();

  @Query("SELECT i FROM Image i WHERE i.visibility = 'PUBLIC' ORDER BY SIZE(i.likes) DESC, i.id DESC")
  List<Image> findByIsPublicOrderByLikesSizeDesc();
}
