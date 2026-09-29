package com.algaworks.algafood.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CadastroCozinhaService {

    private final CozinhaRepository cozinhaRepository;

    CadastroCozinhaService(CozinhaRepository cozinhaRepository) {
        this.cozinhaRepository = cozinhaRepository;
    }

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public void excluir(Long cozinhaId) {
        try {
            // 1. Verificamos se o ID existe no banco
            if (!cozinhaRepository.existsById(cozinhaId)) {
                throw new EntidadeNaoEncontradaException(
                        String.format("Não existe um cadastro de cozinha com código %d", cozinhaId));
            }

            // 2. Se existe, manda deletar com segurança
            cozinhaRepository.deleteById(cozinhaId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format("Cozinha de código %d não pode ser removida, pois está em uso", cozinhaId));
        }
    }
}
