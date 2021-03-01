package br.com.zup.mercadolivre.produtos;

import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.opinioes.Opiniao;
import br.com.zup.mercadolivre.opinioes.Opinioes;
import br.com.zup.mercadolivre.perguntas.Pergunta;
import br.com.zup.mercadolivre.usuarios.Usuario;
import io.jsonwebtoken.lang.Assert;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int quantidade;
    @Lob
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime instanteCadastro = LocalDateTime.now();

    @ManyToOne
    private Categoria categoria;

    @ManyToOne
    private Usuario criador;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
    private Set<CaracteristicaProduto> caracteristicas = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.MERGE)
    private Set<ImagemProduto> imagens = new HashSet<>();

    @OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
    private Set<Pergunta> perguntas = new HashSet<>();
    @OneToMany(mappedBy = "produto", cascade = CascadeType.PERSIST)
    private Set<Opiniao> opinioes = new HashSet<>();


    public Produto(String nome, int quantidade, String descricao, BigDecimal valor, Categoria categoria,
                   Usuario usuarioCriador, List<CaracteristicaProdutoForm> caracteristicas) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.criador = usuarioCriador;
        Set<CaracteristicaProduto> novasCaracteristicas = caracteristicas.stream()
                .map(caracteristica -> caracteristica.toModel(this)).collect(Collectors.toSet());
        this.caracteristicas.addAll(novasCaracteristicas);

        Assert.isTrue(this.caracteristicas.size() >= 3, "Todo produto precisa ter no mínimo 3 características");
    }

    @Deprecated
    public Produto() {

    }

    public Set<Pergunta> getPerguntas() {
        return perguntas;
    }

    public Usuario getCriador() {
        return criador;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getInstanteCadastro() {
        return instanteCadastro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Set<CaracteristicaProduto> getCaracteristicas() {
        return caracteristicas;
    }

    public Set<ImagemProduto> getImagens() {
        return imagens;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(nome, produto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", instanteCadastro=" + instanteCadastro +
                ", categoria=" + categoria +
                ", criador=" + criador +
                ", caracteristicas=" + caracteristicas +
                ", imagens=" + imagens +
                '}';
    }

    public void associaImagens(Set<String> links) {
        Set<ImagemProduto> imagens = links.stream().map(link -> new ImagemProduto(this, link))
                .collect(Collectors.toSet());

        this.imagens.addAll(imagens);
    }

    public boolean pertenceAoUsuario(Usuario possivelUsuario) {
        return this.criador.equals(possivelUsuario);
    }

    public <T> Set<T> mapeiaImagens(Function<ImagemProduto, T> funcaoMapeadora) {
        return this.imagens.stream().map(funcaoMapeadora).collect(Collectors.toSet());
    }

    public <T> Set<T> mapeiaPerguntas(Function<Pergunta, T> funcaoMapeadora) {
        return this.perguntas.stream().map(funcaoMapeadora).collect(Collectors.toSet());
    }

    public <T> Set<T> mapeiaCaracteristicas(Function<CaracteristicaProduto, T> funcaoMapeadora) {
        return this.caracteristicas.stream().map(funcaoMapeadora).collect(Collectors.toSet());
    }

    public <T> Set<T> mapeiaOpinioes(Function<Opiniao, T> funcaoMapeadora) {
        return this.opinioes.stream().map(funcaoMapeadora).collect(Collectors.toSet());
    }

    public Opinioes getOpinioes() {
        return new Opinioes(this.opinioes);
    }

    public boolean abateEstoque(@Positive int quantidade) {
        Assert.isTrue(quantidade > 0, "A quantidade da compra deve ser maior que 0, para abater no estoque");

        if (this.quantidade < quantidade) {
            return false;
        }
        this.quantidade -= quantidade;
        return true;
    }
}
