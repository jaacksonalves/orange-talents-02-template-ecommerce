package br.com.zup.mercadolivre.detalhesproduto;

import br.com.zup.mercadolivre.produtos.CaracteristicaProduto;

public class DetalhesProdutoCaracteristica {


    private final String nome;
    private final String descricao;

    public DetalhesProdutoCaracteristica(CaracteristicaProduto caracteristica) {
        this.nome = caracteristica.getNome();
        this.descricao = caracteristica.getDescricao();
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
