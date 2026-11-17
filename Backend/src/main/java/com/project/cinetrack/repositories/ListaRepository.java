package com.project.cinetrack.repositories;

import com.project.cinetrack.modeloBD.Lista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaRepository extends JpaRepository<Lista, Long> {

    List<Lista> findByUsuarioId(Long usuarioId);

    List<Lista> findByUsuarioIdAndTipo(Long usuarioId, String tipo);

    List<Lista> findByPublicaTrue();

    Optional<Lista> findByUsuarioIdAndTipoAndNome(Long usuarioId, String tipo, String nome);
}