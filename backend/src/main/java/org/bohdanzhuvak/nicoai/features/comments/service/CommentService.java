package org.bohdanzhuvak.nicoai.features.comments.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.comments.CommentFactory;
import org.bohdanzhuvak.nicoai.features.comments.CommentResponseMapper;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final ImageRepository imageRepository;
  private final CommentResponseMapper commentResponseMapper;
  private final CommentFactory commentFactory;

  public CommentResponse getComments(Long imageId, Integer page) {
    Page<Comment> commentPage = commentRepository.findByImageId(imageId, PageRequest.of(page - 1, 3, Sort.by("createdAt").descending()));

    List<Comment> comments = commentPage.getContent();
    long total = commentPage.getTotalElements();
    int totalPages = commentPage.getTotalPages();

    List<CommentDto> commentDtos = comments.stream()
        .map(commentResponseMapper::toCommentResponse)
        .toList();
    CommentResponse.Meta meta = new CommentResponse.Meta(page, total, totalPages);
    return new CommentResponse(commentDtos, meta);
  }

  public void postComment(CommentRequest commentRequest, User currentUser) {
    Long imageId = commentRequest.getImageId();
    Image image = imageRepository.findById(imageId)
        .orElseThrow(() -> new ImageNotFoundException("Image with ID " + imageId + " not found"));
    Comment comment = commentFactory.createComment(commentRequest, image, currentUser);
    commentRepository.save(comment);
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
