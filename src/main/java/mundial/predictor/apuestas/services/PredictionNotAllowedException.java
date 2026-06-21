package mundial.predictor.apuestas.services;

/**
 * Se lanza cuando un usuario intenta crear/actualizar una predicción de
 * un partido que ya está bloqueado (is_locked=1 o dentro de la 1 hora
 * previa al kickoff). El controller la traduce a HTTP 409.
 */
public class PredictionNotAllowedException extends RuntimeException {
    public PredictionNotAllowedException(String message) {
        super(message);
    }
}
