package pe.edu.tecsup.examen1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.tecsup.examen1.entity.Rol;
import pe.edu.tecsup.examen1.service.RolService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
public class RolRestController {

    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> listar() {
        return ResponseEntity.ok(rolService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<Rol> crear(@RequestBody Rol rol) {
        Rol nuevoRol = rolService.guardarRol(rol);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rol> actualizar(@PathVariable Long id, @RequestBody Rol rolDetalles) {
        Rol rol = rolService.obtenerPorId(id);
        rol.setNombre(rolDetalles.getNombre());
        rol.setDescripcion(rolDetalles.getDescripcion());
        Rol actualizado = rolService.guardarRol(rol);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/permisos")
    public ResponseEntity<Rol> asignarPermisos(@PathVariable Long id, @RequestBody Set<Long> permisosIds) {
        Rol rolActualizado = rolService.asignarPermisos(id, permisosIds);
        return ResponseEntity.ok(rolActualizado);
    }
}