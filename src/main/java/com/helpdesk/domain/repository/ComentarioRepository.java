package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.Comentario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para {@link Comentario}.
 */
public interface ComentarioRepository {

    /**
     * Persiste o actualiza un comentario.
     */
    Comentario save(Comentario comentario);

    /**
     * Busca un comentario por identificador.
     */
    Optional<Comentario> findById(Long id);

    /**
     * Lista todos los comentarios de una incidencia.
     */
    List<Comentario> findByIncidenciaId(Long incidenciaId);

    /**
     * Lista comentarios visibles para el cliente de una incidencia.
     */
    List<Comentario> findByIncidenciaIdAndVisibleParaClienteTrue(Long incidenciaId);

    /**
     * Elimina un comentario por identificador.
     */
    void deleteById(Long id);
}
