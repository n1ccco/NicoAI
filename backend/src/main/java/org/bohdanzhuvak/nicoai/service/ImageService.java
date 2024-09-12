package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.config.ImageProperties;
import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.ImageData;
import org.bohdanzhuvak.nicoai.model.PromptData;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;
  private final FileService fileService;
  private final ImageGeneratorService imageGeneratorService;
  private final AuthenticationService authenticationService;
  private final InteractionService interactionService;
  private final ImageProperties imageProperties;

  public GenerateResponse generateImage(PromptRequest promptRequest, User author) {
    byte[] imageBytes = imageGeneratorService.fetchImageFromGenerator(promptRequest);
    MultipartFile multipartFile;
    try {
      multipartFile = fileService.saveImageToFileSystem(imageBytes, UUID.randomUUID() + ".png");
    } catch (IOException e) {
      e.printStackTrace();
      return new GenerateResponse();
    }
    Image image = buildImageEntity(multipartFile, promptRequest, author);
    Long imageId = imageRepository.save(image).getId();
    return new GenerateResponse(imageId);
  }

  private Image buildImageEntity(MultipartFile file, PromptRequest promptRequest, User author) {
    ImageData imageData = ImageData.builder()
        .name(file.getName())
        .type(file.getContentType())
        .path(imageProperties.getFOLDER_PATH() + file.getName())
        .build();

    PromptData promptData = PromptData.builder()
        .prompt(promptRequest.getPrompt())
        .negativePrompt(promptRequest.getNegativePrompt())
        .height(promptRequest.getHeight())
        .width(promptRequest.getWidth())
        .numInterferenceSteps(promptRequest.getNumInterferenceSteps())
        .guidanceScale(promptRequest.getGuidanceScale())
        .build();

    return Image.builder()
        .author(author)
        .imageData(imageData)
        .promptData(promptData)
        .build();
  }

  public List<ImageResponse> getAllImages(String sortBy, String order) {
    String sanitizedSortBy = mapSortBy(sortBy);
    Sort.Direction direction = mapSortDirection(order);

    List<Image> images = getImagesSortedBy(sanitizedSortBy, direction);
    if (authenticationService.isUserAuthenticated()) {
      User user = authenticationService.getCurrentAuthenticatedUser();
      return images.stream()
          .map(image -> buildImageResponse(image, user.getId()))
          .collect(Collectors.toList());
    } else {
      return images.stream()
          .map(image -> buildImageResponse(image, null))
          .collect(Collectors.toList());
    }

  }

  private List<Image> getImagesSortedBy(String sanitizedSortBy, Sort.Direction direction) {
    if ("likes".equals(sanitizedSortBy)) {
      if (direction == Sort.Direction.ASC) {
        return imageRepository.findByIsPublicOrderByLikesSizeAsc();
      } else {
        return imageRepository.findByIsPublicOrderByLikesSizeDesc();
      }
    } else {
      return imageRepository.findByIsPublic(true, Sort.by(direction, sanitizedSortBy));
    }
  }

  private String mapSortBy(String sortBy) {
    switch (sortBy.toLowerCase()) {
      case "date":
        return "id";
      case "rating":
        return "likes";
      default:
        return null;
    }
  }

  private Sort.Direction mapSortDirection(String order) {
    if ("asc".equalsIgnoreCase(order)) {
      return Sort.Direction.ASC;
    } else {
      return Sort.Direction.DESC;
    }
  }

  public List<ImageResponse> getAllUserImages(Long userId) {
    User currentUser = authenticationService.getCurrentAuthenticatedUser();
    Long currentUserId = currentUser != null ? currentUser.getId() : null;
    List<Image> images;
    if (currentUserId != null && currentUserId.equals(userId)) {
      images = imageRepository.findByAuthorId(userId);
    } else {
      images = imageRepository.findByAuthorIdAndIsPublic(userId, true);
    }

    return images.stream()
        .map(image -> buildImageResponse(image, currentUserId))
        .collect(Collectors.toList());
  }

  public ImageResponse getImage(Long id) {
    Optional<Image> optionalImage = imageRepository.findById(id);
    if (optionalImage.isPresent()) {
      Image image = optionalImage.get();
      User currentUser = authenticationService.getCurrentAuthenticatedUser();
      return buildImageResponse(image, currentUser != null ? currentUser.getId() : null);
    } else {
      return new ImageResponse();
    }
  }

  private ImageResponse buildImageResponse(Image image, Long userId) {
    byte[] images;
    try {
      images = fileService.readFileBytes(image.getImageData().getName());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    boolean isLiked = userId != null && interactionService.checkIfUserLikedImage(image, userId);
    int countLikes = image.getLikes().size();

    return ImageResponse.builder()
        .id(image.getId())
        .promptData(image.getPromptData())
        .authorId(image.getAuthor().getId())
        .authorName(image.getAuthor().getUsername())
        .isPublic(image.isPublic())
        .isLiked(isLiked)
        .countLikes(countLikes)
        .imageData(images)
        .build();
  }

  public void changeImage(Long id, InteractionImageRequest interactionImageRequest) {
    if (authenticationService.isUserAuthenticated()) {
      User user = authenticationService.getCurrentAuthenticatedUser();
      interactionService.changeImage(id, interactionImageRequest, user);
    } else {
      System.out.println("User is not authenticated");
    }
  }

  public void deleteImage(Long id) {
    imageRepository.deleteById(id);
  }

}
