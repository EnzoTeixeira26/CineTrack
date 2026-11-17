package com.project.cinetrack.repositories;

import com.project.cinetrack.modeloBD.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long> {

    Optional<Genero> findByNome(String nome);

    Optional<Genero> findBySlug(String slug);

    Optional<Genero> findByTmdbId(Integer tmdbId);
}