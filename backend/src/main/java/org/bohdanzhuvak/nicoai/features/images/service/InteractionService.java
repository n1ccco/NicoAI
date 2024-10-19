package org.bohdanzhuvak.nicoai.features.images.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.request.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.model.Visibility;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.exception.ImageNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InteractionService {
  private final ImageRepository imageRepository;

  public boolean checkIfUserLikedImage(Image image,
                                       Long userId) {
    return image.getLikes().stream().anyMatch(user -> user.getId().equals(userId));
  }

  public void changeImage(Long id,
                          InteractionImageRequest interactionImageRequest,
                          User user) {

    Image image = imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found"));

    switch (interactionImageRequest.getAction()) {
      case "like":
        if (image.getLikes().add(user)) {
          image.setLikeCount(image.getLikeCount() + 1);
        }
        break;
      case "dislike":
        if (image.getLikes().remove(user)) {
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
