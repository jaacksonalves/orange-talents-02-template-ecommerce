package br.com.zup.mercadolivre.perguntas;

import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Component
public interface Mailer {

    void send(@NotBlank String body, @NotBlank String subject, @NotBlank String nomeFrom, @NotBlank @Email String from, @NotBlank @Email String to);
}
