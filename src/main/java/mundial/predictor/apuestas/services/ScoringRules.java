package mundial.predictor.apuestas.services;

import java.util.Map;

/**
 * Reglas de puntaje del Leaderboard — debe coincidir con
 * scoring.config.ts del frontend. Edita estos números para cambiar
 * el sistema de puntos.
 *
 * Eliminatorias — puntaje BASE por partido (máximo 8 pts):
 *   Marcador exacto (90 min): 2 pts
 *   Equipo clasificado:        5 pts
 *   Penales sí/no:             1 pt
 * Luego se multiplica por el multiplicador de ronda y se redondea.
 */
public final class ScoringRules {
    private ScoringRules() {}

    // Fase de Grupos
    public static final int PRIMERO_Y_SEGUNDO_EXACTO = 3;
    public static final int EQUIPO_EN_TOP2_SIN_ORDEN  = 1;
    public static final int MEJOR_TERCERO_ACERTADO    = 1;

    // Eliminatorias — puntaje base
    public static final int MARCADOR_EXACTO       = 2;
    public static final int CLASIFICADO_ACERTADO  = 5;
    public static final int PENALES_ACERTADO      = 1;

    /**
     * Multiplicadores por ronda (matchStageId → factor).
     * El puntaje base acumulado en el partido se multiplica y se redondea.
     * IDs asumidos: Dieciseisavos=2, Octavos=3, Cuartos=4, Semifinal=5, Final=6.
     */
    public static final Map<Integer, Double> STAGE_MULTIPLIER = Map.of(
        2, 1.0,   // Dieciseisavos
        3, 1.5,   // Octavos
        4, 1.5,   // Cuartos
        5, 2.0,   // Semifinal
        6, 3.0    // Final
    );

    /** matchStageId de la fase de grupos. */
    public static final int GROUP_STAGE_ID = 1;

    /** rol_id de administrador (tabla roles: 1=ADMIN, 2=USER) — excluido del leaderboard. */
    public static final int ADMIN_ROLE_ID = 1;
}
