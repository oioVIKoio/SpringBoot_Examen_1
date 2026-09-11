package pe.edu.tecsup.examen1.controller;


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
    public java.util.List<Usuario> listarUsuarios() {
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