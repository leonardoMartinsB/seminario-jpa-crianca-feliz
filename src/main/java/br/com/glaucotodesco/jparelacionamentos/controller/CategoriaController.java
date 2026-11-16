package br.com.glaucotodesco.jparelacionamentos.controller;

import br.com.glaucotodesco.jparelacionamentos.dto.CategoriaResponse;
import br.com.glaucotodesco.jparelacionamentos.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaService categorias;

    public CategoriaController(CategoriaService categorias) {
        this.categorias = categorias;
    }

    public record Entrada(@NotBlank @Size(max = 100) String nome) {}

    @PostMapping
    public ResponseEntity<CategoriaResponse> criar(@Valid @RequestBody Entrada entrada) {
        var criada = categorias.criar(entrada.nome());
        return ResponseEntity.created(URI.create("/categorias/" + criada.id())).body(criada);
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categorias.listar();
    }

    @GetMapping("/{id}")
    public CategoriaResponse buscar(@PathVariable Long id) {
        return CategoriaResponse.from(categorias.buscar(id));
    }
}
