package org.bohdanzhuvak.nicoai.controller;

import java.util.List;

import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/users")
public class UsersController {
    private final ImageService imageService;

    @GetMapping(value = "{id}/images")
    @ResponseStatus(HttpStatus.OK)
    public List<ImageResponse> getImages(@PathVariable("id") Long id) {
        return imageService.getAllUserImages(id);
    }
}
