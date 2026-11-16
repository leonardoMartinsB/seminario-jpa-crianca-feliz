package br.com.criancafeliz.jpa.controller;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.service.ResponsavelService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelController {
    private final ResponsavelService service;

    public ResponsavelController(ResponsavelService service) {
        this.service = service;
    }

    @GetMapping
    public List<Saidas.Responsavel> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Saidas.Responsavel obter(@PathVariable long id) {
        return service.obter(id);
    }

    @PostMapping
    public ResponseEntity<Saidas.Responsavel> criar(@Valid @RequestBody Entradas.Responsavel entrada) {
        var criado = service.criar(entrada);
        return ResponseEntity.created(URI.create("/api/responsaveis/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public Saidas.Responsavel atualizar(@PathVariable long id, @Valid @RequestBody Entradas.Responsavel entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/atendidos")
    public List<Saidas.Atendido> atendidos(@PathVariable long id) {
        return service.atendidos(id);
    }
}
