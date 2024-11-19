package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
  @EntityGraph(attributePaths = {"author", "promptData"})
  @Cacheable(value = "image_with_prompt_data", key = "#id", unless = "#result == null")
  @Query("SELECT i FROM Image i WHERE i.id = :id")
  Optional<Image> findByIdWithAllData(@Param("id") Long id);

  @EntityGraph(attributePaths = {"author", "imageData"})
  @Cacheable(value = "image_with_blob_data", key = "#id")
  @Query("SELECT i FROM Image i WHERE i.id = :id")
  Optional<Image> findByIdWithPartData(@Param("id") Long id);

  Page<Image> findByAuthorId(Long authorId, Pageable pageable);

  Page<Image> findByVisibility(Visibility visibility, Pageable pageable);

  Page<Image> findByAuthorIdAndVisibility(Long authorId, Visibility visibility, Pageable pageable);
}
