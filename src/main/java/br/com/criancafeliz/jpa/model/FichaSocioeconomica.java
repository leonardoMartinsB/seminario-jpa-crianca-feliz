package br.com.criancafeliz.jpa.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "fichas_socioeconomicas", uniqueConstraints = @UniqueConstraint(name = "uk_ficha_atendido", columnNames = "atendido_id"))
public class FichaSocioeconomica {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atendido_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ficha_atendido"))
    private Atendido atendido;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal rendaFamiliar;

    @Column(nullable = false)
    private int pessoasNoDomicilio;

    protected FichaSocioeconomica() {}

    public FichaSocioeconomica(Atendido atendido, BigDecimal rendaFamiliar, int pessoasNoDomicilio) {
        this.atendido = atendido;
        atualizar(rendaFamiliar, pessoasNoDomicilio);
    }

    public void atualizar(BigDecimal rendaFamiliar, int pessoasNoDomicilio) {
        this.rendaFamiliar = rendaFamiliar;
        this.pessoasNoDomicilio = pessoasNoDomicilio;
    }

    public Long getId() { return id; }

    public Atendido getAtendido() { return atendido; }

    public BigDecimal getRendaFamiliar() { return rendaFamiliar; }

    public int getPessoasNoDomicilio() { return pessoasNoDomicilio; }
}
