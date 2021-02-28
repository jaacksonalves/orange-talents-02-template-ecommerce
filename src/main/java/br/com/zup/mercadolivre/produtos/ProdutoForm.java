package br.com.zup.mercadolivre.produtos;

import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.compartilhado.ExistsId;
import br.com.zup.mercadolivre.compartilhado.UniqueValue;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProdutoForm {

    @NotBlank
    @UniqueValue(domainClass = Produto.class, fieldName = "nome", message = "Não pode cadastrar dois produtos com o mesmo nome")
    private String nome;
    @Positive
    private int quantidade;
    @NotBlank
    @Length(max = 1000)
    private String descricao;
    @NotNull
    @Positive
    private BigDecimal valor;
    @NotNull
    @ExistsId(fieldName = "id", domainClass = Categoria.class, message = "Essa categoria não existe!")
    private Long idCategoria;
    @Size(min = 3)
    @Valid
    private List<CaracteristicaProdutoForm> caracteristicas = new ArrayList<>();


    public List<CaracteristicaProdutoForm> getCaracteristicas() {
        return caracteristicas;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }


    public Set<String> buscaCaracteristicasIguais() {
        HashSet<String> nomesIguais = new HashSet<>();
        HashSet<String> resultados = new HashSet<>();

        for (CaracteristicaProdutoForm caracteristica : caracteristicas) {
            String nome = caracteristica.getNome();

            if (!nomesIguais.add(nome)) {
                resultados.add(nome);
            }
        }
        return resultados;
    }

    public ProdutoForm(@NotBlank String nome, @Positive int quantidade, @NotBlank @Length(max = 1000) String descricao,
                       @NotNull @Positive BigDecimal valor, @NotNull Long idCategoria, @Size(min = 3) @Valid List<CaracteristicaProdutoForm> caracteristicas) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valor = valor;
        this.idCategoria = idCategoria;
        this.caracteristicas.addAll(caracteristicas);
    }

    public Produto toModel(EntityManager em, Usuario usuarioCriador) {
        Categoria categoria = em.find(Categoria.class, idCategoria);

        return new Produto(nome, quantidade, descricao, valor, categoria, usuarioCriador, caracteristicas);
    }
}
