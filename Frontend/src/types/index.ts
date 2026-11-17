export interface Usuario {
  id: number;
  nome: string;
  email: string;
  provider: string;
  providerId?: string;
  avatarUrl?: string;
  dataCriacao: string;
  dataAtualizacao: string;
}

export interface Genero {
  id: number;
  nome: string;
  slug: string;
  tmdbId?: number;
  dataCriacao: string;
}

export type TipoObra = 'FILME' | 'SERIE';

export interface Obra {
  id: number;
  titulo: string;
  tituloOriginal?: string;
  tipo: TipoObra;
  sinopse?: string;
  dataLancamento?: string;
  posterUrl?: string;
  backdropUrl?: string;
  duracaoMinutos?: number;
  numeroTemporadas?: number;
  notaMedia: number;
  tmdbId?: number;
  dataCriacao: string;
  generos: Genero[];
}

export interface Avaliacao {
  id: number;
  usuario: Usuario;
  obra: Obra;
  nota: number;
  comentario?: string;
  dataAvaliacao: string;
}

export type StatusHistorico = 'QUERO_VER' | 'ASSISTINDO' | 'ASSISTIDO' | 'ABANDONADO';

export interface Historico {
  id: number;
  usuario: Usuario;
  obra: Obra;
  status: StatusHistorico;
  dataInicio?: string;
  dataConclusao?: string;
  progresso?: string;
  dataAtualizacao: string;
}

export type TipoLista = 'FAVORITOS' | 'WATCHLIST' | 'CUSTOM';

export interface ListaItem {
  id: number;
  lista: Lista;
  obra: Obra;
  ordem: number;
  dataAdicao: string;
}

export interface Lista {
  id: number;
  usuario: Usuario;
  nome: string;
  tipo: TipoLista;
  descricao?: string;
  publica: boolean;
  dataCriacao: string;
  itens?: ListaItem[];
}

export interface AuthResponse {
  token: string;
  usuario: Usuario;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}