package com.project.cinetrack.controller;

import com.project.cinetrack.modeloBD.Avaliacao;
import com.project.cinetrack.services.AvaliacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Avaliacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(avaliacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/obra/{obraId}")
    public ResponseEntity<List<Avaliacao>> listarPorObra(@PathVariable Long obraId) {
        return ResponseEntity.ok(avaliacaoService.listarPorObra(obraId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@RequestBody CriarAvaliacaoRequest request) {
        Avaliacao criada = avaliacaoService.criar(
                request.usuarioId(),
                request.obraId(),
                request.nota(),
                request.comentario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(
            @PathVariable Long id,
            @RequestBody AtualizarAvaliacaoRequest request) {
        return ResponseEntity.ok(avaliacaoService.atualizar(id, request.nota(), request.comentario()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        avaliacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    public record CriarAvaliacaoRequest(Long usuarioId, Long obraId, BigDecimal nota, String comentario) {}
    public record AtualizarAvaliacaoRequest(BigDecimal nota, String comentario) {}
}