package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.search.ImageSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ImageSearchRepository extends ElasticsearchRepository<ImageSearch, Long> {
  Page<ImageSearch> findByPromptDescriptionContainingAndVisibility(String keyword, Visibility visibility, Pageable pageable);

  Page<ImageSearch> findByPromptDescriptionContainingAndAuthorId(String keyword, Long authorId, Pageable pageable);

  Page<ImageSearch> findByPromptDescriptionContainingAndAuthorIdAndVisibility(String keyword, Long authorId, Visibility visibility, Pageable pageable);
}
