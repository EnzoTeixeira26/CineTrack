import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { IonButton,IonContent,IonHeader,IonIcon,IonPage,IonSpinner,IonToast,useIonRouter } from '@ionic/react';
import { arrowBackOutline, star } from 'ionicons/icons';
import { obrasService } from '../services/obras';
import { avaliacoesService } from '../services/avaliacoes';
import { historicosService } from '../services/historicos';
import { useAuth } from '../contexts/AuthContext';
import type { Avaliacao, Obra, StatusHistorico } from '../types';
import './PagesCSS/ObraDetalhe.css';

const STATUS_OPTIONS: { value: StatusHistorico; label: string }[] = [
  { value: 'QUERO_VER', label: 'Quero ver' },
  { value: 'ASSISTINDO', label: 'Assistindo' },
  { value: 'ASSISTIDO', label: 'Assistido' },
  { value: 'ABANDONADO', label: 'Abandonado' },
];

export default function ObraDetalhe() {
  const { id } = useParams<{ id: string }>();
  const router = useIonRouter();
  const { user, isAuthenticated } = useAuth();

  const [obra, setObra] = useState<Obra | null>(null);
  const [reviews, setReviews] = useState<Avaliacao[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  const [statusAtual, setStatusAtual] = useState<StatusHistorico | null>(null);
  const [nota, setNota] = useState(8);
  const [comentario, setComentario] = useState('');
  const [minhaReviewId, setMinhaReviewId] = useState<number | null>(null);
  const [enviandoReview, setEnviandoReview] = useState(false);

  useEffect(() => {
    let active = true;
    const obraId = Number(id);

    Promise.all([
      obrasService.buscarPorId(obraId),
      avaliacoesService.listarPorObra(obraId),
    ])
      .then(([o, rs]) => {
        if (!active) return;
        setObra(o);
        setReviews(rs);
        if (user) {
          const minha = rs.find((r) => r.usuario.id === user.id);
          if (minha) {
            setMinhaReviewId(minha.id);
            setNota(Number(minha.nota));
            setComentario(minha.comentario ?? '');
          }
          historicosService
            .listarPorUsuario(user.id)
            .then((hist) => {
              const meu = hist.find((h) => h.obra.id === obraId);
              if (meu) setStatusAtual(meu.status);
            })
            .catch(() => {});
        }
      })
      .catch(() => setErro('Não foi possível carregar a obra.'))
      .finally(() => active && setLoading(false));

    return () => { active = false; };
  }, [id, user]);

  const handleStatus = async (status: StatusHistorico) => {
    if (!user) {
      setErro('Faça login para salvar na sua estante.');
      return;
    }
    try {
      await historicosService.registrar({
        usuarioId: user.id,
        obraId: Number(id),
        status,
      });
      setStatusAtual(status);
    } catch {
      setErro('Erro ao salvar status.');
    }
  };

  const submitReview = async () => {
    if (!user || !obra) return;
    setEnviandoReview(true);
    try {
      if (minhaReviewId) {
        await avaliacoesService.atualizar(minhaReviewId, { nota, comentario });
      } else {
        const criada = await avaliacoesService.criar({
          usuarioId: user.id,
          obraId: obra.id,
          nota,
          comentario,
        });
        setMinhaReviewId(criada.id);
      }
      const rs = await avaliacoesService.listarPorObra(obra.id);
      setReviews(rs);
      const atualizada = await obrasService.buscarPorId(obra.id);
      setObra(atualizada);
    } catch {
      setErro('Não foi possível salvar sua avaliação.');
    } finally {
      setEnviandoReview(false);
    }
  };

  if (loading) {
    return (
      <IonPage>
        <IonContent className="detalhe-content">
          <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 80 }}>
            <IonSpinner name="crescent" />
          </div>
        </IonContent>
      </IonPage>
    );
  }

  if (!obra) {
    return (
      <IonPage>
        <IonContent className="detalhe-content">
          <div style={{ padding: 24, textAlign: 'center', color: '#b0b0c0' }}>
            Obra não encontrada.
            <IonButton expand="block" onClick={() => router.goBack()}>
              Voltar
            </IonButton>
          </div>
        </IonContent>
      </IonPage>
    );
  }

  const ano = obra.dataLancamento ? new Date(obra.dataLancamento).getFullYear() : null;
  const notaMedia = Number(obra.notaMedia ?? 0);

  return (
    <IonPage>
      <IonHeader className="ion-no-border">
        <div style={{ padding: '16px', background: 'transparent' }}>
          <IonIcon
            icon={arrowBackOutline}
            style={{ fontSize: 24, color: '#fff', cursor: 'pointer' }}
            onClick={() => router.goBack()}
          />
        </div>
      </IonHeader>

      <IonContent className="detalhe-content">
        <div className="detalhe-backdrop">
          {obra.backdropUrl && <img src={obra.backdropUrl} alt={obra.titulo} />}
        </div>

        <div className="detalhe-top">
          <div className="detalhe-poster">
            {obra.posterUrl ? (
              <img src={obra.posterUrl} alt={obra.titulo} />
            ) : (
              <div className="detalhe-poster-empty">🎬</div>
            )}
          </div>
          <div className="detalhe-info" style={{ paddingTop: 0 }}>
            <h1>{obra.titulo}</h1>
            {obra.tituloOriginal && (
              <p className="detalhe-original">{obra.tituloOriginal}</p>
            )}
            <div className="detalhe-meta">
              <span>{ano ?? '—'}</span>
              <span>{obra.tipo === 'FILME' ? 'Filme' : 'Série'}</span>
              <span className="detalhe-rating">
                <IonIcon icon={star} /> {notaMedia.toFixed(1)}
              </span>
            </div>
          </div>
        </div>

        {obra.sinopse && (
          <div className="detalhe-section">
            <h3>Sinopse</h3>
            <p className="detalhe-sinopse">{obra.sinopse}</p>
          </div>
        )}

        {obra.generos && obra.generos.length > 0 && (
          <div className="detalhe-section">
            <h3>Gêneros</h3>
            <div className="detalhe-chips">
              {obra.generos.map((g) => (
                <span key={g.id} className="detalhe-chip">{g.nome}</span>
              ))}
            </div>
          </div>
        )}

        <div className="detalhe-actions">
          {isAuthenticated ? (
            <div className="detalhe-status-row">
              {STATUS_OPTIONS.map((opt) => (
                <div
                  key={opt.value}
                  className={`detalhe-status-btn ${statusAtual === opt.value ? 'active' : ''}`}
                  onClick={() => handleStatus(opt.value)}
                >
                  {opt.label}
                </div>
              ))}
            </div>
          ) : (
            <div
              className="detalhe-login-prompt"
              onClick={() => router.push('/login', 'forward')}
            >
              Entre para salvar na sua estante →
            </div>
          )}
        </div>

        {isAuthenticated && (
          <div className="detalhe-review-form">
            <h3>{minhaReviewId ? 'Sua avaliação' : 'Avaliar esta obra'}</h3>
            <div className="detalhe-nota-input">
              <input
                type="range"
                min="0"
                max="10"
                step="0.5"
                value={nota}
                onChange={(e) => setNota(Number(e.target.value))}
              />
              <span className="detalhe-nota-valor">{nota.toFixed(1)}</span>
            </div>
            <textarea
              className="detalhe-textarea"
              placeholder="Escreva um comentário (opcional)..."
              value={comentario}
              onChange={(e) => setComentario(e.target.value)}
            />
            <IonButton
              expand="block"
              className="landing-btn-email"
              style={{ marginTop: 12, marginBottom: 0 }}
              onClick={submitReview}
              disabled={enviandoReview}
            >
              {enviandoReview ? <IonSpinner name="dots" /> : 'Salvar avaliação'}
            </IonButton>
          </div>
        )}

        <div className="detalhe-section">
          <h3>Comentários ({reviews.filter((r) => r.comentario).length})</h3>
          {reviews.filter((r) => r.comentario).length === 0 ? (
            <p className="detalhe-sinopse">Ainda não há comentários.</p>
          ) : (
            <div className="detalhe-reviews-list">
              {reviews
                .filter((r) => r.comentario)
                .map((r) => {
                  const inicial = r.usuario.nome?.charAt(0).toUpperCase() ?? '?';
                  const data = new Date(r.dataAvaliacao).toLocaleDateString('pt-BR');
                  return (
                    <div key={r.id} className="detalhe-review">
                      <div className="detalhe-review-header">
                        <div className="detalhe-review-user">
                          <div className="detalhe-review-avatar">
                            {r.usuario.avatarUrl ? (
                              <img src={r.usuario.avatarUrl} alt={r.usuario.nome} />
                            ) : (
                              inicial
                            )}
                          </div>
                          <div>
                            <div className="detalhe-review-name">{r.usuario.nome}</div>
                            <div className="detalhe-review-date">{data}</div>
                          </div>
                        </div>
                        <div className="detalhe-review-nota">
                          <IonIcon icon={star} /> {Number(r.nota).toFixed(1)}
                        </div>
                      </div>
                      <p className="detalhe-review-comment">{r.comentario}</p>
                    </div>
                  );
                })}
            </div>
          )}
        </div>

        <div style={{ height: 32 }} />

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