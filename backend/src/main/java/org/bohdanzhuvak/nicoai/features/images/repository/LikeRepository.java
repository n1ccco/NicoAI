package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Like;
import org.bohdanzhuvak.nicoai.features.images.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
  boolean existsById(LikeId id);

  @Query(value = "SELECT l.image.id FROM Like l WHERE l.user.id = :userId AND l.image.id IN :imageIds")
  Set<Long> findImageIdByUserIdAndImageIdIn(@Param("userId") Long userId, @Param("imageIds") Set<Long> imageIds);
}
