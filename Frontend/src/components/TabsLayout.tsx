import {
  IonIcon,
  IonLabel,
  IonRouterOutlet,
  IonTabBar,
  IonTabButton,
  IonTabs,
} from '@ionic/react';
import { Navigate, Route } from 'react-router-dom';
import { bookmarkOutline, homeOutline, listOutline, personOutline } from 'ionicons/icons';
import Home from '../pages/Home';
import MinhaEstante from '../pages/MinhaEstante';
import Listas from '../pages/Listas';
import ListaDetalhe from '../pages/ListaDetalhe';
import Perfil from '../pages/Perfil';
import './ComponentsCSS/TabsLayout.css';

export default function TabsLayout() {
  return (
    <IonTabs>
      <IonRouterOutlet>
        <Route path="/app/home" element={<Home />} />
        <Route path="/app/estante" element={<MinhaEstante />} />
        <Route path="/app/listas" element={<Listas />} />
        <Route path="/app/lista/:id" element={<ListaDetalhe />} />
        <Route path="/app/perfil" element={<Perfil />} />
        <Route path="/app" element={<Navigate to="/app/home" replace />} />
      </IonRouterOutlet>
      <IonTabBar slot="bottom" className="tabs-bar">
        <IonTabButton tab="home" href="/app/home">
          <IonIcon icon={homeOutline} />
          <IonLabel>Início</IonLabel>
        </IonTabButton>
        <IonTabButton tab="estante" href="/app/estante">
          <IonIcon icon={bookmarkOutline} />
          <IonLabel>Estante</IonLabel>
        </IonTabButton>
        <IonTabButton tab="listas" href="/app/listas">
          <IonIcon icon={listOutline} />
          <IonLabel>Listas</IonLabel>
        </IonTabButton>
        <IonTabButton tab="perfil" href="/app/perfil">
          <IonIcon icon={personOutline} />
          <IonLabel>Perfil</IonLabel>
        </IonTabButton>
      </IonTabBar>
    </IonTabs>
  );
}