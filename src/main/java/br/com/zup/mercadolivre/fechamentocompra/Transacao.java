package br.com.zup.mercadolivre.fechamentocompra;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StatusTransacao status;
    private String idTransacaoGateway;
    private LocalDateTime instanteTransacao;
    @ManyToOne
    private Compra compra;


    public Transacao(StatusTransacao normaliza, String idTransacao, Compra compra) {

        this.status = normaliza;
        this.idTransacaoGateway = idTransacao;
        this.instanteTransacao = LocalDateTime.now();
        this.compra = compra;
    }

    @Deprecated
    public Transacao() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(idTransacaoGateway, transacao.idTransacaoGateway);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransacaoGateway);
    }


    public boolean concluidaComSucesso() {
        return this.status.equals(StatusTransacao.SUCESSO);
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", status=" + status +
                ", idTransacaoGateway='" + idTransacaoGateway + '\'' +
                ", instanteTransacao=" + instanteTransacao +
                '}';
    }
}
