package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.anyOf;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CadastroCozinhaServiceTests {

	@Autowired
	private CadastroCozinhaService cadastroCozinhaService;

	@Autowired
	private CadastroRestauranteService cadastroRestauranteService;
	@Test
	public void testarCadastroCozinhaComSucesso() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");

		novaCozinha = cadastroCozinhaService.salvar(novaCozinha);

		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
		Assertions.assertEquals("Chinesa", novaCozinha.getNome());
	}

	@Test
	public void testarCadastroCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		ConstraintViolationException erroEsperado =
				assertThrows(ConstraintViolationException.class, () -> {
					cadastroCozinhaService.salvar(novaCozinha);
				});

		assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalharQuandoExcluirCozinhaEmUso() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		Cozinha excluirCozinha = cadastroCozinhaService.salvar(novaCozinha);

		Restaurante restaurante = new Restaurante();
		restaurante.setNome("All Food");
		restaurante.setTaxaFrete(BigDecimal.valueOf(10));
		restaurante.setCozinha(excluirCozinha);
		restaurante = cadastroRestauranteService.salvar(restaurante);

		EntidadeEmUsoException erroEsperado =
				assertThrows(EntidadeEmUsoException.class, () -> {
					cadastroCozinhaService.excluir(excluirCozinha.getId());
				});

		assertThat(erroEsperado).isNotNull();
		assertThat(restaurante.getCozinha().getId()).isEqualTo(excluirCozinha.getId());
	}

	@Test
	public void deveFalharQuandoExcluirCozinhaInexistente() {
		List<Cozinha> listaCozinhas = cadastroCozinhaService.buscarTodas();
		Long idError = listaCozinhas.size() + 1L;

		CozinhaNaoEncontradaException erroEsperado =
				assertThrows(CozinhaNaoEncontradaException.class, () -> {
					cadastroCozinhaService.excluir(idError);
				});

		assertThat(erroEsperado).isNotNull();
	}
}
