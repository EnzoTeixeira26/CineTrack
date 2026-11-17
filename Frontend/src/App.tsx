import { Navigate, Route } from 'react-router-dom';
import { IonApp, IonRouterOutlet, setupIonicReact } from '@ionic/react';
import { IonReactRouter } from '@ionic/react-router';
import Principal from './pages/Principal';
import Login from './pages/Usuario/Login';
import Register from './pages/Usuario/Register';
import ObraDetalhe from './pages/ObraDetalhe';
import TabsLayout from './components/TabsLayout';
import ProtectedRoute from './components/ProtectedRoute';

import '@ionic/react/css/core.css';
import '@ionic/react/css/normalize.css';
import '@ionic/react/css/structure.css';
import '@ionic/react/css/typography.css';
import '@ionic/react/css/padding.css';
import '@ionic/react/css/float-elements.css';
import '@ionic/react/css/text-alignment.css';
import '@ionic/react/css/text-transformation.css';
import '@ionic/react/css/flex-utils.css';
import '@ionic/react/css/display.css';
import '@ionic/react/css/palettes/dark.system.css';

import './theme/variables.css';

setupIonicReact();

const App: React.FC = () => (
  <IonApp>
    <IonReactRouter>
      <IonRouterOutlet>
        <Route path="/principal" element={<Principal />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/obra/:id" element={<ObraDetalhe />} />
        <Route
          path="/app/*"
          element={
            <ProtectedRoute>
              <TabsLayout />
            </ProtectedRoute>
          }
        />
        <Route path="/" element={<Navigate to="/principal" replace />} />
        <Route path="*" element={<Navigate to="/principal" replace />} />
      </IonRouterOutlet>
    </IonReactRouter>
  </IonApp>
);

export default App;