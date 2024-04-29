package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.ImageRequest;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/image")
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
    @ResponseStatus(HttpStatus.CREATED)
    public void createImage(@ModelAttribute ImageRequest imageRequest) {
        imageService.createImage(imageRequest);
    }

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.OK)
    public void generate(@ModelAttribute PromptRequest promptRequest) {
        imageService.generateImage(promptRequest);
    }
}
