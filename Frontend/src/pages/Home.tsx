import { useCallback, useEffect, useState } from 'react';
import { IonContent, IonHeader, IonIcon, IonInfiniteScroll, IonInfiniteScrollContent, IonPage, IonSearchbar, IonSpinner, useIonRouter} from '@ionic/react';
import { logOutOutline } from 'ionicons/icons';
import type { InfiniteScrollCustomEvent } from '@ionic/react';
import ObraCard from '../components/ObraCard';
import { obrasService } from '../services/obras';
import { generosService } from '../services/generos';
import { useAuth } from '../contexts/AuthContext';
import type { Genero, Obra, Page, TipoObra } from '../types';
import './PagesCSS/Home.css';

type TipoFiltro = 'TODOS' | TipoObra;

export default function Home() {
  const [obras, setObras] = useState<Obra[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  const [termo, setTermo] = useState('');
  const [tipo, setTipo] = useState<TipoFiltro>('TODOS');
  const [generoId, setGeneroId] = useState<number | null>(null);
  const [generos, setGeneros] = useState<Genero[]>([]);

  const { user, logout } = useAuth();
  const router = useIonRouter();

  useEffect(() => {
    generosService.listar().then(setGeneros).catch(() => setGeneros([]));
  }, []);

  const fetchPage = useCallback(
    async (pageNumber: number): Promise<Page<Obra>> => {
      if (termo.trim()) return obrasService.buscarPorTitulo(termo.trim(), pageNumber);
      if (generoId) return obrasService.buscarPorGenero(generoId, pageNumber);
      if (tipo !== 'TODOS') return obrasService.buscarPorTipo(tipo, pageNumber);
      return obrasService.listar(pageNumber);
    },
    [termo, tipo, generoId]
  );

  const reload = useCallback(async () => {
    setLoading(true);
    try {
      const data = await fetchPage(0);
      setObras(data.content);
      setPage(1);
      setHasMore(!data.last);
    } catch {
      setObras([]);
      setHasMore(false);
    } finally {
      setLoading(false);
    }
  }, [fetchPage]);

  useEffect(() => {
    reload();
  }, [reload]);

  const loadMore = async (event: InfiniteScrollCustomEvent) => {
    if (loading || !hasMore) {
      event.target.complete();
      return;
    }
    setLoading(true);
    try {
      const data = await fetchPage(page);
      setObras((prev) => [...prev, ...data.content]);
      setPage((p) => p + 1);
      setHasMore(!data.last);
    } finally {
      setLoading(false);
      event.target.complete();
    }
  };

  const handleLogout = () => {
    logout();
    router.push('/principal', 'back', 'replace');
  };

  return (
    <IonPage>
      <IonHeader className="ion-no-border">
        <div
          className="home-header"
          style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}
        >
          <div>
            <h1>CineTrack</h1>
            <p>Olá{user ? `, ${user.nome.split(' ')[0]}` : ''} 👋</p>
          </div>
          {user && (
            <IonIcon
              icon={logOutOutline}
              style={{ fontSize: 22, color: '#ffd700', cursor: 'pointer' }}
              onClick={handleLogout}
            />
          )}
        </div>
      </IonHeader>

      <IonContent className="home-content">
        <IonSearchbar
          className="home-search"
          value={termo}
          onIonInput={(e) => setTermo(e.detail.value ?? '')}
          debounce={400}
          placeholder="Buscar filmes e séries..."
        />

        <div className="home-filters">
          <div
            className={`home-filter-chip ${tipo === 'TODOS' && !generoId ? 'active' : ''}`}
            onClick={() => { setTipo('TODOS'); setGeneroId(null); }}
          >
            Todos
          </div>
          <div
            className={`home-filter-chip ${tipo === 'FILME' && !generoId ? 'active' : ''}`}
            onClick={() => { setTipo('FILME'); setGeneroId(null); }}
          >
            Filmes
          </div>
          <div
            className={`home-filter-chip ${tipo === 'SERIE' && !generoId ? 'active' : ''}`}
            onClick={() => { setTipo('SERIE'); setGeneroId(null); }}
          >
            Séries
          </div>
          {generos.map((g) => (
            <div
              key={g.id}
              className={`home-filter-chip ${generoId === g.id ? 'active' : ''}`}
              onClick={() => setGeneroId(generoId === g.id ? null : g.id)}
            >
              {g.nome}
            </div>
          ))}
        </div>

        <div className="home-grid">
          {obras.map((obra) => (
            <ObraCard key={obra.id} obra={obra} />
          ))}

          {!loading && obras.length === 0 && (
            <div className="home-empty">
              <div className="home-empty-icon">🎬</div>
              <p>Nenhuma obra encontrada</p>
            </div>
          )}
        </div>

        {loading && obras.length === 0 && (
          <div className="home-loading">
            <IonSpinner name="crescent" />
          </div>
        )}

        <IonInfiniteScroll onIonInfinite={loadMore} disabled={!hasMore}>
          <IonInfiniteScrollContent loadingSpinner="dots" />
        </IonInfiniteScroll>
      </IonContent>
    </IonPage>
  );
}