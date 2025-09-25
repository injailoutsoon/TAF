package ca.etsmtl.taf.auth.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequest {
    @NotBlank
    private String token;
}
