package pe.edu.tecsup.examen1.controller;

import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PutMapping("/{id}")
    public Usuario modificarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        return usuarioService.modificarUsuario(id, usuario);
    }

    @PatchMapping("/{id}/estado")
    public Usuario cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo) {

        return usuarioService.cambiarEstado(id, activo);
    }
}