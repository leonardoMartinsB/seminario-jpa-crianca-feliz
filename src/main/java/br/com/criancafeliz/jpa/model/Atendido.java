package br.com.criancafeliz.jpa.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "atendidos")
public class Atendido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", foreignKey = @ForeignKey(name = "fk_atendido_responsavel"))
    private Responsavel responsavel;

    protected Atendido() {}

    public Atendido(String nome, LocalDate dataNascimento, Responsavel responsavel) {
        atualizar(nome, dataNascimento, responsavel);
    }

    public void atualizar(String nome, LocalDate dataNascimento, Responsavel responsavel) {
        this.nome = nome.strip();
        this.dataNascimento = dataNascimento;
        this.responsavel = responsavel;
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }

    public LocalDate getDataNascimento() { return dataNascimento; }

    public Responsavel getResponsavel() { return responsavel; }
}
