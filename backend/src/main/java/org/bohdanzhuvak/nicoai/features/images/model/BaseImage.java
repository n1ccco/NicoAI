package org.bohdanzhuvak.nicoai.features.images.model;

import org.bohdanzhuvak.nicoai.features.users.model.User;

import java.util.Set;

public interface BaseImage {
  Long getId();
  User getAuthor();
  Visibility getVisibility();
  Long getLikeCount();
  Set<User> getLikes();
}