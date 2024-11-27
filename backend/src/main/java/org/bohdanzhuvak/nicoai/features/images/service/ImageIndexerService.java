package org.bohdanzhuvak.nicoai.features.images.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageSearchRepository;
import org.bohdanzhuvak.nicoai.features.images.search.ImageSearch;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageIndexerService {

  private final ImageSearchRepository imageSearchRepository;
  private final ImageRepository imageRepository;

  @PostConstruct
  public void indexImages() {
    List<Image> images = imageRepository.findAll();
    for (Image image : images) {
      Hibernate.initialize(image.getPromptData());

      ImageSearch imageSearch = new ImageSearch();
      imageSearch.setId(image.getId());
      imageSearch.setPromptDescription(image.getPromptData().getPrompt());
      imageSearch.setLikeCount(image.getLikeCount());

      imageSearchRepository.save(imageSearch);
    }
  }
}
