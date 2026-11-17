package com.project.cinetrack.repositories;

import com.project.cinetrack.modeloBD.ListaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListaItemRepository extends JpaRepository<ListaItem, Long> {

    List<ListaItem> findByListaIdOrderByOrdemAsc(Long listaId);

    Optional<ListaItem> findByListaIdAndObraId(Long listaId, Long obraId);

    boolean existsByListaIdAndObraId(Long listaId, Long obraId);

    long countByListaId(Long listaId);
}