package br.com.zup.mercadolivre.fechamentocompra;

import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Produto produtoComprado;
    private int quantidade;
    @ManyToOne
    private Usuario comprador;
    @Enumerated(EnumType.STRING)
    private GatewayPagamento gatewayPagamento;
    @OneToMany(mappedBy = "compra", cascade = CascadeType.MERGE)
    private Set<Transacao> transacoes = new HashSet<>();


    public Compra(Produto produtoComprado, int quantidade, Usuario comprador, GatewayPagamento gatewayPagamento) {

        this.produtoComprado = produtoComprado;
        this.quantidade = quantidade;
        this.comprador = comprador;
        this.gatewayPagamento = gatewayPagamento;
    }

    public Compra() {

    }


    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", produtoComprado=" + produtoComprado +
                ", quantidade=" + quantidade +
                ", comprador=" + comprador +
                ", gatewayPagamento=" + gatewayPagamento +
                ", transacoes=" + transacoes +
                '}';
    }

    public void adicionaTransacao(RetornoGatewayPagamento form) {
        Transacao novaTransacao = form.toTransacao(this);
        Assert.isTrue(!this.transacoes.contains(novaTransacao), "Já existe uma transação igual a essa processada");

        Set<Transacao> transacoesConcluidasSucesso = getTransacoesConcluidasComSucesso();
        Assert.isTrue(transacoesConcluidasSucesso.isEmpty(), "Essa compra já foi concluída");

        this.transacoes.add(form.toTransacao(this));
    }

    private Set<Transacao> getTransacoesConcluidasComSucesso() {
        return this.transacoes.stream().filter(Transacao::concluidaComSucesso).collect(Collectors.toSet());
    }

    public Long getId() {
        return this.id;
    }

    public boolean processadaComSucesso() {
        return !getTransacoesConcluidasComSucesso().isEmpty();
    }

    public Usuario getComprador() {
        return comprador;
    }

    public Usuario getVendedor() {
        return this.produtoComprado.getCriador();
    }
}
