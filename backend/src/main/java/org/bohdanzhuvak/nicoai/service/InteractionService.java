package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.image.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InteractionService {
  private final ImageRepository imageRepository;

  public boolean checkIfUserLikedImage(Image image, Long userId) {
    return image.getLikes().stream().anyMatch(user -> user.getId().equals(userId));
  }

  public void changeImage(Long id, InteractionImageRequest interactionImageRequest, User user) {

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
