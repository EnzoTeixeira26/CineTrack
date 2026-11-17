import api from './api';
import type { Genero } from '../types';

export const generosService = {
  listar: async (): Promise<Genero[]> => {
    const { data } = await api.get<Genero[]>('/generos');
    return data;
  },
};