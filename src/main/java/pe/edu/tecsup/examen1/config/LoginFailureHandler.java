package pe.edu.tecsup.examen1.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private static final int MAX_INTENTOS = 3;
    private static final int MINUTOS_BLOQUEO = 5;

    private final UsuarioRepository usuarioRepository;

    public LoginFailureHandler(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof LockedException) {
            response.sendRedirect("/login?bloqueado");
            return;
        }

        String termino = request.getParameter("username");

        Optional<Usuario> usuarioOpt =
                usuarioRepository.findByUsuarioOrCorreo(termino, termino);

        if (usuarioOpt.isPresent()) {

            Usuario usuario = usuarioOpt.get();

            int intentos = usuario.getIntentosFallidos() == null
                    ? 0
                    : usuario.getIntentosFallidos();

            intentos++;
            usuario.setIntentosFallidos(intentos);

            if (intentos >= MAX_INTENTOS) {

                usuario.setBloqueadoHasta(
                        LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO)
                );

                usuario.setIntentosFallidos(0);
                usuarioRepository.save(usuario);

                response.sendRedirect("/login?bloqueado");
                return;
            }

            usuarioRepository.save(usuario);

            response.sendRedirect("/login?error");

            usuarioRepository.save(usuario);
        }

        response.sendRedirect("/login?error");
    }
}