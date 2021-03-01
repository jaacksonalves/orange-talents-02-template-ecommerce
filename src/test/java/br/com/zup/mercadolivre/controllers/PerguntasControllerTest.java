package br.com.zup.mercadolivre.controllers;

import br.com.zup.mercadolivre.perguntas.Pergunta;
import br.com.zup.mercadolivre.perguntas.PerguntaForm;
import br.com.zup.mercadolivre.usuarios.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PerguntasControllerTest {

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
    @DisplayName("Deve cadastrar nova pergunta, com um id de produto definido, contendo titulo, instante criação, usuario que fez a pergunta, produto. Retornar status 200")
    @WithUserDetails("jk@email.com")//logado
    public void deveCadastrarNovaPergunta() throws Exception {
        PerguntaForm perguntaForm = new PerguntaForm();
        perguntaForm.setTitulo("Pergunta Teste");

        //Cadastro de pergunta completo, usuário logado e id de produto selecionado
        mockMvc.perform(post("/perguntas/1")
                .contentType(APPLICATION_JSON).content(toJson(perguntaForm)))
                .andExpect(status().is(200));

        Pergunta pergunta = (Pergunta) em.createQuery("select p from Pergunta p where p.titulo=:titulo").setParameter("titulo", "Pergunta Teste").getResultList().get(0);
        Usuario usuario = (Usuario) em.createQuery("select u from Usuario u where u.email=:email").setParameter("email", "jk@email.com").getResultList().get(0);


        assertNotNull(pergunta);
        assertNotNull(pergunta.getTitulo());
        assertNotNull(pergunta.getUsuario());
        assertNotNull(pergunta.getProduto());
        assertEquals(usuario.getEmail(), pergunta.getUsuario().getEmail());

    }

    @Test
    @DisplayName("Deve cadastrar nova pergunta, sem titulo. deve retornar 400")
    @WithUserDetails("jk@email.com")//logado
    public void deveCadastrarNovaPerguntaSemTitulo() throws Exception {
        PerguntaForm perguntaForm = new PerguntaForm();

        //Cadastro de pergunta sem título
        mockMvc.perform(post("/perguntas/1")
                .contentType(APPLICATION_JSON).content(toJson(perguntaForm)))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("não deve cadastrar pergunta sem logar, retornar 401")
    //nao logado
    public void naoDeveCadastrarNovaPerguntaSemEstarLogado() throws Exception {
        PerguntaForm perguntaForm = new PerguntaForm();
        perguntaForm.setTitulo("Pergunta Teste");

        //Cadastro de pergunta completo, porém sem usuário logado
        mockMvc.perform(post("/perguntas/1")
                .contentType(APPLICATION_JSON).content(toJson(perguntaForm)))
                .andExpect(status().is(401));
    }


}
