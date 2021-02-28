package br.com.zup.mercadolivre.produtos;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CaracteristicaProdutoForm {

    @NotBlank
    private String nome;
    @NotBlank
    private String descricao;

    public CaracteristicaProdutoForm(@NotBlank String nome, @NotBlank String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }


    public CaracteristicaProduto toModel(@NotNull @Valid Produto produto){
        return new CaracteristicaProduto(nome, descricao, produto);

    }
}
