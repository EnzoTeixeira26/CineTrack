import { useEffect, useState } from 'react';
import { IonContent, IonHeader, IonPage, IonSpinner, IonToast } from '@ionic/react';
import ObraCard from '../components/ObraCard';
import { historicosService } from '../services/historicos';
import { useAuth } from '../contexts/AuthContext';
import type { Historico, StatusHistorico } from '../types';
import './PagesCSS/MinhaEstante.css';

const FILTROS: { value: StatusHistorico | 'TODOS'; label: string }[] = [
  { value: 'TODOS', label: 'Todos' },
  { value: 'QUERO_VER', label: 'Quero ver' },
  { value: 'ASSISTINDO', label: 'Assistindo' },
  { value: 'ASSISTIDO', label: 'Assistido' },
  { value: 'ABANDONADO', label: 'Abandonado' },
];

export default function MinhaEstante() {
  const { user } = useAuth();
  const [historicos, setHistoricos] = useState<Historico[]>([]);
  const [loading, setLoading] = useState(true);
  const [filtro, setFiltro] = useState<StatusHistorico | 'TODOS'>('TODOS');
  const [erro, setErro] = useState<string | null>(null);

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    historicosService
      .listarPorUsuario(user.id)
      .then(setHistoricos)
      .catch(() => setErro('Não foi possível carregar sua estante.'))
      .finally(() => setLoading(false));
  }, [user]);

  const filtrados =
    filtro === 'TODOS' ? historicos : historicos.filter((h) => h.status === filtro);

  return (
    <IonPage>
      <IonHeader className="ion-no-border">
        <div className="estante-header">
          <h1>Minha Estante</h1>
          <p>
            {historicos.length} {historicos.length === 1 ? 'obra' : 'obras'}
          </p>
        </div>
      </IonHeader>
      <IonContent className="estante-content">
        <div className="estante-filters">
          {FILTROS.map((f) => (
            <div
              key={f.value}
              className={`estante-chip ${filtro === f.value ? 'active' : ''}`}
              onClick={() => setFiltro(f.value)}
            >
              {f.label}
            </div>
          ))}
        </div>

        {loading ? (
          <div className="estante-loading">
            <IonSpinner name="crescent" />
          </div>
        ) : filtrados.length === 0 ? (
          <div className="estante-empty">
            <div className="estante-empty-icon">📭</div>
            <p>Nada por aqui ainda.</p>
            <p className="estante-empty-sub">
              Explore o catálogo e adicione obras à sua estante.
            </p>
          </div>
        ) : (
          <div className="estante-grid">
            {filtrados.map((h) => (
              <ObraCard key={h.id} obra={h.obra} />
            ))}
          </div>
        )}

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