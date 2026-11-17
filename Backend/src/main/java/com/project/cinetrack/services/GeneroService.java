package com.project.cinetrack.services;

import com.project.cinetrack.modeloBD.Genero;
import com.project.cinetrack.repositories.GeneroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GeneroService {

    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    @Transactional(readOnly = true)
    public List<Genero> listarTodos() {
        return generoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Genero buscarPorId(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero não encontrado: " + id));
    }

    @Transactional
    public Genero criar(Genero genero) {
        if (generoRepository.findByNome(genero.getNome()).isPresent()) {
            throw new RuntimeException("Já existe um gênero com este nome: " + genero.getNome());
        }
        return generoRepository.save(genero);
    }

    @Transactional
    public Genero atualizar(Long id, Genero dados) {
        Genero genero = buscarPorId(id);
        genero.setNome(dados.getNome());
        genero.setSlug(dados.getSlug());
        genero.setTmdbId(dados.getTmdbId());
        return generoRepository.save(genero);
    }

    @Transactional
    public void deletar(Long id) {
        if (!generoRepository.existsById(id)) {
            throw new RuntimeException("Gênero não encontrado: " + id);
        }
        generoRepository.deleteById(id);
    }
}