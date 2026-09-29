package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping("/estados")
public class EstadoController {

    /**
     * Não se utiliza mais essas anotações nas versões atualizadoas.
     *
     * @Autowired
     *            privite EstadoRepository estadoRepository;
     */
    private final EstadoRepository estadoRepository;
    private final CadastroEstadoService cadastroEstado;

    EstadoController(EstadoRepository estadoRepository, CadastroEstadoService cadastroEstado) {
        this.estadoRepository = estadoRepository;
        this.cadastroEstado = cadastroEstado;
    }

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.findAll();
    }

    @GetMapping("/{estadoId}")
    public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {

        Optional<Estado> estado = estadoRepository.findById(estadoId);

        if (estado.isPresent()) {

            return ResponseEntity.ok(estado.get());
        }

        return ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Estado adicionar(@RequestBody Estado estado) {
        return cadastroEstado.salvar(estado);
    }

    @PutMapping("/{estadoId}")
    public ResponseEntity<Estado> atualiza(@PathVariable Long estadoId, @RequestBody Estado estado) {

        if (estadoId == null || estado == null) {
            return ResponseEntity.badRequest().build();
        }

        Estado estadoAtual = estadoRepository.findById(estadoId).orElse(null);

        if (estadoAtual != null) {

            BeanUtils.copyProperties(estado, estadoAtual, "id");

            Estado estadoSlvar = cadastroEstado.salvar(estadoAtual);

            return ResponseEntity.ok(estadoSlvar);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<?> remover(@PathVariable Long estadoId) {
        try {

            cadastroEstado.excluir(estadoId);

            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {

            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {

            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }
}
