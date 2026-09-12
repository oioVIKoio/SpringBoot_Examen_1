package pe.edu.tecsup.examen1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pe.edu.tecsup.examen1.service.AuditoriaService;

@Controller
public class AuditoriaViewController {

    private final AuditoriaService auditoriaService;

    public AuditoriaViewController(
            AuditoriaService auditoriaService) {

        this.auditoriaService = auditoriaService;
    }

    @GetMapping("/auditorias")
    public String listar(Model model) {

        model.addAttribute(
                "auditorias",
                auditoriaService.listarTodas()
        );

        return "auditorias/lista";
    }
}