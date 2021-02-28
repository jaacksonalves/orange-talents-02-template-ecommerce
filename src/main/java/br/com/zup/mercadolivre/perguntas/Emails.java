package br.com.zup.mercadolivre.perguntas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Service
public class Emails {

    @Autowired
    private Mailer mailer;


    public void novaPergunta(@NotNull @Valid Pergunta pergunta) {
        System.out.println("novaPergunta");

        mailer.send("<html>...</html>", "nova pergunta...", "nome", pergunta.getUsuario().getEmail(),
                pergunta.getProduto().getCriador().getEmail());
    }
}
