package br.com.zup.mercadolivre.detalhesproduto;

import br.com.zup.mercadolivre.opinioes.Opinioes;
import br.com.zup.mercadolivre.perguntas.Pergunta;
import br.com.zup.mercadolivre.produtos.ImagemProduto;
import br.com.zup.mercadolivre.produtos.Produto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public class DetalhesProdutoDto {

    private String descricao;
    private String nome;
    private BigDecimal preco;
    private Set<DetalhesProdutoCaracteristica> caracteristicas;
    private Set<String> linksImagens;
    private Set<String> perguntas;
    private Set<Map<String, String>> opinioes;
    private double mediaNotas;
    private int total;

    public DetalhesProdutoDto(Produto produto) {
        this.descricao = produto.getDescricao();
        this.nome = produto.getNome();
        this.preco = produto.getValor();
        this.caracteristicas = produto.mapeiaCaracteristicas(DetalhesProdutoCaracteristica::new);
        this.linksImagens = produto.mapeiaImagens(ImagemProduto::getLink);
        this.perguntas = produto.mapeiaPerguntas(Pergunta::getTitulo);
        Opinioes opinioes = produto.getOpinioes();
        this.opinioes = produto.mapeiaOpinioes(opiniao ->{return
                Map.of("Titulo", opiniao.getTitulo(), "descricao", opiniao.getDescricao());
        });
        this.mediaNotas = opinioes.media();
        this.total = opinioes.total();

    }

    public String getDescricao() {
        return descricao;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Set<DetalhesProdutoCaracteristica> getCaracteristicas() {
        return caracteristicas;
    }

    public Set<String> getLinksImagens() {
        return linksImagens;
    }

    public Set<String> getPerguntas() {
        return perguntas;
    }

    public Set<Map<String, String>> getOpinioes() {
        return opinioes;
    }

    public double getMediaNotas() {
        return mediaNotas;
    }

    public int getTotal() {
        return total;
    }
}
