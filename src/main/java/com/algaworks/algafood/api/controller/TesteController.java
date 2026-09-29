package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {

    private final CozinhaRepository cozinhaRepository;
    private final RestauranteRepository restauranteRepository;

    TesteController(CozinhaRepository cozinhaRepository,
            RestauranteRepository restauranteRepository) {
        this.cozinhaRepository = cozinhaRepository;
        this.restauranteRepository = restauranteRepository;
    }

    @GetMapping("/cozinhas/por-nome")
    public List<Cozinha> cozinhasPorNome(String nome) {
        return cozinhaRepository.findTodasByNomeContaining(nome);
    }

    @GetMapping("/cozinhas/unica-por-nome")
    public Optional<Cozinha> cozinhaPorNome(String nome) {
        return cozinhaRepository.findByNome(nome);
    }

    @GetMapping("/cozinhas/exists")
    public Boolean cozinhaExists(String nome) {
        return cozinhaRepository.existsByNome(nome);
    }

    @GetMapping("/cozinhas/primeiro")
    public Optional<Cozinha> CozinhaPrimeiro(String nome) {
        return cozinhaRepository.buscarPrimeiro();
    }

    @GetMapping("/resturantes/por-taxa-frete")
    public List<Restaurante> restaurantesPorTaxaFrete(
            BigDecimal taxaInical, BigDecimal taxaFinal) {
        return restauranteRepository.queryByTaxaFreteBetween(taxaInical, taxaFinal);
    }

    @GetMapping("/resturantes/por-nome")
    public List<Restaurante> restaurantesPorNome(
            String nome, Long cozinhaId) {
        return restauranteRepository.consultarPorNome(nome, cozinhaId);
    }

    @GetMapping("/resturantes/primeiro-por-nome")
    public Optional<Restaurante> restaurantePrimeiroPorNome(String nome) {
        return restauranteRepository.findFirstRestauranteByNomeContaining(nome);
    }

    @GetMapping("/resturantes/top2-por-nome")
    public List<Restaurante> restaurantesTopPorNome(String nome) {
        return restauranteRepository.findTop2ByNomeContaining(nome);
    }

    @GetMapping("/resturantes/por-nome-e-frete")
    public List<Restaurante> restaurantesPorNomeFrete(String nome,
            BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {
        return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
    }

    @GetMapping("/resturantes/count-por-cozinha")
    public int restaurantesCountPorCozinha(Long cozinhaId) {
        return restauranteRepository.countByCozinhaId(cozinhaId);
    }

    @GetMapping("/resturantes/com-frete-gratis")
    public List<Restaurante> restaurantesComFreteGratis(String nome) {

        return restauranteRepository.findComFreteGratis(nome);
    }

    @GetMapping("/resturantes/primeiro")
    public Optional<Restaurante> restaurantePrimeiro() {
        return restauranteRepository.buscarPrimeiro();
    }
}