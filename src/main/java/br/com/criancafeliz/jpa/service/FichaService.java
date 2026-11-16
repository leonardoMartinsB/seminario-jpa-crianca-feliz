package br.com.criancafeliz.jpa.service;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.mapper.FichaMapper;
import br.com.criancafeliz.jpa.model.FichaSocioeconomica;
import br.com.criancafeliz.jpa.repository.FichaRepository;
import br.com.criancafeliz.jpa.repository.AtendidoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FichaService {
    private final FichaRepository repo;
    private final AtendidoRepository atendidos;

    public FichaService(FichaRepository repo, AtendidoRepository atendidos) {
        this.repo = repo;
        this.atendidos = atendidos;
    }

    public List<Saidas.Ficha> listar() {
        return repo.findAllByOrderByIdAsc().stream().map(FichaMapper::ficha).toList();
    }

    public Saidas.Ficha obter(long id) {
        return FichaMapper.ficha(buscar(id));
    }

    @Transactional
    public Saidas.Ficha criar(Entradas.Ficha entrada) {
        var atendido = atendidos.findById(entrada.atendidoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atendido informado não encontrado"));
        if (repo.existsByAtendidoId(atendido.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Atendido já possui ficha socioeconômica");
        }
        var item = new FichaSocioeconomica(atendido, entrada.rendaFamiliar(), entrada.pessoasNoDomicilio());
        return FichaMapper.ficha(repo.saveAndFlush(item));
    }

    @Transactional
    public Saidas.Ficha atualizar(long id, Entradas.Ficha entrada) {
        var item = buscar(id);
        if (!item.getAtendido().getId().equals(entrada.atendidoId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A ficha pertence ao atendido original");
        }
        item.atualizar(entrada.rendaFamiliar(), entrada.pessoasNoDomicilio());
        return FichaMapper.ficha(item);
    }

    @Transactional
    public void excluir(long id) {
        var item = buscar(id);
        repo.delete(item);
        repo.flush();
    }

    private FichaSocioeconomica buscar(long id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha socioeconômica não encontradoa"));
    }


}
