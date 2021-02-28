package br.com.zup.mercadolivre.perguntas;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class FakeMailer implements Mailer {
    @Override
    public void send(String body, String subject, String nomeFrom, String from, String to) {
        System.out.println(body);
        System.out.println(subject);
        System.out.println(nomeFrom);
        System.out.println(from);
        System.out.println(to);

    }
}
