package org.bohdanzhuvak.nicoai.service;

import org.bohdanzhuvak.nicoai.dto.comment.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.comment.CommentResponse;
import org.bohdanzhuvak.nicoai.exception.CommentNotFoundException;
import org.bohdanzhuvak.nicoai.exception.ImageNotFoundException;
import org.bohdanzhuvak.nicoai.exception.UnauthorizedActionException;
import org.bohdanzhuvak.nicoai.model.Comment;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.CommentRepository;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CommentServiceTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentService commentService;

  private User user1;
  private User otherUser;
  private Image image1;
  private Image image2;

  @BeforeEach
  public void setUp() {
    user1 = userRepository.findByUsername("user").orElseThrow(() -> new RuntimeException("User not found"));
    otherUser = userRepository.findByUsername("otherUser").orElseThrow(() -> new RuntimeException("OtherUser not found"));

    image1 = imageRepository.findById(1L).orElseThrow(() -> new RuntimeException("Image1 not found"));
    image2 = imageRepository.findById(2L).orElseThrow(() -> new RuntimeException("Image2 not found"));
  }

  @Test
  public void testGetComments_Success() {
    List<CommentResponse> comments = commentService.getComments(image1.getId());

    assertEquals(3, comments.size());

    assertEquals("First comment", comments.get(0).getBody());
    assertEquals("Second comment", comments.get(1).getBody());
    assertEquals("Authors comment", comments.get(2).getBody());
  }

  @Test
  public void testPostComment_Success() {
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setBody("New Test Comment");

    CommentResponse response = commentService.postComment(commentRequest, image2.getId(), user1);

    assertNotNull(response);
    assertNotNull(response.getId());
    assertEquals("New Test Comment", response.getBody());
    assertEquals("user", response.getAuthorName());

    Optional<Comment> savedComment = commentRepository.findById(response.getId());
    assertTrue(savedComment.isPresent());
    assertEquals("New Test Comment", savedComment.get().getBody());
  }

  @Test
  public void testPostComment_ImageNotFound() {
    Long nonExistentImageId = 999L;
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setBody("Comment for non-existent image");

    ImageNotFoundException exception = assertThrows(ImageNotFoundException.class,
        () -> commentService.postComment(commentRequest, nonExistentImageId, user1));

    assertEquals("Image with ID " + nonExistentImageId + " not found", exception.getMessage());
  }

  @Test
  public void testDeleteComment_Success() {
    Long commentId = 1L;

    commentService.deleteComment(commentId, user1);

    Optional<Comment> deletedComment = commentRepository.findById(commentId);
    assertFalse(deletedComment.isPresent());
  }

  @Test
  public void testDeleteComment_CommentNotFound() {
    Long nonExistentCommentId = 999L;

    CommentNotFoundException exception = assertThrows(CommentNotFoundException.class,
        () -> commentService.deleteComment(nonExistentCommentId, user1));

    assertEquals("Comment with ID " + nonExistentCommentId + " not found", exception.getMessage());
  }

  @Test
  public void testDeleteComment_UnauthorizedAction() {
    Long commentId = 3L;

    UnauthorizedActionException exception = assertThrows(UnauthorizedActionException.class,
        () -> commentService.deleteComment(commentId, otherUser));

    assertEquals("Unauthorized to delete this comment", exception.getMessage());
  }
}