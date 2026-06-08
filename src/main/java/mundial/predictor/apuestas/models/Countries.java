package mundial.predictor.apuestas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class Countries {

    @Getter
    @Setter
    @JsonProperty("country_id")
    private int countryId;

    @Getter
    @Setter
    @JsonProperty("name")
    private String name;

    @Getter
    @Setter
    @JsonProperty("fifa_code")
    private String fifaCode;

    @Getter
    @Setter
    @JsonProperty("image")
    private String image;
}
