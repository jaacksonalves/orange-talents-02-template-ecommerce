package br.com.zup.mercadolivre.opinioes;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class OpiniaoForm {

    @NotNull
    @Range(min = 1, max = 5, message = "Nota tem que ser de 1 a 5")
    private int nota;
    @NotBlank
    @Length(min = 3, message = "Título deve ter ao menos 3 caracteres")
    private String titulo;
    @NotBlank
    @Length(max = 500, message = "Descrição pode ter no máximo 500 caracteres")
    private String descricao;

    public OpiniaoForm(@NotNull @Range(min = 1, max = 5) int nota, @NotBlank @Length(min = 3) String titulo, @NotBlank @Length(max = 500) String descricao) {
        this.nota = nota;
        this.titulo = titulo;
        this.descricao = descricao;
    }


    public Opiniao toModel(Usuario usuario, Produto produto) {
        return new Opiniao(nota, titulo, descricao, usuario, produto);
    }
}
