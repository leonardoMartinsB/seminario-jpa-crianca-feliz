package br.com.criancafeliz.jpa.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "responsaveis")
public class Responsavel {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 25)
    private String telefone;

    @OneToMany(mappedBy = "responsavel")
    private List<Atendido> atendidos = new ArrayList<>();

    protected Responsavel() {}

    public Responsavel(String nome, String telefone) {
        atualizar(nome, telefone);
    }

    public void atualizar(String nome, String telefone) {
        this.nome = nome.strip();
        this.telefone = telefone == null || telefone.isBlank() ? null : telefone.strip();
    }

    public Long getId() { return id; }

    public String getNome() { return nome; }

    public String getTelefone() { return telefone; }

    public List<Atendido> getAtendidos() { return List.copyOf(atendidos); }
}
