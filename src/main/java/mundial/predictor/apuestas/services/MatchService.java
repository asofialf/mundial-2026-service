package mundial.predictor.apuestas.services;

import mundial.predictor.apuestas.repository.IMatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MatchService {
    @Autowired
    private IMatchRepository matchRepository;

    public Map<String, Object> createMatch(int homeTeamId, int awayTeamId, String kickOffTime, int matchStatusId, int matchStageId, String opensAt, String closesAt, int isLocked){
        return matchRepository.createMatch(homeTeamId, awayTeamId, kickOffTime, matchStatusId, matchStageId, opensAt, closesAt, isLocked);
    }
    public Map<String, Object> updateMatchResult(int matchId, int homeScore, int awayScore, int penalties, int winnerTeamId, int matchStatusId, int isLocked){
        return matchRepository.updateMatchResult(matchId, homeScore, awayScore, penalties, winnerTeamId, matchStatusId, isLocked);
    }
    public List<Map<String, Object>> getMatches(){
        return matchRepository.getMatches();
    }
    public Map<String, Object> getMatchById(int matchId){
        return matchRepository.getMatchById(matchId);
    }
    public List<Map<String, Object>> getMatchesByHomeTeam(int countryId){
        return matchRepository.getMatchesByHomeTeam(countryId);
    }
    public List<Map<String, Object>> getMatchesByAwayTeam(int countryId){
        return matchRepository.getMatchesByAwayTeam(countryId);
    }
    public List<Map<String, Object>> getMatchesByDate(String date){
        return matchRepository.getMatchesByDate(date);
    }
    public List<Map<String, Object>> getMatchesUnlocked(){
        return matchRepository.getMatchesUnlocked();
    }
    public List<Map<String, Object>> getMatchesLocked(){
        return matchRepository.getMatchesLocked();
    }
    public Map<String, Object> unlockMatch(int matchId){
        return matchRepository.unlockMatch(matchId);
    }
    public Map<String, Object> lockMatch(int matchId){
        return matchRepository.lockMatch(matchId);
    }
}
