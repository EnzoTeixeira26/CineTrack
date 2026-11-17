import { useEffect, useState } from 'react';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonIcon,
  IonPage,
  useIonRouter,
} from '@ionic/react';
import { bookmarkOutline, listOutline, logOutOutline, starOutline } from 'ionicons/icons';
import { useAuth } from '../contexts/AuthContext';
import { avaliacoesService } from '../services/avaliacoes';
import { historicosService } from '../services/historicos';
import { listasService } from '../services/listas';
import './PagesCSS/Perfil.css';

export default function Perfil() {
  const { user, logout } = useAuth();
  const router = useIonRouter();
  const [stats, setStats] = useState({ avaliacoes: 0, historicos: 0, listas: 0 });

  useEffect(() => {
    if (!user) return;
    Promise.all([
      avaliacoesService.listarPorUsuario(user.id),
      historicosService.listarPorUsuario(user.id),
      listasService.listarPorUsuario(user.id),
    ])
      .then(([av, hi, li]) =>
        setStats({ avaliacoes: av.length, historicos: hi.length, listas: li.length })
      )
      .catch(() => {});
  }, [user]);

  const handleLogout = () => {
    logout();
    router.push('/principal', 'back', 'replace');
  };

  if (!user) return null;

  const inicial = user.nome.charAt(0).toUpperCase();

  return (
    <IonPage>
      <IonHeader className="ion-no-border">
        <div className="perfil-header">
          <h1>Perfil</h1>
        </div>
      </IonHeader>
      <IonContent className="perfil-content">
        <div className="perfil-top">
          <div className="perfil-avatar">
            {user.avatarUrl ? <img src={user.avatarUrl} alt={user.nome} /> : inicial}
          </div>
          <div className="perfil-info">
            <h2>{user.nome}</h2>
            <p>{user.email}</p>
          </div>
        </div>

        <div className="perfil-stats">
          <div className="perfil-stat">
            <IonIcon icon={starOutline} />
            <div className="perfil-stat-num">{stats.avaliacoes}</div>
            <div className="perfil-stat-label">Avaliações</div>
          </div>
          <div className="perfil-stat">
            <IonIcon icon={bookmarkOutline} />
            <div className="perfil-stat-num">{stats.historicos}</div>
            <div className="perfil-stat-label">Estante</div>
          </div>
          <div className="perfil-stat">
            <IonIcon icon={listOutline} />
            <div className="perfil-stat-num">{stats.listas}</div>
            <div className="perfil-stat-label">Listas</div>
          </div>
        </div>

        <div className="perfil-actions">
          <IonButton expand="block" fill="outline" color="danger" onClick={handleLogout}>
            <IonIcon slot="start" icon={logOutOutline} />
            Sair da conta
          </IonButton>
        </div>
      </IonContent>
    </IonPage>
  );
}