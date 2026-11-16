package br.com.criancafeliz.jpa.service;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.mapper.FichaMapper;
import br.com.criancafeliz.jpa.model.Responsavel;
import br.com.criancafeliz.jpa.repository.ResponsavelRepository;
import br.com.criancafeliz.jpa.repository.AtendidoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ResponsavelService {
    private final ResponsavelRepository repo;
    private final AtendidoRepository atendidos;

    public ResponsavelService(ResponsavelRepository repo, AtendidoRepository atendidos) {
        this.repo = repo;
        this.atendidos = atendidos;
    }

    public List<Saidas.Responsavel> listar() {
        return repo.findAll(Sort.by("id")).stream().map(FichaMapper::responsavel).toList();
    }

    public Saidas.Responsavel obter(long id) {
        return FichaMapper.responsavel(buscar(id));
    }

    @Transactional
    public Saidas.Responsavel criar(Entradas.Responsavel entrada) {
        var item = new Responsavel(entrada.nome(), entrada.telefone());
        return FichaMapper.responsavel(repo.saveAndFlush(item));
    }

    @Transactional
    public Saidas.Responsavel atualizar(long id, Entradas.Responsavel entrada) {
        var item = buscar(id);
        item.atualizar(entrada.nome(), entrada.telefone());
        return FichaMapper.responsavel(item);
    }

    @Transactional
    public void excluir(long id) {
        var item = buscar(id);
        if (atendidos.existsByResponsavelId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Desvincule os atendidos antes da exclusão");
        repo.delete(item);
        repo.flush();
    }

    private Responsavel buscar(long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável não encontrado"));
    }

    public List<Saidas.Atendido> atendidos(long id) {
        buscar(id);
        return atendidos.findByResponsavelIdOrderByIdAsc(id).stream().map(FichaMapper::atendido).toList();
    }
}
