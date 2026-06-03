package com.helpdesk.application.usecase;

import com.helpdesk.application.command.CrearAcuerdoServicioCommand;
import com.helpdesk.application.dto.response.AcuerdoServicioDTO;
import com.helpdesk.application.exception.ResourceNotFoundException;
import com.helpdesk.application.mapper.ApplicationMapper;
import com.helpdesk.domain.entity.AcuerdoServicio;
import com.helpdesk.domain.repository.AcuerdoServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Casos de uso de gestión de acuerdos de servicio (SLA).
 */
@Service
public class AcuerdoServicioUseCase {

    private final AcuerdoServicioRepository acuerdoServicioRepository;

    public AcuerdoServicioUseCase(AcuerdoServicioRepository acuerdoServicioRepository) {
        this.acuerdoServicioRepository = acuerdoServicioRepository;
    }

    /**
     * Crea un nuevo acuerdo de servicio activo.
     */
    @Transactional
    public AcuerdoServicioDTO crear(CrearAcuerdoServicioCommand command) {
        AcuerdoServicio acuerdo = new AcuerdoServicio(
                null,
                command.nombre(),
                command.descripcion(),
                command.nivelPrioridad(),
                command.tiempoMaxRespuestaHoras(),
                command.tiempoMaxResolucionHoras(),
                true,
                LocalDateTime.now()
        );
        return ApplicationMapper.toAcuerdoDto(acuerdoServicioRepository.save(acuerdo));
    }

    /**
     * Lista los acuerdos de servicio activos.
     */
    @Transactional(readOnly = true)
    public List<AcuerdoServicioDTO> listarActivos() {
        return acuerdoServicioRepository.findAllActivos().stream()
                .map(ApplicationMapper::toAcuerdoDto)
                .toList();
    }

    /**
     * Obtiene un acuerdo de servicio por identificador.
     */
    @Transactional(readOnly = true)
    public AcuerdoServicioDTO obtenerPorId(Long id) {
        return acuerdoServicioRepository.findById(id)
                .map(ApplicationMapper::toAcuerdoDto)
                .orElseThrow(() -> new ResourceNotFoundException("AcuerdoServicio", id));
    }

    /**
     * Actualiza un acuerdo de servicio existente.
     */
    @Transactional
    public AcuerdoServicioDTO actualizar(Long id, CrearAcuerdoServicioCommand command) {
        acuerdoServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcuerdoServicio", id));

        AcuerdoServicio acuerdo = new AcuerdoServicio(
                id,
                command.nombre(),
                command.descripcion(),
                command.nivelPrioridad(),
                command.tiempoMaxRespuestaHoras(),
                command.tiempoMaxResolucionHoras(),
                true,
                LocalDateTime.now()
        );
        return ApplicationMapper.toAcuerdoDto(acuerdoServicioRepository.save(acuerdo));
    }

    /**
     * Elimina un acuerdo de servicio por identificador.
     */
    @Transactional
    public void eliminar(Long id) {
        acuerdoServicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcuerdoServicio", id));
        acuerdoServicioRepository.deleteById(id);
    }
}
