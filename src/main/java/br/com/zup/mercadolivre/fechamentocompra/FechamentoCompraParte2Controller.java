package br.com.zup.mercadolivre.fechamentocompra;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
public class FechamentoCompraParte2Controller {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private EventosNovaCompra eventosNovaCompra;

    @Transactional
    @PostMapping("/retorno-pagseguro/{id}")
    public String processametnoPagseguro(@PathVariable("id") Long idCompra,
                                         @Valid RetornoPagseguroForm form) {

        return processa(idCompra, form);
    }


    @Transactional
    @PostMapping("/retorno-paypal/{id}")
    public String processametnoPaypal(@PathVariable("id") Long idCompra,
                                      @Valid RetornoPaypalForm form) {
        return processa(idCompra, form);

    }


    private String processa(Long idCompra, RetornoGatewayPagamento retornoGatewayPagamento) {
        Compra compra = em.find(Compra.class, idCompra);
        compra.adicionaTransacao(retornoGatewayPagamento);
        em.merge(compra);

        eventosNovaCompra.processa(compra);

        return compra.toString();
    }

}
