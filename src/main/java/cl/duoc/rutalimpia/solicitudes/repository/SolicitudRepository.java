package cl.duoc.rutalimpia.solicitudes.repository;

import cl.duoc.rutalimpia.solicitudes.model.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    Optional<Solicitud> findByFolio(String folio);
}
