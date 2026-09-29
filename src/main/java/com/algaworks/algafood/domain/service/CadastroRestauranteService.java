package com.algaworks.algafood.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class CadastroRestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final CozinhaRepository cozinhaRepository;

    CadastroRestauranteService(
            RestauranteRepository restauranteRepository, CozinhaRepository cozinhaRepository) {
        this.restauranteRepository = restauranteRepository;
        this.cozinhaRepository = cozinhaRepository;
    }

    public Restaurante salvar(Restaurante restaurante) {

        Long cozinhaId = restaurante.getCozinha().getId();

        Cozinha cozinha = cozinhaRepository.findById(cozinhaId).orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Não existe cadastro de cozinha com o código %d", cozinhaId)));

        restaurante.setCozinha(cozinha);

        return restauranteRepository.save(restaurante);
    }

    public void excluir(Long restauranteId) {

        try {

            if (!restauranteRepository.existsById(restauranteId)) {

                throw new EntidadeNaoEncontradaException(
                        String.format("Não existe um cadastro de restaurante com o código %d", restauranteId));

            }

            restauranteRepository.deleteById(restauranteId);

        } catch (DataIntegrityViolationException e) {

            throw new EntidadeEmUsoException(
                    String.format("Restaurante de código %d não pode ser removida, pois está em uso", restauranteId));

        }
    }
}
