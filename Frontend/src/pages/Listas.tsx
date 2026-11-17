import { useEffect, useState } from 'react';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonIcon,
  IonInput,
  IonModal,
  IonPage,
  IonSpinner,
  IonTextarea,
  IonToast,
} from '@ionic/react';
import { addOutline, closeOutline, trashOutline } from 'ionicons/icons';
import { useNavigate } from 'react-router-dom';
import { listasService } from '../services/listas';
import { useAuth } from '../contexts/AuthContext';
import type { Lista, TipoLista } from '../types';
import './PagesCSS/Listas.css';

const TIPO_LABEL: Record<TipoLista, string> = {
  FAVORITOS: 'Favoritos',
  WATCHLIST: 'Watchlist',
  CUSTOM: 'Personalizada',
};

export default function Listas() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [listas, setListas] = useState<Lista[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [nome, setNome] = useState('');
  const [descricao, setDescricao] = useState('');
  const [publica, setPublica] = useState(false);
  const [enviando, setEnviando] = useState(false);
  const [erro, setErro] = useState<string | null>(null);

  const carregar = () => {
    if (!user) return;
    setLoading(true);
    listasService
      .listarPorUsuario(user.id)
      .then(setListas)
      .catch(() => setErro('Não foi possível carregar suas listas.'))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    carregar();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [user]);

  const criarLista = async () => {
    if (!user) return;
    if (!nome.trim()) {
      setErro('Dê um nome à lista.');
      return;
    }
    setEnviando(true);
    try {
      await listasService.criar({
        usuarioId: user.id,
        nome: nome.trim(),
        tipo: 'CUSTOM',
        descricao: descricao.trim() || undefined,
        publica,
      });
      setShowModal(false);
      setNome('');
      setDescricao('');
      setPublica(false);
      carregar();
    } catch {
      setErro('Não foi possível criar a lista.');
    } finally {
      setEnviando(false);
    }
  };

  const deletarLista = async (e: React.MouseEvent, id: number) => {
    e.stopPropagation();
    if (!confirm('Excluir esta lista?')) return;
    try {
      await listasService.deletar(id);
      carregar();
    } catch {
      setErro('Não foi possível excluir.');
    }
  };

  return (
    <IonPage>
      <IonHeader className="ion-no-border">
        <div className="listas-header">
          <h1>Listas</h1>
          <IonButton className="listas-add-btn" onClick={() => setShowModal(true)}>
            <IonIcon slot="icon-only" icon={addOutline} />
          </IonButton>
        </div>
      </IonHeader>
      <IonContent className="listas-content">
        {loading ? (
          <div style={{ display: 'flex', justifyContent: 'center', padding: 48 }}>
            <IonSpinner name="crescent" />
          </div>
        ) : listas.length === 0 ? (
          <div className="listas-empty">
            <div className="listas-empty-icon">📚</div>
            <p>Você ainda não tem listas.</p>
            <p style={{ fontSize: 12, color: '#6a6a8a' }}>
              Crie sua primeira lista no botão +.
            </p>
          </div>
        ) : (
          <div className="listas-list">
            {listas.map((l) => (
              <div
                key={l.id}
                className="lista-card"
                onClick={() => navigate(`/app/lista/${l.id}`)}
              >
                <div className="lista-delete-btn" onClick={(e) => deletarLista(e, l.id)}>
                  <IonIcon icon={trashOutline} />
                </div>
                <div className="lista-card-nome">{l.nome}</div>
                {l.descricao && <p className="lista-card-desc">{l.descricao}</p>}
                <div className="lista-card-meta">
                  <span
                    className={`lista-tipo-badge ${l.tipo === 'CUSTOM' ? 'custom' : ''}`}
                  >
                    {TIPO_LABEL[l.tipo]}
                  </span>
                  {l.publica && <span className="lista-publica-badge">Pública</span>}
                </div>
              </div>
            ))}
          </div>
        )}

        <IonModal
          isOpen={showModal}
          onDidDismiss={() => setShowModal(false)}
          className="listas-modal"
        >
          <IonHeader className="ion-no-border">
            <div
              style={{
                padding: '20px 16px',
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
              }}
            >
              <h2 style={{ margin: 0, fontSize: 18, fontWeight: 800 }}>Nova lista</h2>
              <IonIcon
                icon={closeOutline}
                style={{ fontSize: 24, cursor: 'pointer' }}
                onClick={() => setShowModal(false)}
              />
            </div>
          </IonHeader>
          <IonContent className="listas-modal-content">
            <div className="listas-form">
              <IonInput
                label="Nome"
                labelPlacement="stacked"
                value={nome}
                onIonInput={(e) => setNome(e.detail.value ?? '')}
                placeholder="Ex.: Filmes para o fim de semana"
              />
              <IonTextarea
                label="Descrição (opcional)"
                labelPlacement="stacked"
                value={descricao}
                onIonInput={(e) => setDescricao(e.detail.value ?? '')}
                rows={3}
                autoGrow
              />
              <label
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  gap: 8,
                  fontSize: 14,
                  color: '#b0b0c0',
                }}
              >
                <input
                  type="checkbox"
                  checked={publica}
                  onChange={(e) => setPublica(e.target.checked)}
                />
                Lista pública (outros usuários podem ver)
              </label>
              <IonButton expand="block" onClick={criarLista} disabled={enviando}>
                {enviando ? <IonSpinner name="dots" /> : 'Criar lista'}
              </IonButton>
            </div>
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