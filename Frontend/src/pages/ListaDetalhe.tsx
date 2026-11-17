import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonIcon,
  IonModal,
  IonPage,
  IonSearchbar,
  IonSpinner,
  IonToast,
  useIonRouter,
} from '@ionic/react';
import { arrowBackOutline, closeOutline, trashOutline } from 'ionicons/icons';
import ObraCard from '../components/ObraCard';
import { listasService } from '../services/listas';
import { obrasService } from '../services/obras';
import type { Lista, ListaItem, Obra } from '../types';
import './PagesCSS/ListaDetalhe.css';

export default function ListaDetalhe() {
  const { id } = useParams<{ id: string }>();
  const router = useIonRouter();
  const listaId = Number(id);

  const [lista, setLista] = useState<Lista | null>(null);
  const [itens, setItens] = useState<ListaItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  const [showAdd, setShowAdd] = useState(false);
  const [busca, setBusca] = useState('');
  const [resultados, setResultados] = useState<Obra[]>([]);
  const [buscando, setBuscando] = useState(false);

  const carregar = async () => {
    setLoading(true);
    try {
      const [l, its] = await Promise.all([
        listasService.buscarPorId(listaId),
        listasService.listarItens(listaId),
      ]);
      setLista(l);
      setItens(its);
    } catch {
      setErro('Não foi possível carregar a lista.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [listaId]);

  useEffect(() => {
    if (!showAdd) return;
    if (!busca.trim()) {
      setResultados([]);
      return;
    }
    const t = setTimeout(async () => {
      setBuscando(true);
      try {
        const p = await obrasService.buscarPorTitulo(busca.trim(), 0, 20);
        setResultados(p.content);
      } catch {
        setResultados([]);
      } finally {
        setBuscando(false);
      }
    }, 400);
    return () => clearTimeout(t);
  }, [busca, showAdd]);

  const remover = async (obraId: number) => {
    try {
      await listasService.removerItem(listaId, obraId);
      setItens((prev) => prev.filter((i) => i.obra.id !== obraId));
    } catch {
      setErro('Não foi possível remover.');
    }
  };

  const adicionar = async (obraId: number) => {
    try {
      await listasService.adicionarItem(listaId, obraId);
      setShowAdd(false);
      setBusca('');
      setResultados([]);
      carregar();
    } catch {
      setErro('Não foi possível adicionar (talvez já esteja na lista).');
    }
  };

  return (
    <IonPage>
      <IonHeader className="ion-no-border">
        <div className="lista-detalhe-header">
          <IonIcon
            icon={arrowBackOutline}
            className="lista-detalhe-back"
            onClick={() => router.push('/app/listas', 'back', 'replace')}
          />
          <h1 className="lista-detalhe-title">{lista?.nome ?? 'Lista'}</h1>
          <IonButton
            size="small"
            style={
              {
                '--background': 'linear-gradient(90deg, #ffd700, #ff8f00)',
                '--color': '#0f0f1a',
              } as React.CSSProperties
            }
            onClick={() => setShowAdd(true)}
          >
            Adicionar
          </IonButton>
        </div>
      </IonHeader>

      <IonContent className="lista-detalhe-content">
        {loading ? (
          <div style={{ display: 'flex', justifyContent: 'center', padding: 48 }}>
            <IonSpinner name="crescent" />
          </div>
        ) : itens.length === 0 ? (
          <div className="lista-detalhe-empty">
            <div className="lista-detalhe-empty-icon">📭</div>
            <p>Essa lista está vazia.</p>
            <p style={{ fontSize: 12, color: '#6a6a8a' }}>
              Toque em "Adicionar" para incluir obras.
            </p>
          </div>
        ) : (
          <div className="lista-detalhe-grid">
            {itens.map((item) => (
              <div key={item.id} className="lista-detalhe-item">
                <div className="lista-detalhe-remove" onClick={() => remover(item.obra.id)}>
                  <IonIcon icon={trashOutline} />
                </div>
                <ObraCard obra={item.obra} />
              </div>
            ))}
          </div>
        )}

        <IonModal
          isOpen={showAdd}
          onDidDismiss={() => setShowAdd(false)}
          className="lista-modal"
        >
          <IonHeader className="ion-no-border">
            <div
              style={{
                padding: '16px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
              }}
            >
              <h2 style={{ margin: 0, fontSize: 18, fontWeight: 800 }}>Adicionar obra</h2>
              <IonIcon
                icon={closeOutline}
                style={{ fontSize: 24, cursor: 'pointer' }}
                onClick={() => setShowAdd(false)}
              />
            </div>
          </IonHeader>
          <IonContent className="lista-modal-content">
            <IonSearchbar
              value={busca}
              onIonInput={(e) => setBusca(e.detail.value ?? '')}
              placeholder="Buscar filmes e séries..."
              debounce={400}
            />
            {buscando && (
              <div style={{ textAlign: 'center', padding: 24 }}>
                <IonSpinner name="dots" />
              </div>
            )}
            {!buscando && busca.trim() && resultados.length === 0 && (
              <p style={{ textAlign: 'center', color: '#8a8aa0', padding: 24 }}>
                Nenhum resultado.
              </p>
            )}
            {!buscando && !busca.trim() && (
              <p
                style={{
                  textAlign: 'center',
                  color: '#6a6a8a',
                  padding: 24,
                  fontSize: 13,
                }}
              >
                Digite para buscar.
              </p>
            )}
            {resultados.map((o) => (
              <div key={o.id} className="lista-modal-item" onClick={() => adicionar(o.id)}>
                {o.posterUrl ? (
                  <img src={o.posterUrl} alt={o.titulo} />
                ) : (
                  <div className="lista-modal-poster-empty" />
                )}
                <div>
                  <div className="lista-modal-titulo">{o.titulo}</div>
                  <div className="lista-modal-sub">
                    {o.dataLancamento ? new Date(o.dataLancamento).getFullYear() : '—'}
                  </div>
                </div>
              </div>
            ))}
          </IonContent>
        </IonModal>

        <IonToast
          isOpen={!!erro}
          message={erro ?? ''}
          duration={3000}
          color="danger"
          onDidDismiss={() => setErro(null)}
        />
      </IonContent>
    </IonPage>
  );
}