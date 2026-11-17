import { useState } from 'react';
import { IonButton, IonContent, IonInput, IonPage, IonSpinner, IonToast, useIonRouter } from '@ionic/react';
import { useAuth } from '../../contexts/AuthContext';
import '../PagesCSS/register.css';

export default function Register() {
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [confirmar, setConfirmar] = useState('');
  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState<string | null>(null);

  const { register } = useAuth();
  const ionRouter = useIonRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro(null);

    if (!nome.trim() || !email.trim() || !senha) {
      setErro('Preencha todos os campos.');
      return;
    }
    if (senha.length < 6) {
      setErro('A senha deve ter pelo menos 6 caracteres.');
      return;
    }
    if (senha !== confirmar) {
      setErro('As senhas não coincidem.');
      return;
    }

    setLoading(true);
    try {
      await register({ nome: nome.trim(), email: email.trim(), senha });
      ionRouter.push('/app/home', 'forward', 'replace');
    } catch (err: any) {
      const msg = err?.response?.data?.message ?? 'Não foi possível criar a conta.';
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
            <p className="landing-subtitle">Crie sua conta</p>
          </section>

          <form className="landing-auth" onSubmit={handleSubmit}>
            <h2 className="landing-auth-title">Comece agora</h2>

            <IonInput
              label="Nome"
              labelPlacement="stacked"
              value={nome}
              onIonInput={(e) => setNome(e.detail.value ?? '')}
              required
              className="ion-margin-bottom"
            />
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
            <IonInput
              label="Confirmar senha"
              labelPlacement="stacked"
              type="password"
              value={confirmar}
              onIonInput={(e) => setConfirmar(e.detail.value ?? '')}
              required
              className="ion-margin-bottom"
            />

            <IonButton
              type="submit"
              expand="block"
              className="landing-btn-email"
              disabled={loading}
            >
              {loading ? <IonSpinner name="dots" /> : 'Criar conta'}
            </IonButton>

            <p className="landing-register-text">
              Já tem conta?{' '}
              <IonButton routerLink="/login" className="landing-link" fill="clear">
                Entrar
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