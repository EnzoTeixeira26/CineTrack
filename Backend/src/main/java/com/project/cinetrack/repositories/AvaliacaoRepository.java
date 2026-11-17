package com.project.cinetrack.repositories;

import com.project.cinetrack.modeloBD.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByUsuarioId(Long usuarioId);

    List<Avaliacao> findByObraId(Long obraId);

    Optional<Avaliacao> findByUsuarioIdAndObraId(Long usuarioId, Long obraId);

    boolean existsByUsuarioIdAndObraId(Long usuarioId, Long obraId);

    // Média das notas de uma obra
    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.obra.id = :obraId")
    BigDecimal calcularMediaPorObra(@Param("obraId") Long obraId);

    // Quantidade de avaliações de uma obra
    long countByObraId(Long obraId);
}