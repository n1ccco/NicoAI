package org.bohdanzhuvak.nicoai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String username;
    private String token;
}
