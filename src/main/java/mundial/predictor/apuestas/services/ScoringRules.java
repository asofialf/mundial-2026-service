package mundial.predictor.apuestas.services;

/**
 * Reglas de puntaje del Leaderboard — debe coincidir con
 * scoring.config.ts del frontend. Edita estos números para cambiar
 * el sistema de puntos.
 */
public final class ScoringRules {
    private ScoringRules() {}

    // Fase de Grupos
    public static final int PRIMERO_Y_SEGUNDO_EXACTO = 3;
    public static final int EQUIPO_EN_TOP2_SIN_ORDEN  = 1;
    public static final int MEJOR_TERCERO_ACERTADO    = 1;

    // Eliminatorias
    public static final int MARCADOR_EXACTO       = 3;
    public static final int CLASIFICADO_ACERTADO  = 1;
    public static final int PENALES_ACERTADO      = 1;

    /** matchStageId de la fase de grupos (suposición — confirmar con backend real). */
    public static final int GROUP_STAGE_ID = 1;

    /** rol_id de administrador (tabla roles: 1=ADMIN, 2=USER) — excluido del leaderboard. */
    public static final int ADMIN_ROLE_ID = 1;
}
