package pe.edu.tecsup.examen1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.tecsup.examen1.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuarioOrCorreo(String usuario, String correo);

    List<Usuario> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDniContainingOrCorreoContainingIgnoreCaseOrUsuarioContainingIgnoreCase(
            String nombres,
            String apellidos,
            String dni,
            String correo,
            String usuario
    );

}