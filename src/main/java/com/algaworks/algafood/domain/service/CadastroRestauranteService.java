package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.Cidade;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroRestauranteService {

	@Autowired
	private RestauranteRepository restauranteRepository;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@Transactional
	public Restaurante salvar(@NotNull Restaurante restaurante ) {
	    Cozinha cozinha = cadastroCozinha.buscarOuFalhar(restaurante.getCozinha().getId());
		Cidade cidade = cadastroCidade.buscarOuFalhar(restaurante.getEndereco().getCidade().getId());
	    
	    restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
	    
	    return restauranteRepository.save(restaurante);
	}

	@Transactional
	public void ativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

		restauranteAtual.ativar();
	}

	@Transactional
	public void inativar(Long restauranteId) {
		Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

		restauranteAtual.inativar();
	}

	public Restaurante buscarOuFalhar(Long restauranteId) {
	    return restauranteRepository.findById(restauranteId)
	        .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
	}
}
