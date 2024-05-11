package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;

import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/images")
public class ImagesController {
    private final ImageService imageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ImageResponse> getImages() {
        return imageService.getAllImages();
    }

    @GetMapping(value = "{id}")
    @ResponseStatus(HttpStatus.OK)
    public ImageResponse getImage(@PathVariable("id") Long id) {
        return imageService.getImage(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GenerateResponse generateImage(@ModelAttribute PromptRequest promptRequest, @AuthenticationPrincipal UserDetails user) {

        return imageService.generateImage(promptRequest, user);
    }
}
