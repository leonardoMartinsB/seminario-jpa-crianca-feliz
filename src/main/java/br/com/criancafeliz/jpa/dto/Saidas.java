package br.com.criancafeliz.jpa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Saidas {
    private Saidas() {}

    public record Responsavel(Long id, String nome, String telefone) {}

    public record Atendido(Long id, String nome, LocalDate dataNascimento, Resumo responsavel) {}

    public record Ficha(Long id, Resumo atendido, BigDecimal rendaFamiliar,
                        int pessoasNoDomicilio, BigDecimal rendaPorPessoa) {}

    public record Resumo(Long id, String nome) {}
}
