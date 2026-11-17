import { useState } from 'react';
import { IonButton, IonContent, IonInput, IonPage, IonSpinner, IonToast, useIonRouter } from '@ionic/react';
import { useAuth } from '../../contexts/AuthContext';
import '../PagesCSS/Principal.css';

export default function Login() {
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState<string | null>(null);

  const { login } = useAuth();
  const ionRouter = useIonRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro(null);
    setLoading(true);
    try {
      await login({ email, senha });
      ionRouter.push('/app/home', 'forward', 'replace');
    } catch (err: any) {
      const msg = err?.response?.data?.message ?? 'Falha ao entrar. Verifique suas credenciais.';
      setErro(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <IonPage>
      <IonContent fullscreen className="landing-content">
        <div className="landing-wrapper">
          <section className="landing-hero">
            <div className="landing-logo">
              <span className="landing-logo-icon">🎬</span>
              <h1 className="landing-title">CineTrack</h1>
            </div>
            <p className="landing-subtitle">Entre na sua conta</p>
          </section>

          <form className="landing-auth" onSubmit={handleSubmit}>
            <h2 className="landing-auth-title">Bem-vindo de volta</h2>

            <IonInput
              label="E-mail"
              labelPlacement="stacked"
              type="email"
              value={email}
              onIonInput={(e) => setEmail(e.detail.value ?? '')}
              required
              className="ion-margin-bottom"
            />

            <IonInput
              label="Senha"
              labelPlacement="stacked"
              type="password"
              value={senha}
              onIonInput={(e) => setSenha(e.detail.value ?? '')}
              required
              className="ion-margin-bottom"
            />

            <IonButton
              type="submit"
              expand="block"
              className="landing-btn-email"
              disabled={loading}
            >
              {loading ? <IonSpinner name="dots" /> : 'Entrar'}
            </IonButton>

            <p className="landing-register-text">
              Ainda não tem conta?{' '}
              <IonButton routerLink="/register" className="landing-link" fill="clear">
                Cadastre-se
              </IonButton>
            </p>

            <p className="landing-register-text">
              <IonButton routerLink="/principal" className="landing-link" fill="clear">
                Voltar
              </IonButton>
            </p>
          </form>
        </div>

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