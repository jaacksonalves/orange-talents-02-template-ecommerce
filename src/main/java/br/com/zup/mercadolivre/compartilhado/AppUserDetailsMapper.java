package br.com.zup.mercadolivre.compartilhado;

import br.com.zup.mercadolivre.compartilhado.seguranca.UserDetailsMapper;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;


@Configuration
public class AppUserDetailsMapper implements UserDetailsMapper {

    @Override
    public UserDetails map(Object shouldBeASystemUser) {
        return new UsuarioLogado((Usuario) shouldBeASystemUser);
    }

}
