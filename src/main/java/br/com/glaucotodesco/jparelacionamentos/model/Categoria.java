package br.com.glaucotodesco.jparelacionamentos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<Curso> cursos = new ArrayList<>();

    protected Categoria() {}

    public Categoria(String nome) {
        this.nome = nome.strip();
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }
}
