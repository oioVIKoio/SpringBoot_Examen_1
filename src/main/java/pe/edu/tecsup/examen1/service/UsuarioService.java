package pe.edu.tecsup.examen1.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.examen1.entity.TokenRecuperacion;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.repository.RolRepository;
import pe.edu.tecsup.examen1.repository.TokenRecuperacionRepository;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import pe.edu.tecsup.examen1.entity.Rol;
import java.util.HashSet;
import java.util.Set;
@Service
public class UsuarioService {

    private static final String MSG_USUARIO_NO_ENCONTRADO = "Usuario no encontrado con ID: ";
    private final RolRepository rolRepository;

    private final UsuarioRepository usuarioRepository;
    private final TokenRecuperacionRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public UsuarioService(RolRepository rolRepository, UsuarioRepository usuarioRepository,
                          TokenRecuperacionRepository tokenRepository,
                          PasswordEncoder passwordEncoder,
                          Clock clock) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }
    public Usuario asignarRoles(Long usuarioId, Set<Long> rolesIds) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException(MSG_USUARIO_NO_ENCONTRADO + usuarioId));

        List<Rol> roles = rolRepository.findAllById(rolesIds);

        if (roles.size() != rolesIds.size()) {
            throw new RuntimeException("Uno o más roles no existen");
        }

        usuario.setRoles(new HashSet<>(roles));

        return usuarioRepository.save(usuario);
    }
    public Usuario registrarUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
    public List<Usuario> buscarUsuarios(String termino) {
        if (termino == null || termino.isBlank()) {
            return usuarioRepository.findAll();
        }

        String texto = termino.trim();

        return usuarioRepository
                .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDniContainingOrCorreoContainingIgnoreCaseOrUsuarioContainingIgnoreCase(
                        texto, texto, texto, texto, texto);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MSG_USUARIO_NO_ENCONTRADO + id));
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
        }).orElseThrow(() -> new RuntimeException(MSG_USUARIO_NO_ENCONTRADO + id));
    }

    public Usuario cambiarEstado(Long id, boolean activo) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(activo);
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException(MSG_USUARIO_NO_ENCONTRADO + id));
    }

    public Optional<Usuario> buscarPorUsuarioOCorreo(String termino) {
        return usuarioRepository.findByUsuarioOrCorreo(termino, termino);
    }

    public String crearTokenRecuperacion(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        TokenRecuperacion tokenEntity = new TokenRecuperacion(
                token,
                usuario,
                LocalDateTime.now(clock).plusMinutes(15)
        );
        tokenRepository.save(tokenEntity);
        return token;
    }

    public boolean validarToken(String token) {
        Optional<TokenRecuperacion> tokenOpt = tokenRepository.findByToken(token);
        return tokenOpt.isPresent() && tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now(clock));
    }

    public boolean cambiarPasswordConToken(String token, String nuevaPassword) {
        Optional<TokenRecuperacion> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isPresent() && tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now(clock))) {
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