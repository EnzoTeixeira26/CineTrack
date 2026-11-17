package com.project.cinetrack.repositories;

import com.project.cinetrack.modeloBD.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {

    List<Historico> findByUsuarioId(Long usuarioId);

    List<Historico> findByUsuarioIdAndStatus(Long usuarioId, String status);

    Optional<Historico> findByUsuarioIdAndObraId(Long usuarioId, Long obraId);

    boolean existsByUsuarioIdAndObraId(Long usuarioId, Long obraId);

    long countByUsuarioIdAndStatus(Long usuarioId, String status);
}