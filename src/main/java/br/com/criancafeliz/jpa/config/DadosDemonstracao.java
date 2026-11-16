package br.com.criancafeliz.jpa.config;

import br.com.criancafeliz.jpa.dto.Entradas;
import br.com.criancafeliz.jpa.service.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DadosDemonstracao {
    @Bean
    ApplicationRunner dadosIniciais(ResponsavelService responsaveis, AtendidoService atendidos, FichaService fichas) {
        return args -> {
            var responsavel = responsaveis.criar(new Entradas.Responsavel("Responsável Demo", null));
            var alice = atendidos.criar(new Entradas.Atendido("Alice Demo", LocalDate.of(2015, 4, 10), responsavel.id()));
            atendidos.criar(new Entradas.Atendido("Bruno Demo", LocalDate.of(2016, 9, 20), responsavel.id()));
            atendidos.criar(new Entradas.Atendido("Caio Demo", LocalDate.of(2018, 1, 12), null));
            fichas.criar(new Entradas.Ficha(alice.id(), new BigDecimal("1500.00"), 3));
        };
    }
}
