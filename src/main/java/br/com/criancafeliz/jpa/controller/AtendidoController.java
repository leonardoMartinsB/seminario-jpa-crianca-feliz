package br.com.criancafeliz.jpa.controller;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.service.AtendidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/atendidos")
public class AtendidoController {
    private final AtendidoService service;

    public AtendidoController(AtendidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Saidas.Atendido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Saidas.Atendido obter(@PathVariable long id) {
        return service.obter(id);
    }

    @PostMapping
    public ResponseEntity<Saidas.Atendido> criar(@Valid @RequestBody Entradas.Atendido entrada) {
        var criado = service.criar(entrada);
        return ResponseEntity.created(URI.create("/api/atendidos/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public Saidas.Atendido atualizar(@PathVariable long id, @Valid @RequestBody Entradas.Atendido entrada) {
        return service.atualizar(id, entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/ficha-socioeconomica")
    public Saidas.Ficha ficha(@PathVariable long id) {
        return service.ficha(id);
    }
}
