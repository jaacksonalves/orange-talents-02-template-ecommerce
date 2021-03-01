package br.com.zup.mercadolivre.controllers;

import br.com.zup.mercadolivre.opinioes.Opiniao;
import br.com.zup.mercadolivre.opinioes.OpiniaoForm;
import br.com.zup.mercadolivre.produtos.Produto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OpinioesControllerTest {

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
    @DisplayName("Deve cadastra uma opinião de um produto, com nota de 1 a 5, com titulo, descricao, usuario, e produto que foi direcionada a opinião, retornar status 200")
    @WithUserDetails("jk@email.com") //logado
    public void deveCadastrarNovaOpiniaoParaUmProduto() throws Exception {
        OpiniaoForm opiniaoForm = new OpiniaoForm(3, "Gostei Teste", "Produto ok, bom custoxbeneficio");

        //Deve cadastrar opinião completa e retornar 200.
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(toJson(opiniaoForm)))
                .andExpect(status().is(200));

        Opiniao opiniao = (Opiniao) em.createQuery("select o from Opiniao o where o.titulo=:titulo").setParameter("titulo", "Gostei Teste").getResultList().get(0);
        Produto produto = em.find(Produto.class, 1L);
        Usuario usuario = (Usuario) em.createQuery("select u from Usuario u where u.email=:email").setParameter("email", "jk@email.com").getResultList().get(0);

        assertEquals(produto.getId(), opiniao.getProduto().getId());
        assertTrue(opiniao.getNota() >= 1 && opiniao.getNota() <= 5);
        assertNotNull(opiniao.getTitulo(), opiniao.getDescricao());
        assertEquals(opiniao.getUsuario(), usuario);
        assertNotNull(opiniao.getUsuario());

    }

    @Test
    @DisplayName("Não deve cadastrar opinião faltando campos ou ferindo validações, status retorno 400")
    @WithUserDetails("jk@email.com") //logado
    public void naoDeveCadastrarOpiniaoSemCamposPreenchidos() throws Exception {


        //Tentativa cadastrar opinião sem NOTA
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(opiniaoSemNota))
                .andExpect(status().is(400));

        //Tentativa cadastrar opinião com NOTA 0
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(opiniaoNota0))
                .andExpect(status().is(400));

        //Tentativa cadastrar opinião com NOTA 6
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(opiniaoNota6))
                .andExpect(status().is(400));

        //Tentativa cadastrar opinião sem TITULO
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(opiniaoSemTitulo))
                .andExpect(status().is(400));

        //Tentativa cadastrar opinião sem DESCRICAO
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(opiniaoSemDescricao))
                .andExpect(status().is(400));

    }

    @Test
    @DisplayName("Não deve cadastrar opiniãosem estar logado, retorno 401")
    public void naoDeveCadastrarOpiniaoSemEstarLogado() throws Exception {

        OpiniaoForm opiniaoForm = new OpiniaoForm(3, "Gostei Teste", "Produto ok, bom custoxbeneficio");

        //Tentativa de cadastar opinião sem estar logado
        mockMvc.perform(post("/opiniao/1")
                .contentType(APPLICATION_JSON).content(toJson(opiniaoForm)))
                .andExpect(status().is(401));

    }


    private String opiniaoSemNota = "{\"nota\":\"\",\n" +
            "\t\"titulo\":\"Opiniao Teste\",\n" +
            "\t\"descricao\":\"Comprei e arrependo, adorei muito\"}";

    private String opiniaoNota0 = "{\"nota\":\"0\",\n" +
            "\t\"titulo\":\"Adorei, nota 2\",\n" +
            "\t\"descricao\":\"Comprei e arrependo, adorei muito\"}";

    private String opiniaoNota6 = "{\"nota\":\"6\",\n" +
            "\t\"titulo\":\"Adorei, nota 2\",\n" +
            "\t\"descricao\":\"Comprei e arrependo, adorei muito\"}";

    private String opiniaoSemTitulo = "{\"nota\":\"2\",\n" +
            "\t\"titulo\":\"\",\n" +
            "\t\"descricao\":\"Comprei e arrependo, adorei muito\"}";

    private String opiniaoSemDescricao = "{\"nota\":\"2\",\n" +
            "\t\"titulo\":\"Adorei, nota 2\",\n" +
            "\t\"descricao\":\"\"}";


}
