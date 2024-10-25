package org.bohdanzhuvak.nicoai.comments;

import org.bohdanzhuvak.nicoai.features.comments.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.bohdanzhuvak.nicoai.features.comments.repository.CommentRepository;
import org.bohdanzhuvak.nicoai.features.comments.service.CommentService;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.bohdanzhuvak.nicoai.shared.exception.CommentNotFoundException;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.bohdanzhuvak.nicoai.shared.exception.UnauthorizedActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Sql(scripts = "/db/test-data/CommentServiceTest-data.sql")
public class CommentServiceTest {

  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommentService commentService;

  private User user;
  private User otherUser;
  private Image image;

  @BeforeEach
  public void setUp() {
    user = userRepository.findByUsername("user").orElseThrow(() -> new RuntimeException("User not found"));
    otherUser = userRepository.findByUsername("otherUser").orElseThrow(() -> new RuntimeException("OtherUser not found"));

    image = imageRepository.findById(1L).orElseThrow(() -> new RuntimeException("Image1 not found"));
  }

  @Test
  public void testGetComments_Success() {
    List<CommentResponse> comments = commentService.getComments(image.getId());

    assertEquals(3, comments.size());

    assertEquals("First comment", comments.get(0).getBody());
    assertEquals("Second comment", comments.get(1).getBody());
    assertEquals("Authors comment", comments.get(2).getBody());
  }

  @Test
  public void testPostComment_Success() {
    CommentRequest commentRequest = new CommentRequest();
    commentRequest.setBody("New Test Comment");
    commentRequest.setImageId(image.getId());

    CommentResponse response = commentService.postComment(commentRequest, user);

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
    commentRequest.setImageId(nonExistentImageId);

    ImageNotFoundException exception = assertThrows(ImageNotFoundException.class,
        () -> commentService.postComment(commentRequest, user));

    assertEquals("Image with ID " + nonExistentImageId + " not found", exception.getMessage());
  }

  @Test
  public void testDeleteComment_Success() {
    Long commentId = 1L;

    commentService.deleteComment(commentId, user);

    Optional<Comment> deletedComment = commentRepository.findById(commentId);
    assertFalse(deletedComment.isPresent());
  }

  @Test
  public void testDeleteComment_CommentNotFound() {
    Long nonExistentCommentId = 999L;

    CommentNotFoundException exception = assertThrows(CommentNotFoundException.class,
        () -> commentService.deleteComment(nonExistentCommentId, user));

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