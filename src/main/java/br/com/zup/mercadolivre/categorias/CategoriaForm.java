package br.com.zup.mercadolivre.categorias;

import br.com.zup.mercadolivre.compartilhado.ExistsId;
import br.com.zup.mercadolivre.compartilhado.UniqueValue;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotBlank;

public class CategoriaForm {

    @NotBlank(message = "Nome deve ser peenchido")
    @Length(min = 3)
    @UniqueValue(fieldName = "nome", domainClass = Categoria.class, message = "Categoria já existe no sistema!")
    private String nome;
    @ExistsId(fieldName = "id", domainClass = Categoria.class, message = "Id não existe")
    private Long idCategoriaMae;


    public String getNome() {
        return nome;
    }

    public Long getIdCategoriaMae() {
        return idCategoriaMae;
    }


    @Deprecated
    public void setNome(String nome) {
        this.nome = nome;
    }



    public Categoria toModel(EntityManager em) {
        Categoria novaCategoria = new Categoria(nome);
        if (idCategoriaMae != null) {
            Categoria categoriaMae = em.find(Categoria.class, idCategoriaMae);
            novaCategoria.setMae(categoriaMae);
        }
        return novaCategoria;
    }
}
