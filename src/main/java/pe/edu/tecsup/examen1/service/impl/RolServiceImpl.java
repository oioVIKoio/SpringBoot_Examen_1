package pe.edu.tecsup.examen1.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.tecsup.examen1.entity.Permiso;
import pe.edu.tecsup.examen1.entity.Rol;
import pe.edu.tecsup.examen1.repository.PermisoRepository;
import pe.edu.tecsup.examen1.repository.RolRepository;
import pe.edu.tecsup.examen1.service.AuditoriaService;
import pe.edu.tecsup.examen1.service.RolService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final AuditoriaService auditoriaService;

    public RolServiceImpl(
            RolRepository rolRepository,
            PermisoRepository permisoRepository,
            AuditoriaService auditoriaService) {

        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
        this.auditoriaService = auditoriaService;
    }

    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Rol obtenerPorId(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rol no encontrado con ID: " + id
                        )
                );
    }

    @Override
    @Transactional
    public Rol guardarRol(Rol rol) {

        boolean nuevo = rol.getId() == null;

        Rol guardado = rolRepository.save(rol);

        auditoriaService.registrar(
                nuevo ? "CREAR_ROL" : "MODIFICAR_ROL",
                "ROLES",
                (nuevo
                        ? "Se creó el rol "
                        : "Se modificó el rol ")
                        + guardado.getNombre()
        );

        return guardado;
    }

    @Override
    @Transactional
    public Rol asignarPermisos(
            Long rolId,
            Set<Long> permisosIds) {

        Rol rol = obtenerPorId(rolId);

        List<Permiso> permisos =
                permisoRepository.findAllById(permisosIds);

        rol.setPermisos(
                new HashSet<>(permisos)
        );

        Rol actualizado =
                rolRepository.save(rol);

        auditoriaService.registrar(
                "ASIGNAR_PERMISOS",
                "ROLES",
                "Se modificaron los permisos del rol "
                        + actualizado.getNombre()
        );

        return actualizado;
    }

    @Override
    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }
}