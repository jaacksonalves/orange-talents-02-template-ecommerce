package br.com.zup.mercadolivre.fechamentocompra;

import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
public class FechaCompraParte1Controller {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @PostMapping("/compras")
    public String cria(@Valid @RequestBody CompraForm form,
                       @AuthenticationPrincipal UsuarioLogado usuarioLogado,
                       UriComponentsBuilder uri) throws BindException {
        Produto produtoComprado = em.find(Produto.class, form.getIdProduto());
        int quantidade = form.getQuantidade();
        Usuario comprador = usuarioLogado.get();
        GatewayPagamento gatewayPagamento = form.getGatewayPagamento();

        boolean abateu = produtoComprado.abateEstoque(quantidade);

        if (abateu) {
            Compra novaCompra = new Compra(produtoComprado, quantidade, comprador, gatewayPagamento);
            em.persist(novaCompra);
            if (gatewayPagamento.equals(GatewayPagamento.PAGSEGURO)) {
                String urlPagseguro = uri.path("/retorno-pagseguro").buildAndExpand(novaCompra.getId()).toString();
                return "paypal.com" + novaCompra.getId() + "?redirectUrl=" + urlPagseguro;
            } else {

                String urlPaypal = uri.path("/retorno-paypal").buildAndExpand(novaCompra.getId()).toString();
                return "paypal.com" + novaCompra.getId() + "?redirectUrl=" + urlPaypal;
            }
        }


        BindException problemaComEstoque = new BindException(form, "compraForm");
        problemaComEstoque.reject(null, "Quantidade maior do que a que existe em estoque");
        throw problemaComEstoque;
    }


}

