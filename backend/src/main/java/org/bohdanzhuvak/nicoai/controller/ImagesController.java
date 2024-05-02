package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
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
    public Long generateImage(@ModelAttribute PromptRequest promptRequest) {
        return imageService.generateImage(promptRequest);
    }
}
