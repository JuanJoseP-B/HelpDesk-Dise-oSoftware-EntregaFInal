package com.helpdesk.domain.repository;

import com.helpdesk.domain.entity.Usuario;
import com.helpdesk.domain.enums.RolUsuario;
import com.helpdesk.domain.pagination.Pagina;
import com.helpdesk.domain.pagination.Paginacion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de persistencia para {@link Usuario}.
 */
public interface UsuarioRepository {

    /**
     * Persiste o actualiza un usuario.
     */
    Usuario save(Usuario usuario);

    /**
     * Busca un usuario por identificador.
     */
    Optional<Usuario> findById(Long id);

    /**
     * Busca un usuario por correo electrónico.
     */
    Optional<Usuario> findByCorreoElectronico(String email);

    /**
     * Lista usuarios con el rol indicado.
     */
    List<Usuario> findByRol(RolUsuario rol);

    /**
     * Lista usuarios activos.
     */
    List<Usuario> findActivos();

    /**
     * Lista todos los usuarios de forma paginada.
     */
    Pagina<Usuario> findAll(Paginacion paginacion);

    /**
     * Indica si ya existe un usuario con el correo dado.
     */
    boolean existsByCorreoElectronico(String email);

    /**
     * Elimina un usuario por identificador.
     */
    void deleteById(Long id);
}
