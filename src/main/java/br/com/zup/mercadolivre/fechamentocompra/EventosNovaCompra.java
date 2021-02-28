package br.com.zup.mercadolivre.fechamentocompra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EventosNovaCompra {

    @Autowired
    private Set<EventoCompraSucesso> eventoCompraSucessos;

    public void processa(Compra compra) {
        if (compra.processadaComSucesso()) {
            eventoCompraSucessos.forEach(evento -> evento.processa(compra));
        } else {
            //eventos falha
        }
    }
}
