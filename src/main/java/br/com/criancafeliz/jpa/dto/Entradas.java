package br.com.criancafeliz.jpa.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class Entradas {
    private Entradas() {}

    public record Responsavel(
            @NotBlank @Size(max = 120) String nome,
            @Size(max = 25) @Pattern(regexp = "[0-9 ()+.-]*", message = "Informe um telefone válido") String telefone) {}

    public record Atendido(
            @NotBlank @Size(max = 120) String nome,
            @NotNull @PastOrPresent LocalDate dataNascimento,
            @Positive Long responsavelId) {}

    public record Ficha(
            @NotNull @Positive Long atendidoId,
            @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal rendaFamiliar,
            @NotNull @Positive Integer pessoasNoDomicilio) {}
}
