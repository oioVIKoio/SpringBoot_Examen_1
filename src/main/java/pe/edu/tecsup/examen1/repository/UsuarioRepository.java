package pe.edu.tecsup.examen1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.tecsup.examen1.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}