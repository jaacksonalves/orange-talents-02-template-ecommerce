package br.com.zup.mercadolivre.detalhesproduto;

import br.com.zup.mercadolivre.produtos.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RestController
@RequestMapping("/detalhes/produto")
public class DetalhesProdutoController {

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public DetalhesProdutoDto detalhesProduto(@PathVariable("id") Long idProduto) {
        Optional<Produto> existe = Optional.ofNullable(em.find(Produto.class, idProduto));
        if (existe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Produto produto = em.find(Produto.class, idProduto);


        return new DetalhesProdutoDto(produto);
    }
}
