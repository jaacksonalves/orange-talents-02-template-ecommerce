package br.com.zup.mercadolivre.fechamentocompra;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

public class RetornoPaypalForm implements RetornoGatewayPagamento{

    @Range(min = 0, max = 1)
    private int status;
    @NotBlank
    private String idTransacao;


    public RetornoPaypalForm(@Range(min = 0, max = 1) int status, @NotBlank String idTransacao) {
        this.status = status;
        this.idTransacao = idTransacao;
    }

    public Transacao toTransacao(Compra compra) {
        StatusTransacao statusTransacao = this.status == 0 ? StatusTransacao.ERRO : StatusTransacao.SUCESSO;

        return new Transacao(statusTransacao, idTransacao, compra);
    }
}
