package pe.edu.tecsup.examen1.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.examen1.entity.TokenRecuperacion;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.repository.TokenRecuperacionRepository;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TokenRecuperacionRepository tokenRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Métodos para el CRUD de Usuarios ---

    public Usuario registrarUsuario(Usuario usuario) {
        // Encriptar la contraseña antes de guardar
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario modificarUsuario(Long id, Usuario datosNuevos) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombres(datosNuevos.getNombres());
            usuario.setApellidos(datosNuevos.getApellidos());
            usuario.setDni(datosNuevos.getDni());
            usuario.setCorreo(datosNuevos.getCorreo());
            usuario.setTelefono(datosNuevos.getTelefono());
            usuario.setUsuario(datosNuevos.getUsuario());
            usuario.setArea(datosNuevos.getArea());
            if (datosNuevos.getPassword() != null && !datosNuevos.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
            }
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario cambiarEstado(Long id, boolean activo) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(activo);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    // --- Métodos de Autenticación y Recuperación ---

    public Optional<Usuario> buscarPorUsuarioOCorreo(String termino) {
        return usuarioRepository.findByUsuarioOrCorreo(termino, termino);
    }

    public String crearTokenRecuperacion(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        TokenRecuperacion tokenEntity = new TokenRecuperacion(
                token,
                usuario,
                LocalDateTime.now().plusMinutes(15)
        );
        tokenRepository.save(tokenEntity);
        return token;
    }

    public boolean validarToken(String token) {
        Optional<TokenRecuperacion> tokenOpt = tokenRepository.findByToken(token);
        return tokenOpt.isPresent() && tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now());
    }

    public boolean cambiarPasswordConToken(String token, String nuevaPassword) {
        Optional<TokenRecuperacion> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isPresent() && tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now())) {
            TokenRecuperacion tokenEntity = tokenOpt.get();
            Usuario usuario = tokenEntity.getUsuario();

            usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            usuarioRepository.save(usuario);

            tokenRepository.delete(tokenEntity);
            return true;
        }
        return false;
    }
}