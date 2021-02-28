package br.com.zup.mercadolivre.usuarios;

import br.com.zup.mercadolivre.compartilhado.UniqueValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UsuarioForm {


    @NotBlank
    @Email
    @UniqueValue(fieldName = "email", domainClass = Usuario.class, message = "Email já cadastrado!")
    @JsonProperty
    private final String email;
    @NotBlank
    @Length(min = 6, message = "Senha precisa ter no mínimo 6 caracteres!")
    @JsonProperty
    private final String senha;


    /**
     * @param email string no formato de email
     * @param senha string no formato de texto limpo
     */
    public UsuarioForm(@NotBlank @Email String email, @NotBlank @Length(min = 6) String senha) {
        this.email = email;
        this.senha = senha;
    }

    @Deprecated
    public String getEmail() {
        return email;
    }

    @Deprecated
    public String getSenha() {
        return senha;
    }

    public Usuario toModel() {
        return new Usuario(email, senha);
    }

}
