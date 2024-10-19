package org.bohdanzhuvak.nicoai.features.comments.repository;

import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByImage_id(Long id, Sort sort);
}
