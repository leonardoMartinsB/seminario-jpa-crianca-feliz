package br.com.criancafeliz.jpa.controller;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.service.FichaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fichas-socioeconomicas")
public class FichaController {
    private final FichaService service;

    public FichaController(FichaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Saidas.Ficha> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Saidas.Ficha obter(@PathVariable long id) {
        return service.obter(id);
    }

    @PostMapping
    public ResponseEntity<Saidas.Ficha> criar(@Valid @RequestBody Entradas.Ficha entrada) {
        var criado = service.criar(entrada);
        return ResponseEntity.created(URI.create("/api/fichas-socioeconomicas/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public Saidas.Ficha atualizar(@PathVariable long id, @Valid @RequestBody Entradas.Ficha entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
