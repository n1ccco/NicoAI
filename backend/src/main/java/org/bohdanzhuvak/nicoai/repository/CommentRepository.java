package org.bohdanzhuvak.nicoai.repository;

import org.bohdanzhuvak.nicoai.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByImage_id(Long id, Sort sort);
}
