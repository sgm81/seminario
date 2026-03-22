package com.tuapp.usuarios; // <-- cambia el paquete a tu proyecto

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // READ ALL
    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        // opcional: evitar que venga con id seteado
        // usuario.setId(null);

        Usuario creado = usuarioRepository.save(usuario);
        return ResponseEntity
                .created(URI.create("/api/usuarios/" + creado.getId()))
                .body(creado);
    }

    // UPDATE (reemplazo completo)
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return usuarioRepository.findById(id)
                .map(existing -> {
                    usuario.setId(id); // requiere setId en Usuario
                    Usuario actualizado = usuarioRepository.save(usuario);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
