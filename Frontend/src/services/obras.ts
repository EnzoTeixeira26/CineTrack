import api from './api';
import type { Obra, Page } from '../types';

export const obrasService = {
  listar: async (page = 0, size = 20): Promise<Page<Obra>> => {
    const { data } = await api.get<Page<Obra>>('/obras', { params: { page, size } });
    return data;
  },
  buscarPorId: async (id: number): Promise<Obra> => {
    const { data } = await api.get<Obra>(`/obras/${id}`);
    return data;
  },
  buscarPorTitulo: async (termo: string, page = 0, size = 20): Promise<Page<Obra>> => {
    const { data } = await api.get<Page<Obra>>('/obras/busca', { params: { termo, page, size } });
    return data;
  },
  buscarPorTipo: async (tipo: 'FILME' | 'SERIE', page = 0, size = 20): Promise<Page<Obra>> => {
    const { data } = await api.get<Page<Obra>>(`/obras/tipo/${tipo}`, { params: { page, size } });
    return data;
  },
  buscarPorGenero: async (generoId: number, page = 0, size = 20): Promise<Page<Obra>> => {
    const { data } = await api.get<Page<Obra>>(`/obras/genero/${generoId}`, { params: { page, size } });
    return data;
  },
};