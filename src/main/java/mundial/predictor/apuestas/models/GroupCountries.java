package mundial.predictor.apuestas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class GroupCountries {
    @Getter
    @Setter
    @JsonProperty("group_country_id")
    private int groupCountryId;

    @Getter
    @Setter
    @JsonProperty("group_id")
    private int groupId;

    @Getter
    @Setter
    @JsonProperty("country_id")
    private int countryId;
}
