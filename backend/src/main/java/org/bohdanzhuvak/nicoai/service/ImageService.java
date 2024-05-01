package org.bohdanzhuvak.nicoai.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import org.bohdanzhuvak.nicoai.dto.CustomMultipartFile;
import org.bohdanzhuvak.nicoai.dto.ImageRequest;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.ImageData;
import org.bohdanzhuvak.nicoai.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final String FOLDER_PATH = "/home/nico/images/";

    private Long createImage(ImageRequest imageRequest) {
        Long imageId = imageRepository.save(toImage(imageRequest)).getId();
        try {
            imageRequest.getImageFile().transferTo(new File(FOLDER_PATH + imageRequest.getImageFile().getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imageId;
    }

    public Long generateImage(PromptRequest promptRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = UriComponentsBuilder.fromHttpUrl("http://localhost:5000")
            .pathSegment("generate")
            .queryParam("prompt", promptRequest.getPrompt())
            .queryParam("negativePrompt", promptRequest.getNegativePrompt())
            .queryParam("height", promptRequest.getHeight())
            .queryParam("width", promptRequest.getWidth())
            .queryParam("numInterferenceSteps", promptRequest.getNumInterferenceSteps())
            .queryParam("guidanceScale", promptRequest.getGuidanceScale())
            .build()
            .toUriString();
        byte[] imageBytes = restTemplate.getForObject(uri, byte[].class, promptRequest);
        MultipartFile multipartFile = new CustomMultipartFile(UUID.randomUUID() +".png", imageBytes);
        return createImage(new ImageRequest(promptRequest.getPrompt(), multipartFile));
    }

    public List<ImageResponse> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(this::toImageResponse).toList();
    }
    public ImageResponse getImage(Long id) {
        return toImageResponse(imageRepository.findById(id).get());
    }

    private ImageResponse toImageResponse(Image image) {
        String filePath = image.getImageData().getPath();
        byte[] images;
        try {
            images = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ImageResponse.builder()
                .id(image.getId())
                .description(image.getDescription())
                .imageData(images)
                .build();
    }
    private Image toImage(ImageRequest imageRequest) {
        String filePath = FOLDER_PATH + imageRequest.getImageFile().getName();
        ImageData imageData = new ImageData(imageRequest.getImageFile().getName(),imageRequest.getImageFile().getContentType(), filePath);
        return new Image(imageRequest.getDescription(), imageData);
    }


}
