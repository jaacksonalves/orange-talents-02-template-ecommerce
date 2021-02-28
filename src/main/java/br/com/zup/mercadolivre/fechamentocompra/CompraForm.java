package br.com.zup.mercadolivre.fechamentocompra;

import br.com.zup.mercadolivre.compartilhado.ExistsId;
import br.com.zup.mercadolivre.produtos.Produto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CompraForm {

    @Positive
    @NotNull
    private int quantidade;
    @NotNull
    @ExistsId(domainClass = Produto.class, fieldName = "id", message = "Produto não encontrado")
    private Long idProduto;
    @NotNull
    private GatewayPagamento gatewayPagamento;


    public GatewayPagamento getGatewayPagamento() {
        return gatewayPagamento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Long getIdProduto() {
        return idProduto;
    }
}
