package br.com.zup.mercadolivre.controllers;

import br.com.zup.mercadolivre.produtos.Produto;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProdutosControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @PersistenceContext
    private EntityManager em;


    @Test
    @DisplayName("Deve cadastrar novo produto, com usuário logado, produto pertence a esse usuário, deve retornar status 200")
    @WithUserDetails("jackson@email.com") //logado
    public void deveCadastrarNovoProduto() throws Exception {

        //Cadastro de produto completo, com nome, valor, quantidade, caracteristicas (3 no minimo), descrição, pertence a uma categoria e instante do cadastro.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonCompleto))
                .andExpect(status().is(200));

        Produto produto = (Produto) em.createQuery("select p from Produto p where p.nome=:nome").setParameter("nome", "Produto Teste").getResultList().get(0);

        assertNotNull(produto);
        assertNotNull(produto.getInstanteCadastro());
        assertNotNull(produto.getNome(), produto.getDescricao());
        assertNotNull(produto.getValor());
        assertNotNull(produto.getCategoria());
        assertNotNull(produto.getCaracteristicas());
        assertTrue(produto.getQuantidade() >= 0);


    }


    @Test
    @DisplayName("não deve cadastrar produtos sem nome, sem valor, sem quantidade ou quantidade menor que 0, com menos de 3 caracteristicas, sem descrição e sem categoria." +
            " Deve retornar status 400")
    @WithUserDetails("jackson@email.com") //logado
    public void naoDeveCadastrarProdutoComDadosErradosOuFaltandoOuNulos() throws Exception {


        //Tentando cadastrar produto sem NOME, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSemNome))
                .andExpect(status().is(400));

        //Tentando cadastrar produto sem CARACTERISTICAS, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSemCaracteristicas))
                .andExpect(status().is(400));

        //Tentando cadastrar produto sem 3CARACTERISTICAS, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSem3Caracteristicas))
                .andExpect(status().is(400));

        //Tentando cadastrar produto sem QUANTIDADE, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSemQuantidade))
                .andExpect(status().is(400));

        //Tentando cadastrar produto sem VALOR, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSemValor))
                .andExpect(status().is(400));

        //Tentando cadastrar produto sem CATEGORIA, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSemCategoria))
                .andExpect(status().is(400));

        //Tentando cadastrar produto sem DESCRICAO, retorno 400.
        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonSemDescricao))
                .andExpect(status().is(400));


    }


    @Test
    @DisplayName("Não deve cadastrar produtos se não estiver logado")
    //nao logado
    public void naoDeveCadastrarProdutoSemEstarLogado() throws Exception {

        mockMvc.perform(post("/produto")
                .contentType(MediaType.APPLICATION_JSON).content(jsonCompleto))
                .andExpect(status().is(401));

    }

    private final String jsonCompleto = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Tamanho\",\n" +
            "\t\t\t\"descricao\":\"109mm\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSemNome = "{\"nome\":\"\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Tamanho\",\n" +
            "\t\t\t\"descricao\":\"109mm\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSemValor = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Tamanho\",\n" +
            "\t\t\t\"descricao\":\"109mm\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSemQuantidade = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Tamanho\",\n" +
            "\t\t\t\"descricao\":\"109mm\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSemCaracteristicas = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\"\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSem3Caracteristicas = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSemDescricao = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"2\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Tamanho\",\n" +
            "\t\t\t\"descricao\":\"109mm\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";

    private final String jsonSemCategoria = "{\"nome\":\"Produto Teste\",\n" +
            "\t\"descricao\":\"Smartphone Samsung\",\n" +
            "\t\"valor\":\"4000\",\n" +
            "\t\"quantidade\":\"100\",\n" +
            "\t\"idCategoria\":\"\",\n" +
            "\t\"caracteristicas\":[\n" +
            "\t\t{\n" +
            "\t\t\t\"nome\":\"Tamanho\",\n" +
            "\t\t\t\"descricao\":\"109mm\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Potencia\",\n" +
            "\t\t\t\"descricao\":\"muita mesmo\"\n" +
            "\t\t},\t{\n" +
            "\t\t\t\"nome\":\"Peso\",\n" +
            "\t\t\t\"descricao\":\"45\"\n" +
            "\t\t}\n" +
            "\t\t\n" +
            "\t]}";


}
