package br.com.zup.mercadolivre.fechamentocompra;

public interface RetornoGatewayPagamento {

    Transacao toTransacao(Compra compra);
}
