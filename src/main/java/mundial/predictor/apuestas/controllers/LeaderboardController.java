package mundial.predictor.apuestas.controllers;

import mundial.predictor.apuestas.services.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leaderboard")
public class LeaderboardController {
    @Autowired
    private LeaderboardService leaderboardService;

    /**
     ** GET /api/leaderboard/all
     ** Devuelve [{ userId, alias, totalPoints, rank }, ...] ordenado por
     ** totalPoints descendente. Ver LeaderboardService para las
     ** limitaciones del cálculo (standings de grupo derivados, no oficiales).
     **/
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getLeaderboard() {
        try {
            return ResponseEntity.ok(leaderboardService.getLeaderboard());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
