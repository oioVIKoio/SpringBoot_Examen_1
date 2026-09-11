package pe.edu.tecsup.examen1.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.tecsup.examen1.entity.Rol;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.repository.RolRepository;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializarDatos(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Crear rol ADMIN si no existe
            Rol rolAdmin = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> {
                        Rol rol = new Rol();
                        rol.setNombre("ADMIN");
                        rol.setDescripcion("Administrador del sistema");
                        return rolRepository.save(rol);
                    });

            // Crear usuario admin si no existe
            Usuario admin = usuarioRepository
                    .findByUsuarioOrCorreo("admin", "admin@tecsup.edu.pe")
                    .orElseGet(() -> {
                        Usuario usuario = new Usuario();
                        usuario.setNombres("Administrador");
                        usuario.setApellidos("Sistema");
                        usuario.setDni("00000001");
                        usuario.setCorreo("admin@tecsup.edu.pe");
                        usuario.setTelefono("");
                        usuario.setUsuario("admin");
                        usuario.setArea("Administración");
                        return usuario;
                    });

            // Datos de acceso conocidos para pruebas
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setActivo(true);
            admin.getRoles().add(rolAdmin);

            usuarioRepository.save(admin);

            System.out.println("==========================================");
            System.out.println(" ADMIN INICIALIZADO");
            System.out.println(" Usuario: admin");
            System.out.println(" Password: admin");
            System.out.println(" Rol: ADMIN");
            System.out.println("==========================================");
        };
    }
}