package org.bohdanzhuvak.nicoai.images;

import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
//todo: fix test
@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/db/test-data/ImageRepository-data.sql")
public class ImageRepositoryTest {
  @Autowired
  private ImageRepository imageRepository;

  /*@Test
  void testFindAllPublicImagesSortedByLikesAsc() {
    List<Image> publicImages = imageRepository.findByVisibility(
        Visibility.PUBLIC,
        Sort.by(Sort.Order.asc("likeCount"), Sort.Order.desc("id"))
    );

    assertEquals(3, publicImages.size());
    assertEquals(1L, publicImages.get(0).getLikeCount());
    assertTrue(publicImages.get(0).getLikeCount() < publicImages.get(1).getLikeCount());
    assertEquals(publicImages.get(1).getLikeCount(), publicImages.get(2).getLikeCount());
    assertTrue(publicImages.get(1).getId() > publicImages.get(2).getId());
  }

  @Test
  void testFindAllPublicImagesSortedByLikesDesc() {
    List<Image> publicImages = imageRepository.findByVisibility(
        Visibility.PUBLIC,
        PageRequest.of(page - 1, 6, getSort(sanitizedSortBy, direction)),
    );

    assertEquals(3, publicImages.size());
    assertEquals(3L, publicImages.get(0).getLikeCount());
    assertTrue(publicImages.get(1).getLikeCount() > publicImages.get(2).getLikeCount());
    assertEquals(publicImages.get(0).getLikeCount(), publicImages.get(1).getLikeCount());
    assertTrue(publicImages.get(0).getId() > publicImages.get(1).getId());
  }*/
}
