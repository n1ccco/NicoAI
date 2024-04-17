package org.bohdanzhuvak.nicoai.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.ImageRequest;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImagesController {
    private final ImageService imageService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ImageResponse> getImages() {
        return imageService.getAllImages();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createImage(@ModelAttribute ImageRequest imageRequest) {
        imageService.createImage(imageRequest);
    }
}
