package org.bohdanzhuvak.nicoai.features.images.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.request.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.images.repository.ImageRepository;
import org.bohdanzhuvak.nicoai.features.users.model.User;
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

    Image image = imageRepository.findById(id).orElse(null);

    switch (interactionImageRequest.getAction()) {
      case "like":
        image.getLikes().add(user);
        break;
      case "dislike":
        image.getLikes().remove(user);
        break;
      case "makePublic":
        image.setPublic(true);
        break;
      case "makePrivate":
        image.setPublic(false);
        break;
      default:
        System.out.println("Invalid action");
        return;
    }

    imageRepository.save(image);
  }

}
