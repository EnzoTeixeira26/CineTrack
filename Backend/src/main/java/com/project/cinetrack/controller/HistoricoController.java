package com.project.cinetrack.controller;

import com.project.cinetrack.modeloBD.Historico;
import com.project.cinetrack.services.HistoricoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoController {

    private final HistoricoService historicoService;

    public HistoricoController(HistoricoService historicoService) {
        this.historicoService = historicoService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Historico>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(historicoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/status/{status}")
    public ResponseEntity<List<Historico>> listarPorUsuarioEStatus(
            @PathVariable Long usuarioId,
            @PathVariable String status) {
        return ResponseEntity.ok(historicoService.listarPorUsuarioEStatus(usuarioId, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(historicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Historico> registrar(@RequestBody RegistrarHistoricoRequest request) {
        Historico historico = historicoService.registrar(
                request.usuarioId(),
                request.obraId(),
                request.status(),
                request.dataInicio(),
                request.dataConclusao(),
                request.progresso()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(historico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    public record RegistrarHistoricoRequest(
            Long usuarioId,
            Long obraId,
            String status,
            LocalDate dataInicio,
            LocalDate dataConclusao,
            String progresso
    ) {}
}