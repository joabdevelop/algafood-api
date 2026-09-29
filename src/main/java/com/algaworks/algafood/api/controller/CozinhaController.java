package com.algaworks.algafood.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
//import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.bind.annotation.ResponseStatus;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(value = "/cozinhas") // , produces = MediaType.APPLICATION_XML_VALUE)
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;
    private final CadastroCozinhaService cadastroCozinha;

    CozinhaController(CozinhaRepository cozinhaRepository, CadastroCozinhaService cadastroCozinha) {
        this.cozinhaRepository = cozinhaRepository;
        this.cadastroCozinha = cadastroCozinha;
    }

    @GetMapping
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    // Este methodo buscar cozinha com o tratamento para buscas localizadas
    @GetMapping(value = "/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {

        if (cozinhaId != null) {
            Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);

            if (cozinha.isPresent()) {
                return ResponseEntity.ok(cozinha.get());
            }
        }

        // Posso retornar desta forma: return
        // ResponseEntity.status(HttpSatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha) {
        return cadastroCozinha.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(
            @PathVariable Long cozinhaId,
            @RequestBody Cozinha cozinha) {

        if (cozinhaId == null || cozinha == null) {
            return ResponseEntity.badRequest().build();
        }

        Cozinha cozinhaAtual = cozinhaRepository.findById(cozinhaId).orElse(null);

        if (cozinhaAtual != null) {

            BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");

            Cozinha cozinhaSlvar = cadastroCozinha.salvar(cozinhaAtual);

            return ResponseEntity.ok(cozinhaSlvar);
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> remover(@PathVariable Long cozinhaId) {

        try {
            cadastroCozinha.excluir(cozinhaId);
            return ResponseEntity.noContent().build();

        } catch (EntidadeNaoEncontradaException e) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Não se utiliza mais essas anotações nas versões atualizadoas.
     * 
     * @Autowired
     *            privite CozinhaRepository cozinhaRepository;
     */

    /*
     * Se coloca essa propriedade (produces = MediaType.APPLICATION_JSON_VALUE) para
     * referenciar qual formato JSON ou XML quer usar na requisição dessa
     * propriedade listar()
     * 
     * @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
     * public List<Cozinha> listar() {
     * return cozinhaRepository.listar();
     * }
     */

    /*
     * Este é o methodo buscar cozinhinha sem o tratamento para buscas não
     * localizadas
     * 
     * @GetMapping(value = "/{cozinhaId}")
     * public Cozinha buscar(@PathVariable Long cozinhaId) {
     * return cozinhaRepository.buscar(cozinhaId);
     * }
     */

    /*
     * // @ResponseStatus(value = HttpStatus.CREATED) Esse methodo altera o status
     * da requisição abaixo.
     * 
     * @GetMapping(value = "/{cozinhaId}")
     * public Cozinha buscar(@PathVariable Long cozinhaId){
     * return cozinhaRepository.buscar(cozinhaId);
     * }
     */

    /*
     * @GetMapping(value = "/{cozinhaId}")
     * public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId){
     * Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);
     * 
     * // Desta forma customizamos o retorto da requisição onde o status nesse caso
     * retorna 200 e podemos retornar um body ou apenas o build que é sem o corpo na
     * resposta.
     * return ResponseEntity.status(HttpStatus.OK).body(cozinha);
     * 
     * // Forma mais simples para realizar uma requisição com sucesso
     * //return ResponseEntity.ok(cozinha);
     * 
     * // Desta forma abaixo o metodo FOUND para a resposta da requisição e o
     * direcionamento (LOCATION) que foi feito desse methodo FOUND.
     * 
     * HttpHeaders headers = new HttpHeaders();
     * headers.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");
     * 
     * return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
     * }
     */

}