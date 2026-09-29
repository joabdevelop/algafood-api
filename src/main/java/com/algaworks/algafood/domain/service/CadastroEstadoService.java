package com.algaworks.algafood.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CadastroEstadoService {

    private final EstadoRepository estadoRepository;

    public CadastroEstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    public void excluir(Long estadoId) {
        try {

            if (!estadoRepository.existsById(estadoId)) {
                throw new EntidadeNaoEncontradaException(
                        String.format(
                                "Não existe um cadastro de estado com o código %d",
                                estadoId));
            }

            estadoRepository.deleteById(estadoId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(
                            "Estado de código %d não pode ser removida, pois está em uso.", estadoId));
        }
    }

}
