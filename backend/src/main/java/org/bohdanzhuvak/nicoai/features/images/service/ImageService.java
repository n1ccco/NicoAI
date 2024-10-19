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
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.bohdanzhuvak.nicoai.shared.exception.UnauthorizedActionException;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

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

    return images.stream()
        .map(image -> imageResponseMapper.toImageResponseSimplified(image, currentUserId))
        .collect(Collectors.toList());
  }

  private List<Image> getImagesSortedBy(String sortBy, Sort.Direction direction) {
    if ("likes".equals(sortBy)) {
      return direction == Sort.Direction.ASC
          ? imageRepository.findByIsPublicOrderByLikesSizeAsc()
          : imageRepository.findByIsPublicOrderByLikesSizeDesc();
    } else {
      return imageRepository.findByIsPublic(true, Sort.by(direction, sortBy));
    }
  }

  private List<Image> getImagesByUserId(Long userId, User currentUser) {
    boolean isOwnerOrAdmin = currentUser != null && (currentUser.isAdmin() || currentUser.getId().equals(userId));
    return isOwnerOrAdmin
        ? imageRepository.findByAuthorId(userId)
        : imageRepository.findByAuthorIdAndIsPublic(userId, true);
  }

  public ImageResponse getImage(Long id, @Nullable User currentUser) {
    return imageRepository.findById(id)
        .map(image -> imageResponseMapper.toImageResponse(image, currentUser != null ? currentUser.getId() : null))
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));
  }

  public ImageBlobResponse getImageBlob(Long id, @Nullable User currentUser) {
    Image foundImage = imageRepository.findById(id)
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));

    boolean isAuthorized = foundImage.isPublic() ||
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
