package pe.edu.tecsup.examen1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.service.UsuarioService;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Solo se aceptan datos personales: el id, los roles y el estado los maneja el administrador
    @InitBinder("usuarioForm")
    public void camposPermitidos(WebDataBinder binder) {
        binder.setAllowedFields(
                "nombres",
                "apellidos",
                "dni",
                "correo",
                "telefono",
                "usuario",
                "password"
        );
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioForm", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @ModelAttribute("usuarioForm") Usuario usuario,
            Model model) {

        try {

            usuarioService.registrarUsuario(usuario);

            return "redirect:/login?registrado";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());

            return "registro";
        }
    }
}
