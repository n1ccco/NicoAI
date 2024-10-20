package org.bohdanzhuvak.nicoai.features.images.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.request.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Like;
import org.bohdanzhuvak.nicoai.features.images.model.LikeId;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.images.repository.LikeRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.springframework.stereotype.Service;

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

  public void changeImage(Long id,
                          InteractionImageRequest interactionImageRequest,
                          User user) {

    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found"));
    LikeId likeId = new LikeId(user.getId(), image.getId());

    switch (interactionImageRequest.getAction()) {
      case "like":
        if (!likeRepository.existsById(likeId)) {
          Like like = new Like(likeId, user, image);
          likeRepository.save(like);
          image.setLikeCount(image.getLikeCount() + 1);
        }
        break;
      case "dislike":
        if (likeRepository.existsById(likeId)) {
          likeRepository.deleteById(likeId);
          image.setLikeCount(image.getLikeCount() - 1);
        }
        break;
      case "makePublic":
        image.setVisibility(Visibility.PUBLIC);
        break;
      case "makePrivate":
        image.setVisibility(Visibility.PRIVATE);
        break;
      default:
        System.out.println("Invalid action");
        return;
    }

    imageRepository.save(image);
  }

}
