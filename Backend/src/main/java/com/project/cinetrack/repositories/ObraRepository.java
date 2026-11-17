package com.project.cinetrack.repositories;

import com.project.cinetrack.modeloBD.Obra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObraRepository extends JpaRepository<Obra, Long> {

    Optional<Obra> findByTmdbId(Integer tmdbId);

    boolean existsByTmdbId(Integer tmdbId);

    // Busca por título (case-insensitive, contendo)
    @Query("SELECT o FROM Obra o WHERE LOWER(o.titulo) LIKE LOWER(CONCAT('%', :termo, '%'))")
    Page<Obra> buscarPorTitulo(@Param("termo") String termo, Pageable pageable);

    // Busca por tipo (FILME ou SERIE)
    Page<Obra> findByTipo(String tipo, Pageable pageable);

    // Busca por gênero
    @Query("SELECT o FROM Obra o JOIN o.generos g WHERE g.id = :generoId")
    Page<Obra> findByGeneroId(@Param("generoId") Long generoId, Pageable pageable);

    // Obras com nota média acima de um valor
    List<Obra> findByNotaMediaGreaterThanEqualOrderByNotaMediaDesc(java.math.BigDecimal nota);
}