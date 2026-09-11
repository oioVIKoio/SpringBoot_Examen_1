package pe.edu.tecsup.examen1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("msgError", "Usuario o contraseña incorrectos.");
        }
        if (logout != null) {
            model.addAttribute("msgSuccess", "Has cerrado sesión correctamente.");
        }
        return "login";
    }
}