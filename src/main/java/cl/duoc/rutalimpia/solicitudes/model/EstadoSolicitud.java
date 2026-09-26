package cl.duoc.rutalimpia.solicitudes.model;

/**
 * Ciclo de vida de una solicitud de retiro.
 * RECIBIDA  -> se registró y ya tiene folio (todavía sin camión)
 * ASIGNADA  -> la Lambda encontró camión y rutas-service la agregó a la hoja de ruta
 * REALIZADA / FALLIDA -> resultado informado por el conductor
 */
public enum EstadoSolicitud {
    RECIBIDA,
    ASIGNADA,
    REALIZADA,
    FALLIDA
}
