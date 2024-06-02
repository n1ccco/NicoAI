package org.bohdanzhuvak.nicoai.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bohdanzhuvak.nicoai.config.ImageGeneratorProperties;
import org.bohdanzhuvak.nicoai.dto.ChangeImagePrivacyRequest;
import org.bohdanzhuvak.nicoai.dto.CustomMultipartFile;
import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.ImageRequest;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.ImageData;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;
  private final UserRepository userRepository;
  private final ImageGeneratorProperties imageGeneratorProperties;
  private final String FOLDER_PATH = "/images/";

  private GenerateResponse createImage(ImageRequest imageRequest) {
    Long imageId = imageRepository.save(toImage(imageRequest)).getId();
    try {
      imageRequest.getImageFile().transferTo(new File(FOLDER_PATH + imageRequest.getImageFile().getName()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new GenerateResponse(imageId);
  }

  public GenerateResponse generateImage(PromptRequest promptRequest, UserDetails authorDetails) {
    RestTemplate restTemplate = new RestTemplate();
    String uri = UriComponentsBuilder.fromHttpUrl(imageGeneratorProperties.getUrl())
        .pathSegment("generate")
        .queryParam("prompt", promptRequest.getPrompt())
        .queryParam("negativePrompt", promptRequest.getNegativePrompt())
        .queryParam("height", promptRequest.getHeight())
        .queryParam("width", promptRequest.getWidth())
        .queryParam("numInterferenceSteps", promptRequest.getNumInterferenceSteps())
        .queryParam("guidanceScale", promptRequest.getGuidanceScale())
        .build()
        .toUriString();
    byte[] imageBytes = restTemplate.getForObject(uri, byte[].class, promptRequest);
    MultipartFile multipartFile = new CustomMultipartFile(UUID.randomUUID() + ".png", imageBytes);

    User author = userRepository.findByUsername(authorDetails.getUsername());
    return createImage(new ImageRequest(promptRequest.getPrompt(), multipartFile, author));
  }

  public List<ImageResponse> getAllImages(UserDetails userDetails) {
    List<Image> images = imageRepository.findByIsPublic(true);
    return images.stream()
        .map(image -> toImageResponse(image, userDetails))
        .collect(Collectors.toList());
  }

  public List<ImageResponse> getAllImages() {
    List<Image> images = imageRepository.findByIsPublic(true);
    return images.stream()
        .map(image -> toImageResponse(image))
        .collect(Collectors.toList());
  }

  private ImageResponse toImageResponse(Image image, UserDetails userDetails) {
    String filePath = image.getImageData().getPath();
    byte[] images;
    boolean isLiked = checkIfUserLikedImage(image, userDetails);
    try {
      images = Files.readAllBytes(new File(filePath).toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ImageResponse.builder()
        .id(image.getId())
        .description(image.getPrompt())
        .authorId(image.getAuthor().getId())
        .isPublic(image.isPublic())
        .isLiked(isLiked)
        .imageData(images)
        .build();
  }

  public ImageResponse getImage(Long id, UserDetails userDetails) {
    Optional<Image> optionalImage = imageRepository.findById(id);
    if (!optionalImage.isPresent()) {
      return ImageResponse.builder().build();
    }
    Image image = optionalImage.get();
    return buildImageResponse(image, userDetails);
  }

  public ImageResponse getImage(Long id) {
    Optional<Image> optionalImage = imageRepository.findById(id);
    if (!optionalImage.isPresent()) {
      return ImageResponse.builder().build();
    }
    Image image = optionalImage.get();
    String filePath = image.getImageData().getPath();
    byte[] images;
    try {
      images = Files.readAllBytes(new File(filePath).toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ImageResponse.builder()
        .id(image.getId())
        .description(image.getPrompt())
        .authorId(image.getAuthor().getId())
        .isPublic(image.isPublic())
        .imageData(images)
        .build();
  }

  private ImageResponse buildImageResponse(Image image, UserDetails userDetails) {
    String filePath = image.getImageData().getPath();
    byte[] images;
    boolean isLiked = checkIfUserLikedImage(image, userDetails);
    try {
      images = Files.readAllBytes(new File(filePath).toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ImageResponse.builder()
        .id(image.getId())
        .description(image.getPrompt())
        .authorId(image.getAuthor().getId())
        .isPublic(image.isPublic())
        .isLiked(isLiked)
        .imageData(images)
        .build();
  }

  private boolean checkIfUserLikedImage(Image image, UserDetails userDetails) {
    return image.getLikes().stream()
        .anyMatch(user -> user.getUsername().equals(userDetails.getUsername()));
  }

  private ImageResponse toImageResponse(Image image) {
    String filePath = image.getImageData().getPath();
    byte[] images;
    try {
      images = Files.readAllBytes(new File(filePath).toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return ImageResponse.builder()
        .id(image.getId())
        .description(image.getPrompt())
        .authorId(image.getAuthor().getId())
        .isPublic(image.isPublic())
        .imageData(images)
        .build();
  }

  private Image toImage(ImageRequest imageRequest) {
    String filePath = FOLDER_PATH + imageRequest.getImageFile().getName();
    ImageData imageData = new ImageData(imageRequest.getImageFile().getName(),
        imageRequest.getImageFile().getContentType(), filePath);
    return new Image(imageRequest.getDescription(), imageData, imageRequest.getAuthor());
  }

  public List<ImageResponse> getAllUserImages(Long id) {
    List<Image> images = imageRepository.findByAuthorId(id);
    return images.stream().map(this::toImageResponse).toList();
  }

  public void changePrivacy(Long id, ChangeImagePrivacyRequest changeImagePrivacyRequest) {
    Image image = imageRepository.findById(id).get();
    image.setPublic(changeImagePrivacyRequest.isPublic());
    imageRepository.save(image);
  }

  public void changeImage(Long id, UserDetails userDetails, InteractionImageRequest interactionImageRequest) {
    if (interactionImageRequest.getAction() == null) {
      System.out.println("Action is null");
      return;
    }

    Optional<Image> optionalImage = imageRepository.findById(id);
    if (!optionalImage.isPresent()) {
      System.out.println("Image not found");
      return;
    }
    Image image = optionalImage.get();

    User user = userRepository.findByUsername(userDetails.getUsername());
    if (user == null) {
      System.out.println("User not found");
      return;
    }

    switch (interactionImageRequest.getAction()) {
      case "like":
        if (!image.getLikes().contains(user)) {
          image.getLikes().add(user);
        }
        break;
      case "dislike":
        image.getLikes().remove(user);
        break;
      case "makePublic":
        image.setPublic(true);
        break;
      case "makePrivate":
        image.setPublic(false);
        break;
      default:
        System.out.println("Invalid action");
        return;
    }

    imageRepository.save(image);
  }

}
