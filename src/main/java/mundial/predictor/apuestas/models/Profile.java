package mundial.predictor.apuestas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class Profile {
    @Getter
    @Setter
    @JsonProperty("profile_id")
    private int profileId;

    @Getter
    @Setter
    @JsonProperty("users_id")
    private int usersId;

    @Getter
    @Setter
    @JsonProperty("name")
    private String name;

    @Getter
    @Setter
    @JsonProperty("alias")
    private String alias;

    @Getter
    @Setter
    @JsonProperty("icon")
    private String icon;
}
