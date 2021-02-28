package br.com.zup.mercadolivre.outros;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutrosSistemasController {

    @PostMapping(value = "/notas-fiscais")
    public void criaNota(@Valid @RequestBody NovaCompraNFForm form) throws InterruptedException {
        System.out.println("criando nota "+form);
        Thread.sleep(150);
    }

    @PostMapping(value = "/ranking")
    public void ranking(@Valid @RequestBody RankingNovaCompraForm form) throws InterruptedException {
        System.out.println("criando ranking"+form);
        Thread.sleep(150);
    }

}
