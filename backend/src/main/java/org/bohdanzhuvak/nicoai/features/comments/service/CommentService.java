package org.bohdanzhuvak.nicoai.features.comments.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.comments.CommentFactory;
import org.bohdanzhuvak.nicoai.features.comments.CommentResponseMapper;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.bohdanzhuvak.nicoai.features.comments.repository.CommentRepository;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.exception.CommentNotFoundException;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.bohdanzhuvak.nicoai.shared.exception.UnauthorizedActionException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final ImageRepository imageRepository;
  private final CommentResponseMapper commentResponseMapper;
  private final CommentFactory commentFactory;

  public List<CommentResponse> getComments(Long imageId) {
    List<Comment> comments = commentRepository.findByImage_id(imageId, Sort.by(Sort.Direction.ASC, "createdAt"));
    return comments.stream()
        .map(commentResponseMapper::toCommentResponse)
        .collect(Collectors.toList());
  }

  public CommentResponse postComment(CommentRequest commentRequest, User currentUser) {
    Long imageId = commentRequest.getImageId();
    Image image = imageRepository.findById(imageId)
        .orElseThrow(() -> new ImageNotFoundException("Image with ID " + imageId + " not found"));
    Comment comment = commentFactory.createComment(commentRequest, image, currentUser);
    Comment savedComment = commentRepository.save(comment);
    return commentResponseMapper.toCommentResponse(savedComment);
  }

  public void deleteComment(Long commentId, User currentUser) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CommentNotFoundException("Comment with ID " + commentId + " not found"));

    if (!comment.getAuthor().getId().equals(currentUser.getId())) {
      throw new UnauthorizedActionException("Unauthorized to delete this comment");
    }

    commentRepository.deleteById(commentId);
  }
}
