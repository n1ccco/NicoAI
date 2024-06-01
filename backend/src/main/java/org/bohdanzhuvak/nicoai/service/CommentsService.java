package org.bohdanzhuvak.nicoai.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bohdanzhuvak.nicoai.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.model.Comment;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.CommentRepository;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentsService {
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final ImageRepository imageRepository;

  public List<CommentResponse> getComments(Long id) {
    List<Comment> comments = commentRepository.findByImage_id(id);
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

  public void postComment(CommentRequest commentRequest, UserDetails userDetails, Long imageId) {
    User user = userRepository.findByUsername(userDetails.getUsername());
    Image image = imageRepository.findById(imageId).get();
    if (user == null || image == null) {
      return;
    }
    Comment comment = Comment.builder()
        .author(user)
        .image(image)
        .body(commentRequest.getBody())
        .build();
    commentRepository.save(comment);
  }

  public void deleteComment(UserDetails userDetails, Long id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    if (!optionalComment.isPresent()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
    }

    Comment comment = optionalComment.get();
    if (!comment.getAuthor().getUsername().equals(userDetails.getUsername())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized to delete this comment");
    }

    try {
      commentRepository.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting comment");
    }
  }
}
