package br.com.zup.mercadolivre.categorias;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @PostMapping
    public String cadastraCategoria(@Valid @RequestBody CategoriaForm form) {
        Categoria novaCategoria = form.toModel(em);
        em.persist(novaCategoria);
        return novaCategoria.toString();
    }
}
