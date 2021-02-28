package br.com.zup.mercadolivre.controllers;


import br.com.zup.mercadolivre.usuarios.Usuario;
import br.com.zup.mercadolivre.usuarios.UsuarioForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsuarioControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager em;

    private String toJson(Object form) throws JsonProcessingException {
        return objectMapper.writeValueAsString(form);
    }

    @Test
    @DisplayName("Deve criar um novo usuário e retornar status 200, com email checado se existe no banco, e instante de criação não nulo, senha salva em hash")
    public void deveCriarNovoUsuario() throws Exception {

        UsuarioForm usuarioForm = new UsuarioForm("marcio@email.com", "123456");

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(usuarioForm)))
                .andExpect(status().isOk()
                );

        Usuario usuario = (Usuario) em.createQuery("select u from Usuario u where u.email=:email")
                .setParameter("email", "marcio@email.com").getResultList().get(0);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(usuarioForm.getSenha(), usuario.getSenha());

        assertEquals(usuarioForm.getEmail(), usuario.getEmail());
        assertNotNull(usuario.getInstanteCadastro());
        assertNotEquals(usuarioForm.getSenha(), usuario.getSenha());
        assertTrue(matches);

    }


    @Test
    @DisplayName("não deve criar usuário com email ja cadastrado, invalido ou nulo. não deve criar usuário com senha menor de 6 dígitos. deve retornar status 400")
    public void naoDeveCriarNovoUsuarioComEmailJaExistenteOuInvalidoOuNuloOuSenhaMenor6Digitos() throws Exception {
        UsuarioForm usuarioForm = new UsuarioForm("marcio@email.com", "123456");
        UsuarioForm emailInvalido = new UsuarioForm("marcio", "123456");
        UsuarioForm emailNulo = new UsuarioForm("", "123456");
        UsuarioForm senhaMenor = new UsuarioForm("marcio2@email.com", "12345");
        em.persist(usuarioForm.toModel());

        //Testa se consegue cadastrar usuario com email ja cadastrado
        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(usuarioForm)))
                .andExpect(status().isBadRequest()
                );

        //Testa se consegue cadastrar usuario com email invalido
        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(emailInvalido)))
                .andExpect(status().isBadRequest()
                );

        //Testa se consegue cadastrar usuario com email nulo
        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(emailNulo)))
                .andExpect(status().isBadRequest()
                );

        //Testa se consegue cadastrar usuario com senha menor de 6 digitos
        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(senhaMenor)))
                .andExpect(status().isBadRequest()
                );
    }


}
