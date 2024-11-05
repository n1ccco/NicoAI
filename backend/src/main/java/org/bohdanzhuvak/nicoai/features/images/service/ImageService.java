package org.bohdanzhuvak.nicoai.features.images.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.ImageFactory;
import org.bohdanzhuvak.nicoai.features.images.ImageResponseMapper;
import org.bohdanzhuvak.nicoai.features.images.SortMapper;
import org.bohdanzhuvak.nicoai.features.images.dto.request.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.features.images.dto.request.PromptRequest;
import org.bohdanzhuvak.nicoai.features.images.dto.response.GenerateResponse;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageBlobResponse;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponse;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImageResponseSimplified;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.PromptData;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.bohdanzhuvak.nicoai.shared.exception.UnauthorizedActionException;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
  private final ImageRepository imageRepository;
  private final FileService fileService;
  private final ImageGeneratorService imageGeneratorService;
  private final InteractionService interactionService;
  private final ImageFactory imageFactory;
  private final ImageResponseMapper imageResponseMapper;

  public GenerateResponse generateImage(PromptRequest promptRequest,
                                        User author) {
    byte[] imageBytes = imageGeneratorService.fetchImageFromGenerator(promptRequest);
    String fileName = UUID.randomUUID() + ".png";
    fileService.saveImageToFileSystem(imageBytes, fileName);
    Image image = imageFactory.createImage(fileName, promptRequest, author);
    Long imageId = imageRepository.save(image).getId();
    return new GenerateResponse(imageId);
  }

  public List<ImageResponseSimplified> getAllImages(String sortBy, String order, User currentUser, Long userId) {
    Sort.Direction direction = SortMapper.mapSortDirection(order);
    String sanitizedSortBy = SortMapper.mapSortBy(sortBy);
    List<Image> images;
    if (userId != null) {
      images = getImagesByUserId(userId, currentUser);
    } else {
      images = getImagesSortedBy(sanitizedSortBy, direction);
    }

    Long currentUserId = currentUser != null ? currentUser.getId() : null;

    Set<Long> imageIds = images.stream()
        .map(Image::getId)
        .collect(Collectors.toSet());

    Set<Long> likedImageIds = currentUserId != null
        ? interactionService.getLikedImageIdsForUserFromList(currentUserId, imageIds)
        : Collections.emptySet();

    return images.stream()
        .map(image -> imageResponseMapper.toImageResponseSimplified(image, likedImageIds.contains(image.getId())))
        .collect(Collectors.toList());
  }

  private List<Image> getImagesSortedBy(String sortBy, Sort.Direction direction) {
    Sort sort = "likes".equals(sortBy)
        ? Sort.by(new Sort.Order(direction, "likeCount"), Sort.Order.desc("id"))
        : Sort.by(direction, sortBy);

    return imageRepository.findByVisibility(Visibility.PUBLIC, sort);
  }

  private List<Image> getImagesByUserId(Long userId, User currentUser) {
    boolean isOwnerOrAdmin = currentUser != null && (currentUser.isAdmin() || currentUser.getId().equals(userId));
    return isOwnerOrAdmin
        ? imageRepository.findByAuthorId(userId)
        : imageRepository.findByAuthorIdAndVisibility(userId, Visibility.PUBLIC);
  }

  public ImageResponse getImage(Long id, @Nullable User currentUser) {
    Image foundImage = imageRepository.findByIdWithAllData(id)
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));
    boolean isAuthorized = foundImage.getVisibility() == Visibility.PUBLIC ||
        (currentUser != null && (currentUser.getId().equals(foundImage.getAuthor().getId()) || currentUser.isAdmin()));
    return Optional.of(isAuthorized)
        .filter(auth -> auth)
        .map(auth -> {
          boolean isLiked = currentUser != null && interactionService.checkIfUserLikedImage(foundImage.getId(), currentUser.getId());
          return imageResponseMapper.toImageResponse(foundImage, isLiked);
        })
        .orElseThrow(() -> new UnauthorizedActionException("Unauthorized action"));
  }

  public PromptData getImagePrompt(Long id, @Nullable User currentUser) {
    Image foundImage = imageRepository.findByIdWithAllData(id)
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));

    boolean isAuthorized = foundImage.getVisibility() == Visibility.PUBLIC ||
        (currentUser != null && (currentUser.getId().equals(foundImage.getAuthor().getId()) || currentUser.isAdmin()));

    return Optional.of(isAuthorized)
        .filter(auth -> auth)
        .map(auth -> foundImage.getPromptData())
        .orElseThrow(() -> new UnauthorizedActionException("Unauthorized action"));
  }

  public ImageBlobResponse getImageBlob(Long id, @Nullable User currentUser) {
    Image foundImage = imageRepository.findByIdWithPartData(id)
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));

    boolean isAuthorized = foundImage.getVisibility() == Visibility.PUBLIC ||
        (currentUser != null && (currentUser.getId().equals(foundImage.getAuthor().getId()) || currentUser.isAdmin()));

    return Optional.of(isAuthorized)
        .filter(auth -> auth)
        .map(auth -> fileService.readFileBytes(foundImage.getImageData().getName()))
        .map(blobImage -> ImageBlobResponse.builder().imageBlob(blobImage).build())
        .orElseThrow(() -> new UnauthorizedActionException("Unauthorized action"));
  }

  public void changeImage(Long id,
                          InteractionImageRequest interactionImageRequest,
                          User currentUser) {
    interactionService.changeImage(id, interactionImageRequest, currentUser);
  }

  public void deleteImage(Long id, User currentUser) {
    Image image = imageRepository.findById(id)
        .orElseThrow(() -> new ImageNotFoundException("Image with ID " + id + " not found"));

    if (!image.getAuthor().getId().equals(currentUser.getId())) {
      throw new UnauthorizedActionException("You are not authorized to delete this image");
    }

    imageRepository.deleteById(id);
  }

}
