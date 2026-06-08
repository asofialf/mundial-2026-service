package mundial.predictor.apuestas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter
    @Setter
    @JsonProperty("user_id")
    private int userId;

    @Getter
    @Setter
    @JsonProperty("email")
    private String email;

    @Getter
    @Setter
    @JsonProperty("password_hash")
    private String passwordHash;

    @Getter
    @Setter
    @JsonProperty("rol_id")
    private int rolId;
}