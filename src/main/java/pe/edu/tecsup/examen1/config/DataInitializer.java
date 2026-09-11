package pe.edu.tecsup.examen1.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner fixAdminPassword(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            usuarioRepository.findByUsuarioOrCorreo("admin", "admin").ifPresent(usuario -> {
                usuario.setPassword(passwordEncoder.encode("admin"));
                usuario.setActivo(true);
                usuarioRepository.save(usuario);
                System.out.println(">>> CONTRASEÑA DE ADMIN RECONFIGURADA A 'admin' CON ÉXITO <<<");
            });
        };
    }
}