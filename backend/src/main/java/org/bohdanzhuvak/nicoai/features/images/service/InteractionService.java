package org.bohdanzhuvak.nicoai.features.images.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Like;
import org.bohdanzhuvak.nicoai.features.images.model.LikeId;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.images.repository.LikeRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class InteractionService {
  private final ImageRepository imageRepository;
  private final LikeRepository likeRepository;

  public boolean checkIfUserLikedImage(Long imageId, Long userId) {
    if (imageId == null || userId == null) {
      return false;
    }

    LikeId likeId = new LikeId(userId, imageId);

    return likeRepository.existsById(likeId);
  }

  public Set<Long> getLikedImageIdsForUserFromList(Long userId, Set<Long> imageIds) {
    return likeRepository.findImageIdByUserIdAndImageIdIn(userId, imageIds);
  }

  public void likeImage(Long id, User user) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found"));
    LikeId likeId = new LikeId(user.getId(), image.getId());
    if (!likeRepository.existsById(likeId)) {
      Like like = new Like(likeId, user, image);
      likeRepository.save(like);
      image.setLikeCount(image.getLikeCount() + 1);
    } else {
      likeRepository.deleteById(likeId);
      image.setLikeCount(image.getLikeCount() - 1);
    }
    imageRepository.save(image);
  }

  @CacheEvict(value = {"image_with_blob_data", "image_with_prompt_data"}, key = "#id")
  public void changeImageVisibility(Long id, User user) {
    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found"));

    boolean isAuthorized = image.getVisibility() == Visibility.PUBLIC ||
        (user != null && (user.getId().equals(image.getAuthor().getId()) || user.isAdmin()));

    if (isAuthorized) {
      image.setVisibility(image.getVisibility() == Visibility.PRIVATE ? Visibility.PUBLIC : Visibility.PRIVATE);
      imageRepository.save(image);
    }
  }

}
