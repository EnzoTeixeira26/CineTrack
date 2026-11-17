import api from './api';
import type { Lista, ListaItem } from '../types';

export const listasService = {
  listarPorUsuario: async (usuarioId: number): Promise<Lista[]> => {
    const { data } = await api.get<Lista[]>(`/listas/usuario/${usuarioId}`);
    return data;
  },
  buscarPorId: async (id: number): Promise<Lista> => {
    const { data } = await api.get<Lista>(`/listas/${id}`);
    return data;
  },
  criar: async (payload: { usuarioId: number; nome: string; tipo: 'FAVORITOS' | 'WATCHLIST' | 'CUSTOM'; descricao?: string; publica: boolean }): Promise<Lista> => {
    const { data } = await api.post<Lista>('/listas', payload);
    return data;
  },
  atualizar: async (id: number, payload: { nome: string; descricao?: string; publica: boolean }): Promise<Lista> => {
    const { data } = await api.put<Lista>(`/listas/${id}`, payload);
    return data;
  },
  deletar: async (id: number): Promise<void> => {
    await api.delete(`/listas/${id}`);
  },
  listarItens: async (id: number): Promise<ListaItem[]> => {
    const { data } = await api.get<ListaItem[]>(`/listas/${id}/itens`);
    return data;
  },
  adicionarItem: async (listaId: number, obraId: number, ordem?: number): Promise<ListaItem> => {
    const { data } = await api.post<ListaItem>(`/listas/${listaId}/itens`, { obraId, ordem });
    return data;
  },
  removerItem: async (listaId: number, obraId: number): Promise<void> => {
    await api.delete(`/listas/${listaId}/itens/${obraId}`);
  },
};