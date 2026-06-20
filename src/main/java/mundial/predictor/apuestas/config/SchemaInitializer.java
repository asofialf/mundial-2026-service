package mundial.predictor.apuestas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Crea, de forma idempotente, las tablas nuevas que necesita el MVP y que
 * no existían en el esquema original (app_settings, user_knockout_predictions).
 *
 * El proyecto no usa Flyway/Liquibase, así que esto corre en cada arranque
 * con "IF OBJECT_ID(...) IS NULL" para no fallar ni duplicar datos si la
 * tabla ya existe.
 *
 * Rollback manual (no automático): ver src/main/resources/db/rollback.sql
 */
@Component
public class SchemaInitializer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(SchemaInitializer.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) {
        createAppSettingsTable();
        createKnockoutPredictionsTable();
    }

    private void createAppSettingsTable() {
        jdbcTemplate.execute(
            "IF OBJECT_ID('dbo.app_settings', 'U') IS NULL " +
            "BEGIN " +
            "  CREATE TABLE app_settings (" +
            "    setting_key VARCHAR(100) NOT NULL PRIMARY KEY," +
            "    setting_value VARCHAR(50) NOT NULL" +
            "  );" +
            "  INSERT INTO app_settings (setting_key, setting_value) VALUES " +
            "    ('is_group_phase_active', '1')," +
            "    ('is_knockout_phase_active', '1')," +
            // knockout_stage_<matchStageId> -> debe coincidir con MatchStageId del frontend
            // (2=ROUND_OF_32, 3=ROUND_OF_16, 4=QUARTER, 5=SEMI, 6=FINAL)
            "    ('knockout_stage_2_active', '0')," +
            "    ('knockout_stage_3_active', '0')," +
            "    ('knockout_stage_4_active', '0')," +
            "    ('knockout_stage_5_active', '0')," +
            "    ('knockout_stage_6_active', '0');" +
            "END"
        );
        log.info("Verificada tabla app_settings");
    }

    private void createKnockoutPredictionsTable() {
        jdbcTemplate.execute(
            "IF OBJECT_ID('dbo.user_knockout_predictions', 'U') IS NULL " +
            "BEGIN " +
            "  CREATE TABLE user_knockout_predictions (" +
            "    prediction_id INT IDENTITY(1,1) PRIMARY KEY," +
            "    match_id INT NOT NULL," +
            "    user_id INT NOT NULL," +
            "    score_team_a INT NOT NULL," +
            "    score_team_b INT NOT NULL," +
            "    advancing_team_id INT NOT NULL," +
            "    has_penalties BIT NOT NULL," +
            "    CONSTRAINT UQ_knockout_match_user UNIQUE (match_id, user_id)" +
            "  );" +
            "END"
        );
        log.info("Verificada tabla user_knockout_predictions");
    }
}
