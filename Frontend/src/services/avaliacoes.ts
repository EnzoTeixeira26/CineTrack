import api from './api';
import type { Avaliacao } from '../types';

export const avaliacoesService = {
  listarPorObra: async (obraId: number): Promise<Avaliacao[]> => {
    const { data } = await api.get<Avaliacao[]>(`/avaliacoes/obra/${obraId}`);
    return data;
  },
  listarPorUsuario: async (usuarioId: number): Promise<Avaliacao[]> => {
    const { data } = await api.get<Avaliacao[]>(`/avaliacoes/usuario/${usuarioId}`);
    return data;
  },
  criar: async (payload: { usuarioId: number; obraId: number; nota: number; comentario?: string }): Promise<Avaliacao> => {
    const { data } = await api.post<Avaliacao>('/avaliacoes', payload);
    return data;
  },
  atualizar: async (id: number, payload: { nota: number; comentario?: string }): Promise<Avaliacao> => {
    const { data } = await api.put<Avaliacao>(`/avaliacoes/${id}`, payload);
    return data;
  },
  deletar: async (id: number): Promise<void> => {
    await api.delete(`/avaliacoes/${id}`);
  },
};