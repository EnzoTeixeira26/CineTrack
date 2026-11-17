import { IonPage, IonContent, IonButton, IonIcon, useIonRouter } from '@ionic/react';
import { logoGoogle, logoApple, searchOutline, starOutline, listOutline, notificationsOutline } from 'ionicons/icons';
import CarroselPrincipal from '../components/CarroselPrincipal';
import './PagesCSS/Principal.css';

/*const router = useIonRouter();

const cadastro = () => {
    
    router.push('/register');
}*/

export default function Principal() {
  return (
    <IonPage>
      <IonContent fullscreen className="landing-content">
        <div className="landing-wrapper">

          
          <section className="landing-hero">
            <div className="landing-logo">
              <span className="landing-logo-icon">🎬</span>
              <h1 className="landing-title">CineTrack</h1>
            </div>
            <p className="landing-subtitle">
              Seu catálogo pessoal de filmes e séries.
            </p>
            <p className="landing-description">
              Organize o que você quer assistir, avalie o que já viu e descubra novas recomendações.
            </p>
          </section>

          <CarroselPrincipal />

          <section className="landing-auth">
            <h2 className="landing-auth-title">Comece a organizar sua coleção</h2>

            <IonButton expand="block" className="landing-btn-email" routerLink="/login">
              Entrar com E-mail
            </IonButton>

            <div className="landing-divider">
              <span>ou continue com</span>
            </div>

            <IonButton expand="block" fill="outline" className="landing-btn-social" onClick={() => alert('Login com Google em breve')}>
              <IonIcon slot="start" icon={logoGoogle} />
              Continuar com Google
            </IonButton>

            <IonButton expand="block" fill="outline" className="landing-btn-social" onClick={() => alert('Login com Apple em breve')}>
              <IonIcon slot="start" icon={logoApple} />
              Continuar com Apple
            </IonButton>

            <p className="landing-register-text">
              Ainda não tem conta?{' '}
              <IonButton routerLink="/register" className="landing-link" fill='clear'>Cadastre-se</IonButton>
            </p>
          </section>

          <section className="landing-features">
            <h3 className="landing-features-title">Por que usar o CineTrack?</h3>

            <div className="landing-features-list">
              <div className="landing-feature-row">
                <div className="landing-feature-icon-wrap">
                  <IonIcon icon={searchOutline} className="landing-feature-icon" />
                </div>
                <div className="landing-feature-info">
                  <h4>Busca inteligente</h4>
                  <p>Encontre filmes e séries pelo título, gênero ou ator.</p>
                </div>
              </div>

              <div className="landing-feature-row">
                <div className="landing-feature-icon-wrap">
                  <IonIcon icon={starOutline} className="landing-feature-icon" />
                </div>
                <div className="landing-feature-info">
                  <h4>Avaliações e comentários</h4>
                  <p>Registre suas notas e opiniões sobre cada obra.</p>
                </div>
              </div>

              <div className="landing-feature-row">
                <div className="landing-feature-icon-wrap">
                  <IonIcon icon={listOutline} className="landing-feature-icon" />
                </div>
                <div className="landing-feature-info">
                  <h4>Listas personalizadas</h4>
                  <p>Crie favoritos, watchlist e listas temáticas.</p>
                </div>
              </div>

              <div className="landing-feature-row">
                <div className="landing-feature-icon-wrap">
                  <IonIcon icon={notificationsOutline} className="landing-feature-icon" />
                </div>
                <div className="landing-feature-info">
                  <h4>Notificações de lançamentos</h4>
                  <p>Receba avisos quando algo que você acompanha estrear.</p>
                </div>
              </div>
            </div>
          </section>

          <footer className="landing-footer">
            <div className="landing-footer-gradient" />
            <div className="landing-footer-content">
              <p className="landing-footer-title">CineTrack © 2026</p>
              <p className="landing-footer-sub">Feito para quem ama cinema.</p>
            </div>
          </footer>

        </div>
      </IonContent>
    </IonPage>
  );
}