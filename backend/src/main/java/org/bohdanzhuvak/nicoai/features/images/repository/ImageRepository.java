package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
  @EntityGraph(attributePaths = {"imageData", "promptData", "author"})
  @Cacheable(value = "image_all_data", key = "#id", unless = "#result == null")
  @Query("SELECT i FROM Image i WHERE i.id = :id")
  Optional<Image> findByIdWithAllData(@Param("id") Long id);

  @EntityGraph(attributePaths = {"author", "imageData"})
  @Cacheable(value = "image_partial_data", key = "#id")
  @Query("SELECT i FROM Image i WHERE i.id = :id")
  Optional<Image> findByIdWithPartData(@Param("id") Long id);

  List<Image> findByAuthorId(Long authorId);

  List<Image> findByVisibility(Visibility visibility, Sort sort);

  List<Image> findByAuthorIdAndVisibility(Long authorId, Visibility visibility);
}
