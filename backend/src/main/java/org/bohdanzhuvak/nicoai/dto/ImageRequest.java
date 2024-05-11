package org.bohdanzhuvak.nicoai.dto;
import lombok.*;

import org.bohdanzhuvak.nicoai.model.User;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {
    private String description;
    private MultipartFile imageFile;
    private User author;
}
