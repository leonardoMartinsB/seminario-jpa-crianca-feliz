package br.com.criancafeliz.jpa.repository;

import br.com.criancafeliz.jpa.model.Atendido;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AtendidoRepository extends JpaRepository<Atendido, Long> {
    boolean existsByResponsavelId(Long id);

    @EntityGraph(attributePaths = "responsavel")
    List<Atendido> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "responsavel")
    List<Atendido> findByResponsavelIdOrderByIdAsc(Long id);
}
