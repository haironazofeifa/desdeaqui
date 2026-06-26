package com.desdeaqui.repository;

import com.desdeaqui.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByCorreo(String correo);                        // para el login
    Usuario findByCorreoAndContrasena(String correo,
                                      String contrasena);       // validar credenciales
    boolean existsByCorreo(String correo);                      // evitar correos duplicados
}