package org.bohdanzhuvak.nicoai.features.images.repository;

import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.search.ImageSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ImageSearchRepository extends ElasticsearchRepository<ImageSearch, Long> {
  List<ImageSearch> findByPromptDescriptionContainingAndVisibility(String keyword, Visibility visibility);
}
