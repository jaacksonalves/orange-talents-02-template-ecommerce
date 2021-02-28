package br.com.zup.mercadolivre.usuarios;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @PostMapping
    public void cadastraUsuarioESenha(@Valid @RequestBody UsuarioForm form) {
        Usuario novoUsuario = form.toModel();
        em.persist(novoUsuario);
    }


}
