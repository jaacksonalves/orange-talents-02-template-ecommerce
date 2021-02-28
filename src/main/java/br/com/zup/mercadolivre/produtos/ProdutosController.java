package br.com.zup.mercadolivre.produtos;

import br.com.zup.mercadolivre.compartilhado.UsuarioLogado;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/produto")
public class ProdutosController {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UploaderFake uploaderFake;

    @InitBinder("ProdutoForm")
    public void init(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new ProibeCaracteristicaComNomeIgualValidator());
    }


    @Transactional
    @PostMapping
    public String cadastraProduto(@Valid @RequestBody ProdutoForm form, @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        Usuario usuarioCriador = usuarioLogado.get();
        Produto novoProduto = form.toModel(em, usuarioCriador);
        em.persist(novoProduto);
        return novoProduto.toString();

    }

    @Transactional
    @PostMapping("/{id}/imagens")
    public String adicionaImagens(@PathVariable("id") Long id,
                                  @Valid ImagemProdutoForm form,
                                  @AuthenticationPrincipal UsuarioLogado usuarioLogado) {
        Usuario usuario = usuarioLogado.get();
        Produto produto = em.find(Produto.class, id);

        if (!produto.pertenceAoUsuario(usuario)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode adicionar imagens a um produto que vc cadastrou");
        }

        Set<String> links = uploaderFake.envia(form.getImagens());
        produto.associaImagens(links);
        em.merge(produto);

        return produto.toString();
    }


}
