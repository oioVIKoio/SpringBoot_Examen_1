package pe.edu.tecsup.examen1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.service.UsuarioService;

import java.util.Optional;

@Controller
public class RecuperacionPasswordController {

    private final UsuarioService usuarioService;

    public RecuperacionPasswordController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/recuperar-password")
    public String mostrarRecuperacion() {
        return "recuperar-password";
    }

    @PostMapping("/recuperar-password")
    public String solicitarRecuperacion(
            @RequestParam String correo,
            Model model) {

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorUsuarioOCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            model.addAttribute(
                    "error",
                    "No existe un usuario registrado con ese correo."
            );

            return "recuperar-password";
        }

        String token =
                usuarioService.crearTokenRecuperacion(usuarioOpt.get());

        String enlace =
                "/restablecer-password?token=" + token;

        model.addAttribute(
                "mensaje",
                "Se generó correctamente el enlace de recuperación."
        );

        model.addAttribute(
                "enlaceRecuperacion",
                enlace
        );

        return "recuperar-password";
    }

    @GetMapping("/restablecer-password")
    public String mostrarRestablecer(
            @RequestParam String token,
            Model model) {

        if (!usuarioService.validarToken(token)) {
            model.addAttribute(
                    "error",
                    "El enlace de recuperación es inválido o ha expirado."
            );

            return "restablecer-password";
        }

        model.addAttribute("token", token);

        return "restablecer-password";
    }

    @PostMapping("/restablecer-password")
    public String restablecerPassword(
            @RequestParam String token,
            @RequestParam String password,
            Model model) {

        boolean cambiado =
                usuarioService.cambiarPasswordConToken(
                        token,
                        password
                );

        if (!cambiado) {

            model.addAttribute(
                    "error",
                    "El enlace de recuperación es inválido o ha expirado."
            );

            model.addAttribute("token", token);

            return "restablecer-password";
        }

        return "redirect:/login?cambioExitoso";
    }
}