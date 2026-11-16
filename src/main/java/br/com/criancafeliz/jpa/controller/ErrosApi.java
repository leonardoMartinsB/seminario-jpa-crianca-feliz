package br.com.criancafeliz.jpa.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;

@RestControllerAdvice
public class ErrosApi {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validacao(MethodArgumentNotValidException erro) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Revise os campos informados");
        var campos = new LinkedHashMap<String, String>();
        erro.getBindingResult().getFieldErrors().forEach(campo -> campos.putIfAbsent(campo.getField(), campo.getDefaultMessage()));
        problem.setProperty("campos", campos);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ProblemDetail jsonInvalido() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "JSON inválido ou valores com tipo incorreto");
    }

    @ExceptionHandler(ResponseStatusException.class)
    ProblemDetail regra(ResponseStatusException erro) {
        return erro.getBody();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail integridade() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "A operação conflita com um vínculo já registrado");
    }
}
