package com.project.cinetrack.controller;

import com.project.cinetrack.modeloBD.Lista;
import com.project.cinetrack.modeloBD.ListaItem;
import com.project.cinetrack.services.ListaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Lista>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(listaService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lista> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(listaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Lista> criar(@RequestBody CriarListaRequest request) {
        Lista criada = listaService.criar(
                request.usuarioId(),
                request.nome(),
                request.tipo(),
                request.descricao(),
                request.publica()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lista> atualizar(@PathVariable Long id, @RequestBody AtualizarListaRequest request) {
        return ResponseEntity.ok(listaService.atualizar(id, request.nome(), request.descricao(), request.publica()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        listaService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/itens")
    public ResponseEntity<List<ListaItem>> listarItens(@PathVariable Long id) {
        return ResponseEntity.ok(listaService.listarItens(id));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<ListaItem> adicionarItem(@PathVariable Long id, @RequestBody AdicionarItemRequest request) {
        ListaItem item = listaService.adicionarItem(id, request.obraId(), request.ordem());
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @DeleteMapping("/{id}/itens/{obraId}")
    public ResponseEntity<Void> removerItem(@PathVariable Long id, @PathVariable Long obraId) {
        listaService.removerItem(id, obraId);
        return ResponseEntity.noContent().build();
    }

    public record CriarListaRequest(Long usuarioId, String nome, String tipo, String descricao, boolean publica) {}
    public record AtualizarListaRequest(String nome, String descricao, boolean publica) {}
    public record AdicionarItemRequest(Long obraId, Integer ordem) {}
}