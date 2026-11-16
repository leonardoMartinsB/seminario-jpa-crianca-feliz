package br.com.criancafeliz.jpa.service;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.mapper.FichaMapper;
import br.com.criancafeliz.jpa.model.Atendido;
import br.com.criancafeliz.jpa.model.Responsavel;
import br.com.criancafeliz.jpa.repository.AtendidoRepository;
import br.com.criancafeliz.jpa.repository.ResponsavelRepository;
import br.com.criancafeliz.jpa.repository.FichaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AtendidoService {
    private final AtendidoRepository repo;
    private final ResponsavelRepository responsaveis;
    private final FichaRepository fichas;

    public AtendidoService(AtendidoRepository repo, ResponsavelRepository responsaveis, FichaRepository fichas) {
        this.repo = repo;
        this.responsaveis = responsaveis;
        this.fichas = fichas;
    }

    public List<Saidas.Atendido> listar() {
        return repo.findAllByOrderByIdAsc().stream().map(FichaMapper::atendido).toList();
    }

    public Saidas.Atendido obter(long id) {
        return FichaMapper.atendido(buscar(id));
    }

    @Transactional
    public Saidas.Atendido criar(Entradas.Atendido entrada) {
        var item = new Atendido(entrada.nome(), entrada.dataNascimento(), responsavel(entrada.responsavelId()));
        return FichaMapper.atendido(repo.saveAndFlush(item));
    }

    @Transactional
    public Saidas.Atendido atualizar(long id, Entradas.Atendido entrada) {
        var item = buscar(id);
        item.atualizar(entrada.nome(), entrada.dataNascimento(), responsavel(entrada.responsavelId()));
        return FichaMapper.atendido(item);
    }

    @Transactional
    public void excluir(long id) {
        var item = buscar(id);
        if (fichas.existsByAtendidoId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Exclua a ficha socioeconômica antes do atendido");
        repo.delete(item);
        repo.flush();
    }

    private Atendido buscar(long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendido não encontrado"));
    }

    private Responsavel responsavel(Long id) {
        return id == null ? null : responsaveis.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Responsável informado não encontrado"));
    }

    public Saidas.Ficha ficha(long id) {
        buscar(id);
        return fichas.findByAtendidoId(id).map(FichaMapper::ficha)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendido sem ficha socioeconômica"));
    }
}
