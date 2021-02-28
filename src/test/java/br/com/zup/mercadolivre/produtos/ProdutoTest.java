package br.com.zup.mercadolivre.produtos;

import br.com.zup.mercadolivre.categorias.Categoria;
import br.com.zup.mercadolivre.usuarios.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Transactional
public class ProdutoTest {

    @DisplayName("um produto precisa de no mínimo 3 características")
    @ParameterizedTest
    @MethodSource("geradorTeste1")
    void teste1(Collection<CaracteristicaProdutoForm> caracteristicas) throws Exception {
        Categoria categoria = new Categoria("categoria");
        Usuario dono = new Usuario("email@email.com", "123456");

        new Produto("nome", 10, "descricao", BigDecimal.TEN, categoria, dono, (List<CaracteristicaProdutoForm>) caracteristicas);

    }

    static Stream<Arguments> geradorTeste1() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new CaracteristicaProdutoForm("chave", "valor"),
                                new CaracteristicaProdutoForm("chave2", "valor2"),
                                new CaracteristicaProdutoForm("chave3", "valor3")
                        )
                ),
                Arguments.of(
                        List.of(new CaracteristicaProdutoForm("chave", "valor"),
                                new CaracteristicaProdutoForm("chave2", "valor2"),
                                new CaracteristicaProdutoForm("chave3", "valor3"),
                                new CaracteristicaProdutoForm("chave4", "valor4")

                        )
                )
        );
    }


    @DisplayName("Um produto não pode ser criado com menos de 3 caracteristicas")
    @ParameterizedTest
    @MethodSource("geradorTeste2")
    void teste2(Collection<CaracteristicaProdutoForm> caracteristicas) throws Exception {
        Categoria categoria = new Categoria("categoria");
        Usuario dono = new Usuario("email@email.com", "123456");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Produto("nome", 10, "descricao", BigDecimal.TEN,
                    categoria, dono, (List<CaracteristicaProdutoForm>) caracteristicas);
        });
    }

    static Stream<Arguments> geradorTeste2() {
        return Stream.of(
                Arguments.of(
                        List.of(new CaracteristicaProdutoForm("chave", "valor"),
                                new CaracteristicaProdutoForm("chave2", "valor2")

                        )
                ), Arguments.of(
                        List.of((new CaracteristicaProdutoForm("chave", "valor"))
                        )
                )
        );

    }
}
