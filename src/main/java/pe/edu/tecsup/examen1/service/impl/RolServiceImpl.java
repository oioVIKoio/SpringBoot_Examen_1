package pe.edu.tecsup.examen1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.examen1.entity.Permiso;
import pe.edu.tecsup.examen1.entity.Rol;
import pe.edu.tecsup.examen1.repository.PermisoRepository;
import pe.edu.tecsup.examen1.repository.RolRepository;
import pe.edu.tecsup.examen1.service.RolService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    @Transactional
    public Rol asignarPermisos(Long rolId, Set<Long> permisosIds) {
        Rol rol = obtenerPorId(rolId);
        List<Permiso> permisos = permisoRepository.findAllById(permisosIds);
        rol.setPermisos(new HashSet<>(permisos));
        return rolRepository.save(rol);
    }

    @Override
    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }
}