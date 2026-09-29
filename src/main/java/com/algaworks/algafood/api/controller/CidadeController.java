package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

    private final CidadeRepository cidadeRepository;
    private final CadastroCidadeService cadastroCidade;

    CidadeController(CidadeRepository cidadeRepository, CadastroCidadeService cadastroCidade) {
        this.cidadeRepository = cidadeRepository;
        this.cadastroCidade = cadastroCidade;
    }

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping(value = "/{cidadeId}")
    public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {

        Cidade cidade = cidadeRepository.findById(cidadeId).orElse(null);

        if (cidade != null) {
            return ResponseEntity.ok(cidade);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
        try {
            cidade = cadastroCidade.salvar(cidade);

            return ResponseEntity.status(HttpStatus.CREATED).body(cidade);

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
        try {

            Cidade cidadeAtual = cidadeRepository.findById(cidadeId).orElse(null);

            if (cidadeAtual != null) {

                BeanUtils.copyProperties(cidade, cidadeAtual, "id");

                cidadeAtual = cadastroCidade.salvar(cidadeAtual);

                return ResponseEntity.ok(cidadeAtual);

            }

            return ResponseEntity.notFound().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{cidadeId}")
    public ResponseEntity<?> remover(@PathVariable Long cidadeId) {
        try {

            cadastroCidade.excluir(cidadeId);

            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {

            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

}
