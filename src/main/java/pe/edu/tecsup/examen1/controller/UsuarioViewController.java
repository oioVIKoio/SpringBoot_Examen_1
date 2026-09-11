package pe.edu.tecsup.examen1.controller;

import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.service.UsuarioService;
import pe.edu.tecsup.examen1.service.RolService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final RolService rolService;




    private static final String REDIRECT_USUARIOS = "redirect:/usuarios";
    private final UsuarioService usuarioService;

    public UsuarioViewController(
            UsuarioService usuarioService,
            RolService rolService) {

        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }
    @GetMapping("/roles/{id}")
    public String asignarRoles(
            @PathVariable Long id,
            Model model) {

        Usuario usuario = usuarioService.buscarPorId(id);

        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolService.listarTodos());

        return "usuarios/roles";
    }
    @PostMapping("/roles/guardar")
    public String guardarRoles(
            @RequestParam Long usuarioId,
            @RequestParam(required = false) Set<Long> rolesIds) {

        usuarioService.asignarRoles(
                usuarioId,
                rolesIds != null ? rolesIds : Set.of()
        );

        return REDIRECT_USUARIOS;
    }
    @GetMapping
    public String listarUsuarios(
            @RequestParam(required = false) String termino,
            Model model) {

        model.addAttribute("usuarios", usuarioService.buscarUsuarios(termino));
        model.addAttribute("termino", termino);

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

        return REDIRECT_USUARIOS;
    }
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, false);
        return REDIRECT_USUARIOS;
    }

    @GetMapping("/activar/{id}")
    public String activarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, true);
        return REDIRECT_USUARIOS;
    }
}