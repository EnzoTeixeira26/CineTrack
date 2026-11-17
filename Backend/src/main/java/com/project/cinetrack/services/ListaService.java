package com.project.cinetrack.services;

import com.project.cinetrack.modeloBD.Lista;
import com.project.cinetrack.modeloBD.ListaItem;
import com.project.cinetrack.modeloBD.Obra;
import com.project.cinetrack.modeloBD.Usuario;
import com.project.cinetrack.repositories.ListaItemRepository;
import com.project.cinetrack.repositories.ListaRepository;
import com.project.cinetrack.repositories.ObraRepository;
import com.project.cinetrack.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final ListaItemRepository listaItemRepository;
    private final UsuarioRepository usuarioRepository;
    private final ObraRepository obraRepository;

    public ListaService(ListaRepository listaRepository,
                        ListaItemRepository listaItemRepository,
                        UsuarioRepository usuarioRepository,
                        ObraRepository obraRepository) {
        this.listaRepository = listaRepository;
        this.listaItemRepository = listaItemRepository;
        this.usuarioRepository = usuarioRepository;
        this.obraRepository = obraRepository;
    }

    @Transactional(readOnly = true)
    public List<Lista> listarPorUsuario(Long usuarioId) {
        return listaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public Lista buscarPorId(Long id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista não encontrada: " + id));
    }

    @Transactional
    public Lista criar(Long usuarioId, String nome, String tipo, String descricao, boolean publica) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + usuarioId));
        Lista lista = new Lista();
        lista.setUsuario(usuario);
        lista.setNome(nome);
        lista.setTipo(tipo);
        lista.setDescricao(descricao);
        lista.setPublica(publica);
        lista.setDataCriacao(LocalDateTime.now());
        return listaRepository.save(lista);
    }

    @Transactional
    public Lista atualizar(Long id, String nome, String descricao, boolean publica) {
        Lista lista = buscarPorId(id);
        lista.setNome(nome);
        lista.setDescricao(descricao);
        lista.setPublica(publica);
        return listaRepository.save(lista);
    }

    @Transactional
    public void deletar(Long id) {
        if (!listaRepository.existsById(id)) {
            throw new RuntimeException("Lista não encontrada: " + id);
        }
        listaRepository.deleteById(id);
    }

    @Transactional
    public ListaItem adicionarItem(Long listaId, Long obraId, Integer ordem) {
        if (listaItemRepository.existsByListaIdAndObraId(listaId, obraId)) {
            throw new RuntimeException("Obra já está nesta lista");
        }
        Lista lista = buscarPorId(listaId);
        Obra obra = obraRepository.findById(obraId)
                .orElseThrow(() -> new RuntimeException("Obra não encontrada: " + obraId));

        ListaItem item = new ListaItem();
        item.setLista(lista);
        item.setObra(obra);
        item.setOrdem(ordem != null ? ordem : 0);
        item.setDataAdicao(LocalDateTime.now());
        return listaItemRepository.save(item);
    }

    @Transactional
    public void removerItem(Long listaId, Long obraId) {
        ListaItem item = listaItemRepository.findByListaIdAndObraId(listaId, obraId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado nesta lista"));
        listaItemRepository.delete(item);
    }

    @Transactional(readOnly = true)
    public List<ListaItem> listarItens(Long listaId) {
        return listaItemRepository.findByListaIdOrderByOrdemAsc(listaId);
    }
}