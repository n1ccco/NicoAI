package org.bohdanzhuvak.nicoai.features.comments.repository;

import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  @EntityGraph(attributePaths = "author")
  Page<Comment> findByImageId(Long id, Pageable pageable);
}
