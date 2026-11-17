package com.project.cinetrack.services;

import com.project.cinetrack.modeloBD.Historico;
import com.project.cinetrack.modeloBD.Obra;
import com.project.cinetrack.modeloBD.Usuario;
import com.project.cinetrack.repositories.HistoricoRepository;
import com.project.cinetrack.repositories.ObraRepository;
import com.project.cinetrack.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoricoService {

    private final HistoricoRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;

    public HistoricoService(HistoricoRepository historicoRepository,
            UsuarioRepository usuarioRepository,
            ObraRepository obraRepository) {
        this.historicoRepository = historicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.obraRepository = obraRepository;
    }

    @Transactional(readOnly = true)
    public List<Historico> listarPorUsuario(Long usuarioId) {
        return historicoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Historico> listarPorUsuarioEStatus(Long usuarioId, String status) {
        return historicoRepository.findByUsuarioIdAndStatus(usuarioId, status.toUpperCase());
    }

    @Transactional(readOnly = true)
    public Historico buscarPorId(Long id) {
        return historicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Histórico não encontrado: " + id));
    }

    @Transactional
    public Historico registrar(Long usuarioId, Long obraId, String status,
            LocalDate dataInicio, LocalDate dataConclusao, String progresso) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada: " + obraId));

        Historico historico = historicoRepository
                .findByUsuarioIdAndObraId(usuarioId, obraId)
                .orElseGet(() -> {
                    Historico novo = new Historico();
                    novo.setUsuario(usuario);
                    novo.setObra(obra);
                    return novo;
                });

        historico.setStatus(status.toUpperCase());
        historico.setDataInicio(dataInicio);
        historico.setDataConclusao(dataConclusao);
        historico.setProgresso(progresso);
        historico.setDataAtualizacao(LocalDateTime.now());

        return historicoRepository.save(historico);
    }

    @Transactional
    public void deletar(Long id) {
        if (!historicoRepository.existsById(id)) {
            throw new RuntimeException("Histórico não encontrado: " + id);
        }
        historicoRepository.deleteById(id);
    }
}