package pe.edu.tecsup.examen1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.tecsup.examen1.entity.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsuarioOrCorreo(String usuario, String correo);
}