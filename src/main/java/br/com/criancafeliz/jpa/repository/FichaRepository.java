package br.com.criancafeliz.jpa.repository;

import br.com.criancafeliz.jpa.model.FichaSocioeconomica;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FichaRepository extends JpaRepository<FichaSocioeconomica, Long> {
    boolean existsByAtendidoId(Long id);

    @EntityGraph(attributePaths = "atendido")
    List<FichaSocioeconomica> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "atendido")
    Optional<FichaSocioeconomica> findByAtendidoId(Long id);
}
