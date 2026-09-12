package pe.edu.tecsup.examen1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.examen1.entity.Permiso;
import pe.edu.tecsup.examen1.repository.PermisoRepository;
import pe.edu.tecsup.examen1.service.AuditoriaService;

@Controller
@RequestMapping("/permisos")
public class PermisoViewController {

    private final PermisoRepository permisoRepository;
    private final AuditoriaService auditoriaService;

    public PermisoViewController(
            PermisoRepository permisoRepository,
            AuditoriaService auditoriaService) {

        this.permisoRepository = permisoRepository;
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public String listarPermisos(Model model) {

        model.addAttribute(
                "permisos",
                permisoRepository.findAll()
        );

        return "permisos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoPermiso(Model model) {

        model.addAttribute(
                "permisoForm",
                new Permiso()
        );

        return "permisos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editarPermiso(
            @PathVariable Long id,
            Model model) {

        Permiso permiso =
                permisoRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Permiso no encontrado"
                                )
                        );

        model.addAttribute(
                "permisoForm",
                permiso
        );

        return "permisos/formulario";
    }

    @PostMapping("/guardar")
    public String guardarPermiso(
            @ModelAttribute("permisoForm") Permiso permiso) {

        boolean nuevo = permiso.getId() == null;

        Permiso guardado =
                permisoRepository.save(permiso);

        auditoriaService.registrar(
                nuevo
                        ? "CREAR_PERMISO"
                        : "MODIFICAR_PERMISO",
                "PERMISOS",
                (nuevo
                        ? "Se creó el permiso "
                        : "Se modificó el permiso ")
                        + guardado.getNombre()
        );

        return "redirect:/permisos";
    }
}