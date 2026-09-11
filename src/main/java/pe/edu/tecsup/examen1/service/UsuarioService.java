package pe.edu.tecsup.examen1.service;

import org.springframework.stereotype.Service;
import pe.edu.tecsup.examen1.entity.Usuario;
import pe.edu.tecsup.examen1.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public java.util.List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario modificarUsuario(Long id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombres(datos.getNombres());
        usuario.setApellidos(datos.getApellidos());
        usuario.setDni(datos.getDni());
        usuario.setCorreo(datos.getCorreo());
        usuario.setTelefono(datos.getTelefono());
        usuario.setUsuario(datos.getUsuario());
        usuario.setPassword(datos.getPassword());
        usuario.setArea(datos.getArea());

        return usuarioRepository.save(usuario);
    }

    public Usuario cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setActivo(activo);

        return usuarioRepository.save(usuario);
    }


}