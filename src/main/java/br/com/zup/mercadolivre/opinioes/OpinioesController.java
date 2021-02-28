package br.com.zup.mercadolivre.opinioes;


import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/opiniao")
public class OpinioesController {

    @PersistenceContext
    private EntityManager em;


    @Transactional
    @PostMapping("/{id}")
    public String cadastraOpiniaoProduto(@PathVariable("id") Long idProduto, @Valid @RequestBody OpiniaoForm form, @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        Usuario usuario = usuarioLogado.get();
        Produto produto = em.find(Produto.class, idProduto);

        Opiniao opiniao = form.toModel(usuario, produto);
        em.persist(opiniao);
        return opiniao.toString();

    }
}
