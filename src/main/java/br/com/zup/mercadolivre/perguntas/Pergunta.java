package br.com.zup.mercadolivre.perguntas;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "perguntas")
public class Pergunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private final LocalDateTime instanteCriacao = LocalDateTime.now();
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private Produto produto;

    public Pergunta(String titulo, Usuario usuario, Produto produto) {
        this.titulo = titulo;
        this.usuario = usuario;
        this.produto = produto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Produto getProduto() {
        return produto;
    }

    public String getTitulo() {
        return titulo;
    }

    @Deprecated
    public Pergunta() {
    }

    @Override
    public String toString() {
        return "Pergunta{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", instanteCriacao=" + instanteCriacao +
                ", usuario=" + usuario +
                ", produto=" + produto +
                '}';
    }
}
