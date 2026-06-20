-- Rollback MANUAL de las tablas creadas por SchemaInitializer.java
-- Esto NO se ejecuta automáticamente en ningún arranque del backend.
-- Ejecutar a mano contra la base de datos solo si hay que revertir el cambio.
--
-- ADVERTENCIA: borra TODA la data de configuración de fases y de
-- predicciones de eliminatorias guardadas por los usuarios. No hay
-- forma de recuperarla después de ejecutar esto.

DROP TABLE IF EXISTS user_knockout_predictions;
DROP TABLE IF EXISTS app_settings;
