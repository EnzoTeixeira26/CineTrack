package com.project.cinetrack.services;

import com.project.cinetrack.exceptions.BusinessException;
import com.project.cinetrack.exceptions.ResourceNotFoundException;
import com.project.cinetrack.modeloBD.Avaliacao;
import com.project.cinetrack.modeloBD.Obra;
import com.project.cinetrack.modeloBD.Usuario;
import com.project.cinetrack.repositories.AvaliacaoRepository;
import com.project.cinetrack.repositories.ObraRepository;
import com.project.cinetrack.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository,
                            UsuarioRepository usuarioRepository,
                            ObraRepository obraRepository) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.obraRepository = obraRepository;
    }

    @Transactional(readOnly = true)
    public List<Avaliacao> listarPorUsuario(Long usuarioId) {
        return avaliacaoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Avaliacao> listarPorObra(Long obraId) {
        return avaliacaoRepository.findByObraId(obraId);
    }

    @Transactional(readOnly = true)
    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avaliação não encontrada: " + id));
    }

    @Transactional
    public Avaliacao criar(Long usuarioId, Long obraId, BigDecimal nota, String comentario) {
        if (avaliacaoRepository.existsByUsuarioIdAndObraId(usuarioId, obraId)) {
            throw new BusinessException("Usuário já avaliou esta obra");
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + usuarioId));
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new ResourceNotFoundException("Obra não encontrada: " + obraId));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setUsuario(usuario);
        avaliacao.setObra(obra);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        avaliacao.setDataAvaliacao(LocalDateTime.now());

        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        atualizarNotaMediaObra(obra);
        return salva;
    }

    @Transactional
    public Avaliacao atualizar(Long id, BigDecimal nota, String comentario) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        atualizarNotaMediaObra(avaliacao.getObra());
        return salva;
    }

    @Transactional
    public void deletar(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        Obra obra = avaliacao.getObra();
        avaliacaoRepository.delete(avaliacao);
        atualizarNotaMediaObra(obra);
    }

    private void atualizarNotaMediaObra(Obra obra) {
        BigDecimal media = avaliacaoRepository.calcularMediaPorObra(obra.getId());
        if (media == null) {
            obra.setNotaMedia(BigDecimal.ZERO);
        } else {
            obra.setNotaMedia(media.setScale(2, RoundingMode.HALF_UP));
        }
        obraRepository.save(obra);
    }
}