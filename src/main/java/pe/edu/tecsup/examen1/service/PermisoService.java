package pe.edu.tecsup.examen1.service;

import org.springframework.stereotype.Service;
import pe.edu.tecsup.examen1.entity.Permiso;
import pe.edu.tecsup.examen1.repository.PermisoRepository;

import java.util.List;

@Service
public class PermisoService {

    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    public List<Permiso> listar() {
        return permisoRepository.findAll();
    }

    public Permiso buscarPorId(Long id) {
        return permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
    }

    public Permiso guardar(Permiso permiso) {
        return permisoRepository.save(permiso);
    }
}