package pe.edu.tecsup.examen1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final UsuarioService usuarioService;

    public UsuarioViewController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoUsuario(Model model) {
        model.addAttribute("usuarioForm", new Usuario());
        return "usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {

        Usuario usuario = usuarioService.buscarPorId(id);

        model.addAttribute("usuarioForm", usuario);

        return "usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute("usuarioForm") Usuario usuario) {

        if (usuario.getId() == null) {
            usuarioService.registrarUsuario(usuario);
        } else {
            usuarioService.modificarUsuario(usuario.getId(), usuario);
        }

        return "redirect:/usuarios";
    }
}