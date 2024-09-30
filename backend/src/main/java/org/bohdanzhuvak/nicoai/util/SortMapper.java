package org.bohdanzhuvak.nicoai.util;

import org.springframework.data.domain.Sort;

public class SortMapper {

  public static String mapSortBy(String sortBy) {
    if ("date".equalsIgnoreCase(sortBy)) {
      return "id";
    } else if ("rating".equalsIgnoreCase(sortBy)) {
      return "likes";
    } else {
      return "id";
    }
  }

  public static Sort.Direction mapSortDirection(String order) {
    return "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
  }
}