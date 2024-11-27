package org.bohdanzhuvak.nicoai.features.images.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImagesResponse;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageSearchRepository;
import org.bohdanzhuvak.nicoai.features.images.search.ImageSearch;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageSearchService {
  private final ImageSearchRepository imageSearchRepository;
  private final ImageService imageService;

  public ImagesResponse searchImages(String keyword, User currentUser, int page, int size) {
    List<ImageSearch> searchResults = imageSearchRepository.findByPromptDescriptionContaining(keyword);

    return imageService.buildImagesResponse(searchResults, currentUser, page, size);
  }
}