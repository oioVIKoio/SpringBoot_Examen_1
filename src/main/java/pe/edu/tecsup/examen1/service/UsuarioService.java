package pe.edu.tecsup.examen1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.tecsup.examen1.entity.TokenRecuperacion;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.repository.TokenRecuperacionRepository;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRecuperacionRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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