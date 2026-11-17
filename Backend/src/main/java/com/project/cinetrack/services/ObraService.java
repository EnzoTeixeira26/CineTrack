package com.project.cinetrack.services;

import com.project.cinetrack.modeloBD.Genero;
import com.project.cinetrack.modeloBD.Obra;
import com.project.cinetrack.repositories.GeneroRepository;
import com.project.cinetrack.repositories.ObraRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ObraService {

    private final ObraRepository obraRepository;
    private final GeneroRepository generoRepository;

    public ObraService(ObraRepository obraRepository, GeneroRepository generoRepository) {
        this.obraRepository = obraRepository;
        this.generoRepository = generoRepository;
    }

    @Transactional(readOnly = true)
    public Page<Obra> listar(Pageable pageable) {
        return obraRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Obra buscarPorId(Long id) {
        return obraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public Page<Obra> buscarPorTitulo(String termo, Pageable pageable) {
        return obraRepository.buscarPorTitulo(termo, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Obra> buscarPorTipo(String tipo, Pageable pageable) {
        return obraRepository.findByTipo(tipo.toUpperCase(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Obra> buscarPorGenero(Long generoId, Pageable pageable) {
        return obraRepository.findByGeneroId(generoId, pageable);
    }

    @Transactional
    public Obra criar(Obra obra) {
        if (obra.getTmdbId() != null && obraRepository.existsByTmdbId(obra.getTmdbId())) {
            throw new RuntimeException("Já existe uma obra com este tmdbId: " + obra.getTmdbId());
        }
        obra.setDataCriacao(LocalDateTime.now());
        obra.setGeneros(resolverGeneros(obra.getGeneros()));
        return obraRepository.save(obra);
    }

    @Transactional
    public Obra atualizar(Long id, Obra dados) {
        Obra obra = buscarPorId(id);
        obra.setTitulo(dados.getTitulo());
        obra.setTituloOriginal(dados.getTituloOriginal());
        obra.setTipo(dados.getTipo());
        obra.setSinopse(dados.getSinopse());
        obra.setDataLancamento(dados.getDataLancamento());
        obra.setPosterUrl(dados.getPosterUrl());
        obra.setBackdropUrl(dados.getBackdropUrl());
        obra.setDuracaoMinutos(dados.getDuracaoMinutos());
        obra.setNumeroTemporadas(dados.getNumeroTemporadas());
        obra.setTmdbId(dados.getTmdbId());
        obra.setGeneros(resolverGeneros(dados.getGeneros()));
        return obraRepository.save(obra);
    }

    @Transactional
    public void deletar(Long id) {
        if (!obraRepository.existsById(id)) {
            throw new RuntimeException("Obra não encontrada: " + id);
        }
        obraRepository.deleteById(id);
    }

    private Set<Genero> resolverGeneros(Set<Genero> generos) {
        Set<Genero> resolvidos = new HashSet<>();
        if (generos != null) {
            for (Genero g : generos) {
                if (g.getId() != null) {
                    resolvidos.add(generoRepository.findById(g.getId())
                            .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + g.getId())));
                }
            }
        }
        return resolvidos;
    }
}