package br.com.zup.mercadolivre.controllers;


import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.categorias.CategoriaForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CategoriaControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager em;

    //Categoria sem categoria mãe
    private final String json1 = "{\"nome\" : \"Categoria\", \"idCategoriaMae\" : \"\"}";

    //Categoria com categoria mãe
    private final String json2 = "{\"nome\" : \"Categoria2\", \"idCategoriaMae\" : \"1\"}";

    //Categoria com nome em branco
    private final String json3 = "{\"nome\" : \"\", \"idCategoriaMae\" : \"\"}";


    @Test
    @DisplayName("Deve criar uma nova categoria com ou sem categoria mãe, deve retornar status 200")
    @WithUserDetails("jackson@email.com") //logado
    public void deveCriarNovaCategoriaComESemCategoriaMae() throws Exception {

        //Cadastra categoria sem categoria mãe
        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1))
                .andExpect(status().isOk());

        //Cadastra categoria com categoria mãe
        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isOk());

        Categoria categoria1 = (Categoria) em.createQuery("select c from Categoria c where c.nome=:nome").setParameter("nome", "Categoria").getResultList().get(0);
        Categoria categoria2 = (Categoria) em.createQuery("select c from Categoria c where c.nome=:nome").setParameter("nome", "Categoria2").getResultList().get(0);

        assertNotNull(categoria1);
        assertNotNull(categoria2.getCategoriaMae());

    }


    @Test
    @DisplayName("Não deve criar categoria com nome em branco ou duplicado, deve retornar 400")
    @WithUserDetails("jackson@email.com") //logado
    public void naoDeveCriarCategoriaComNomeDuplicadoOuEmBranco() throws Exception {
        CategoriaForm categoriaForm = new CategoriaForm();
        categoriaForm.setNome("Categoria");
        Categoria categoria = categoriaForm.toModel(em);
        em.persist(categoria);

        //Tentando cadastrar categoria com nome já existente
        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1))
                .andExpect(status().isBadRequest());

        //Tentando Cadastrar categoria com nome em branco
        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json3))
                .andExpect(status().isBadRequest());

    }


    @Test
    @DisplayName("Não deve cadastrar categoria sem estar logado, deve retornar 401")
    //nao esta logado
    public void naoDeveCriarCategoriaSemEstarLogado() throws Exception {

        //Cadastra categoria sem categoria mãe
        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1))
                .andExpect(status().is(401));

        //Cadastra categoria com categoria mãe
        mockMvc.perform(post("/categoria")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().is(401));
    }


}
