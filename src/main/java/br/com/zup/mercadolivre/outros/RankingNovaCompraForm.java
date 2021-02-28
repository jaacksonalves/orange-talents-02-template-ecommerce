package br.com.zup.mercadolivre.outros;

import javax.validation.constraints.NotNull;

public class RankingNovaCompraForm {

    @NotNull
    private Long idCompra;
    @NotNull
    private Long idVendedor;

    public RankingNovaCompraForm(Long idCompra, Long idVendedor) {
        this.idCompra = idCompra;
        this.idVendedor = idVendedor;
    }

    @Override
    public String toString() {
        return "NovaCompraNFRequest [idCompra=" + idCompra + ", idComprador="
                + idVendedor + "]";
    }

}