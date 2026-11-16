package br.com.glaucotodesco.jparelacionamentos.service;

import br.com.glaucotodesco.jparelacionamentos.dto.CategoriaResponse;
import br.com.glaucotodesco.jparelacionamentos.model.Categoria;
import br.com.glaucotodesco.jparelacionamentos.repository.CategoriaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoriaService {
    private final CategoriaRepository categorias;

    public CategoriaService(CategoriaRepository categorias) {
        this.categorias = categorias;
    }

    @Transactional
    public CategoriaResponse criar(String nome) {
        return CategoriaResponse.from(categorias.save(new Categoria(nome)));
    }

    public List<CategoriaResponse> listar() {
        return categorias.findAll(Sort.by("id")).stream().map(CategoriaResponse::from).toList();
    }

    public Categoria buscar(Long id) {
        return categorias.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public Categoria padrao() {
        return categorias.findFirstByNomeOrderByIdAsc("Geral")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Categoria Geral ausente"));
    }
}
