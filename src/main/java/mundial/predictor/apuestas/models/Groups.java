package mundial.predictor.apuestas.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class Groups {
    @Getter
    @Setter
    @JsonProperty("group_id")
    private int groupId;

    @Getter
    @Setter
    @JsonProperty("tournament_id")
    private int tournamentId;

    @Getter
    @Setter
    @JsonProperty("name")
    private String name;
}
