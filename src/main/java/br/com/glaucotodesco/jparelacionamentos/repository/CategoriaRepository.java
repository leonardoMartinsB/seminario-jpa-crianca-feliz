package br.com.glaucotodesco.jparelacionamentos.repository;

import br.com.glaucotodesco.jparelacionamentos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findFirstByNomeOrderByIdAsc(String nome);
}
