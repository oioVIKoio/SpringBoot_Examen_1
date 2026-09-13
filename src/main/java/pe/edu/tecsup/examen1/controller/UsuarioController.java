package pe.edu.tecsup.examen1.controller;

import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.service.UsuarioService;

import java.util.List;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario registrarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }
    @GetMapping("/{id}")
    public Usuario obtenerUsuario(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
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
    @PutMapping("/{id}/roles")
    public Usuario asignarRoles(
            @PathVariable Long id,
            @RequestBody Set<Long> rolesIds) {

        return usuarioService.asignarRoles(id, rolesIds);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.cambiarEstado(id, false);
    }
}