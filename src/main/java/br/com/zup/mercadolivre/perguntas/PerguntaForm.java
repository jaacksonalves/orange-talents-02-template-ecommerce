package br.com.zup.mercadolivre.perguntas;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PerguntaForm {

    @NotBlank(message = "Pergunta precisa ser preenchida")
    private String titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Pergunta toModel(@NotNull @Valid Produto produto, @NotNull @Valid Usuario usuario) {
        return new Pergunta(titulo, usuario, produto);

    }
}
