package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.comment.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.comment.CommentResponse;
import org.bohdanzhuvak.nicoai.model.Comment;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.CommentRepository;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {
  private final CommentRepository commentRepository;
  private final ImageRepository imageRepository;

  public List<CommentResponse> getComments(Long id) {
    List<Comment> comments = commentRepository.findByImage_id(id, Sort.by(Sort.Direction.ASC, "createdAt"));
    return comments.stream()
        .map(comment -> CommentResponse.builder()
            .id(comment.getId())
            .authorId(comment.getAuthor().getId())
            .authorName(comment.getAuthor().getUsername())
            .body(comment.getBody())
            .createdAt(comment.getCreatedAt())
            .build())
        .collect(Collectors.toList());
  }

  public CommentResponse postComment(CommentRequest commentRequest,
                                     Long imageId,
                                     User currentUser) {
    Optional<Image> image = imageRepository.findById(imageId);
    if (image.isEmpty()) {
      return null;
    }
    Comment comment = Comment.builder()
        .author(currentUser)
        .image(image.get())
        .body(commentRequest.getBody())
        .build();
    commentRepository.save(comment);
    return CommentResponse.builder()
        .id(comment.getId())
        .authorId(comment.getAuthor().getId())
        .authorName(comment.getAuthor().getUsername())
        .body(comment.getBody())
        .createdAt(comment.getCreatedAt())
        .build();
  }

  public void deleteComment(Long id, User currentUser) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    if (optionalComment.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
    }

    Comment comment = optionalComment.get();
    if (!comment.getAuthor().getUsername().equals(currentUser.getUsername())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized to delete this comment");
    }

    try {
      commentRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting comment");
    }
  }
}
