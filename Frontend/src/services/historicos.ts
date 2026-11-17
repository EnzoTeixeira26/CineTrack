import api from './api';
import type { Historico, StatusHistorico } from '../types';

export const historicosService = {
  listarPorUsuario: async (usuarioId: number): Promise<Historico[]> => {
    const { data } = await api.get<Historico[]>(`/historicos/usuario/${usuarioId}`);
    return data;
  },
  listarPorStatus: async (usuarioId: number, status: StatusHistorico): Promise<Historico[]> => {
    const { data } = await api.get<Historico[]>(`/historicos/usuario/${usuarioId}/status/${status}`);
    return data;
  },
  registrar: async (payload: {
    usuarioId: number;
    obraId: number;
    status: StatusHistorico;
    dataInicio?: string | null;
    dataConclusao?: string | null;
    progresso?: string | null;
  }): Promise<Historico> => {
    const { data } = await api.post<Historico>('/historicos', payload);
    return data;
  },
  deletar: async (id: number): Promise<void> => {
    await api.delete(`/historicos/${id}`);
  },
};