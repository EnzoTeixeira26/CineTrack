package com.project.cinetrack.controller;

import com.project.cinetrack.modeloBD.Obra;
import com.project.cinetrack.services.ObraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/obras")
public class ObraController {

    private final ObraService obraService;

    public ObraController(ObraService obraService) {
        this.obraService = obraService;
    }

    @GetMapping
    public ResponseEntity<Page<Obra>> listar(Pageable pageable) {
        return ResponseEntity.ok(obraService.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Obra> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(obraService.buscarPorId(id));
    }

    @GetMapping("/busca")
    public ResponseEntity<Page<Obra>> buscarPorTitulo(
            @RequestParam String termo,
            Pageable pageable) {
        return ResponseEntity.ok(obraService.buscarPorTitulo(termo, pageable));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<Obra>> buscarPorTipo(
            @PathVariable String tipo,
            Pageable pageable) {
        return ResponseEntity.ok(obraService.buscarPorTipo(tipo, pageable));
    }

    @GetMapping("/genero/{generoId}")
    public ResponseEntity<Page<Obra>> buscarPorGenero(
            @PathVariable Long generoId,
            Pageable pageable) {
        return ResponseEntity.ok(obraService.buscarPorGenero(generoId, pageable));
    }

    @PostMapping
    public ResponseEntity<Obra> criar(@RequestBody Obra obra) {
        Obra criada = obraService.criar(obra);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Obra> atualizar(@PathVariable Long id, @RequestBody Obra obra) {
        return ResponseEntity.ok(obraService.atualizar(id, obra));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        obraService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}