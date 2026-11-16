package br.com.criancafeliz.jpa.mapper;

import br.com.criancafeliz.jpa.dto.Saidas;
import br.com.criancafeliz.jpa.model.*;
import java.math.RoundingMode;
import java.math.BigDecimal;

public final class FichaMapper {
    private FichaMapper() {}

    public static Saidas.Responsavel responsavel(Responsavel item) {
        return new Saidas.Responsavel(item.getId(), item.getNome(), item.getTelefone());
    }

    public static Saidas.Atendido atendido(Atendido item) {
        var responsavel = item.getResponsavel();
        var resumo = responsavel == null ? null : new Saidas.Resumo(responsavel.getId(), responsavel.getNome());
        return new Saidas.Atendido(item.getId(), item.getNome(), item.getDataNascimento(), resumo);
    }

    public static Saidas.Ficha ficha(FichaSocioeconomica item) {
        var atendido = item.getAtendido();
        var rendaPorPessoa = item.getRendaFamiliar()
                .divide(BigDecimal.valueOf(item.getPessoasNoDomicilio()), 2, RoundingMode.HALF_UP);
        return new Saidas.Ficha(item.getId(), new Saidas.Resumo(atendido.getId(), atendido.getNome()),
                item.getRendaFamiliar(), item.getPessoasNoDomicilio(), rendaPorPessoa);
    }
}
