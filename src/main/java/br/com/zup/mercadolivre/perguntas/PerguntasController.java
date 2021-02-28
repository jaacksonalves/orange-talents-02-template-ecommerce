package br.com.zup.mercadolivre.perguntas;

import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.produtos.Produto;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/perguntas")
public class PerguntasController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private Emails emails;

    @Transactional
    @PostMapping("/{id}")
    public ResponseEntity<String> cadastraPergunta(@PathVariable("id") Long idProduto,
                                                   @Valid @RequestBody PerguntaForm form,
                                                   @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        Produto produto = em.find(Produto.class, idProduto);
        Usuario usuario = usuarioLogado.get();

        Pergunta pergunta = form.toModel(produto, usuario);
        em.persist(pergunta);

        emails.novaPergunta(pergunta);

        return ResponseEntity.ok(produto.getPerguntas().toString());
    }
}
