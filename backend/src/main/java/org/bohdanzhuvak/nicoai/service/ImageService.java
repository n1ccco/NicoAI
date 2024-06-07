package org.bohdanzhuvak.nicoai.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bohdanzhuvak.nicoai.config.ImageGeneratorProperties;
import org.bohdanzhuvak.nicoai.dto.CustomMultipartFile;
import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.model.CustomUserDetails;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.ImageData;
import org.bohdanzhuvak.nicoai.model.PromptData;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;
  private final ImageGeneratorProperties imageGeneratorProperties;
  private final String FOLDER_PATH = "/images/";
  private final UserRepository userRepository;

  private MultiValueMap<String, String> buildPromptParams(PromptRequest promptRequest) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("prompt", promptRequest.getPrompt());
    params.add("negativePrompt", promptRequest.getNegativePrompt());
    params.add("height", String.valueOf(promptRequest.getHeight()));
    params.add("width", String.valueOf(promptRequest.getWidth()));
    params.add("numInterferenceSteps", String.valueOf(promptRequest.getNumInterferenceSteps()));
    params.add("guidanceScale", String.valueOf(promptRequest.getGuidanceScale()));
    return params;
  }

  public GenerateResponse generateImage(PromptRequest promptRequest) throws IOException {
    if (isUserAuthenticated()) {
      User author = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
          .getPrincipal()).getUser();
      byte[] imageBytes = fetchImageFromGenerator(promptRequest);
      MultipartFile multipartFile = saveImageToFileSystem(imageBytes, UUID.randomUUID() + ".png");
      Image image = buildImageEntity(multipartFile, promptRequest, author);
      Long imageId = imageRepository.save(image).getId();
      return new GenerateResponse(imageId);
    } else {
      return new GenerateResponse();
    }
  }

  private byte[] fetchImageFromGenerator(PromptRequest promptRequest) {
    RestTemplate restTemplate = new RestTemplate();
    String uri = UriComponentsBuilder.fromHttpUrl(imageGeneratorProperties.getUrl())
        .pathSegment("generate")
        .queryParams(buildPromptParams(promptRequest))
        .build()
        .toUriString();
    return restTemplate.getForObject(uri, byte[].class);
  }

  private MultipartFile saveImageToFileSystem(byte[] imageBytes, String filename) throws IOException {
    MultipartFile multipartFile = new CustomMultipartFile(filename, imageBytes);
    String filePath = FOLDER_PATH + multipartFile.getName();
    multipartFile.transferTo(new File(filePath));
    return multipartFile;
  }

  private Image buildImageEntity(MultipartFile file, PromptRequest promptRequest, User author) {
    ImageData imageData = ImageData.builder()
        .name(file.getName())
        .type(file.getContentType())
        .path(FOLDER_PATH + file.getName())
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

  public List<ImageResponse> getAllImages() {
    List<Image> images = imageRepository.findByIsPublic(true);
    if (isUserAuthenticated()) {
      CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
          .getPrincipal();
      return images.stream()
          .map(image -> buildImageResponse(image, userDetails.getUser().getId()))
          .collect(Collectors.toList());
    } else {
      return images.stream()
          .map(image -> buildImageResponse(image, null))
          .collect(Collectors.toList());
    }

  }

  public List<ImageResponse> getAllUserImages(Long id) {
    CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    List<Image> images;
    Long currentUserId = userDetails.getUser().getId();
    if (currentUserId == id) {
      images = imageRepository.findByAuthorId(id);
    } else {
      images = imageRepository.findByAuthorIdAndIsPublic(id, true);
    }

    return images.stream().map(image -> buildImageResponse(image, currentUserId))
        .collect(Collectors.toList());
  }

  public ImageResponse getImage(Long id) {
    Optional<Image> optionalImage = imageRepository.findById(id);
    if (optionalImage.isPresent()) {
      Image image = optionalImage.get();
      if (isUserAuthenticated()) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        return buildImageResponse(image, userDetails.getUser().getId());

      }
      return buildImageResponse(image, null);
    } else {
      return new ImageResponse();
    }
  }

  public boolean isUserAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated() &&
        !(authentication.getPrincipal() instanceof String);
  }

  private ImageResponse buildImageResponse(Image image, Long userId) {
    String filePath = image.getImageData().getPath();
    byte[] images;
    try {
      images = Files.readAllBytes(new File(filePath).toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    boolean isLiked;
    if (userId != null) {
      isLiked = checkIfUserLikedImage(image, userId);
    } else {
      isLiked = false;
    }
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

  private boolean checkIfUserLikedImage(Image image, Long userId) {
    return image.getLikes().stream().anyMatch(user -> user.getId().equals(userId));
  }

  public void changeImage(Long id, InteractionImageRequest interactionImageRequest) {
    if (isUserAuthenticated()) {
      User userFromContext = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
          .getPrincipal()).getUser();
      // TODO This shouldn't be here.
      User user = userRepository.findByUsername(userFromContext.getUsername());

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

      switch (interactionImageRequest.getAction()) {
        case "like":
          image.getLikes().add(user);
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

}
