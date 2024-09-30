package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.image.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.image.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.image.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.exception.ImageNotFoundException;
import org.bohdanzhuvak.nicoai.exception.UnauthorizedActionException;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.service.factory.ImageFactory;
import org.bohdanzhuvak.nicoai.service.mapper.ImageResponseMapper;
import org.bohdanzhuvak.nicoai.util.SortMapper;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
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

  public List<ImageResponse> getAllImages(String sortBy, String order, User currentUser) {
    Sort.Direction direction = SortMapper.mapSortDirection(order);
    String sanitizedSortBy = SortMapper.mapSortBy(sortBy);

    List<Image> images = getImagesSortedBy(sanitizedSortBy, direction);

    Long currentUserId = currentUser != null ? currentUser.getId() : null;

    return images.stream()
        .map(image -> imageResponseMapper.toImageResponse(image, currentUserId))
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

  public List<ImageResponse> getAllUserImages(Long userId, User currentUser) {
    Long currentUserId = currentUser != null ? currentUser.getId() : null;
    List<Image> images = (currentUserId != null && currentUserId.equals(userId))
        ? imageRepository.findByAuthorId(userId)
        : imageRepository.findByAuthorIdAndIsPublic(userId, true);
    return images.stream()
        .map(image -> imageResponseMapper.toImageResponse(image, currentUserId))
        .collect(Collectors.toList());
  }

  public ImageResponse getImage(Long id, @Nullable User currentUser) {
    return imageRepository.findById(id)
        .map(image -> imageResponseMapper.toImageResponse(image, currentUser != null ? currentUser.getId() : null))
        .orElseThrow(() -> new ImageNotFoundException("Image not found"));
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
