import { IonIcon } from '@ionic/react';
import { star } from 'ionicons/icons';
import { useNavigate } from 'react-router-dom';
import type { Obra } from '../types';
import './ComponentsCSS/ObraCard.css';

export default function ObraCard({ obra }: { obra: Obra }) {
  const navigate = useNavigate();
  const ano = obra.dataLancamento ? new Date(obra.dataLancamento).getFullYear() : null;

  return (
    <div className="obra-card" onClick={() => navigate(`/obra/${obra.id}`)}>
      <div className="obra-card-poster">
        {obra.posterUrl ? (
          <img src={obra.posterUrl} alt={obra.titulo} loading="lazy" />
        ) : (
          <div className="obra-card-poster-empty">🎬</div>
        )}
      </div>
      <div className="obra-card-info">
        <h4>{obra.titulo}</h4>
        <div className="obra-card-meta">
          <span>{ano ?? '—'}</span>
          <span className="obra-card-rating">
            <IonIcon icon={star} /> {Number(obra.notaMedia ?? 0).toFixed(1)}
          </span>
        </div>
      </div>
    </div>
  );
}