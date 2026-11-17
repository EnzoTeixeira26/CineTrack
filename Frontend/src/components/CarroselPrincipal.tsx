import './ComponentsCSS/CarroselPrincipal.css';

interface CarroselPrincipalProps {
  imagens?: string[];
  velocidade?: number; // em segundos
  altura?: number;     // em px
}

const imagensPadrao = [
  'https://image.tmdb.org/t/p/w500/8UlWHLMpgZm9bx6QYh0NFoq67TZ.jpg',
  'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
  'https://image.tmdb.org/t/p/w500/1E5baAaEse26fej7uHcjOgEE2t2.jpg',
  'https://image.tmdb.org/t/p/w500/3bhkrj58Vtu7enYsRolD1fZdja1.jpg',
  'https://image.tmdb.org/t/p/w500/2LqaLgk4Z226KkgPJuiOQ58wvrm.jpg',
];

export default function CarroselPrincipal({
  imagens = imagensPadrao,
  velocidade = 30,
  altura = 165,
}: CarroselPrincipalProps) {
  return (
    <section className="carrossel-principal">
      <div
        className="carrossel-principal-track"
        style={{ animationDuration: `${velocidade}s` }}
      >
        {imagens.map((src, index) => (
          <div
            key={`original-${index}`}
            className="carrossel-principal-item"
            style={{ height: `${altura}px` }}
          >
            <img src={src} alt={`Filme ${index + 1}`} loading="lazy" />
          </div>
        ))}
        {imagens.map((src, index) => (
          <div
            key={`duplicado-${index}`}
            className="carrossel-principal-item"
            style={{ height: `${altura}px` }}
          >
            <img src={src} alt={`Filme duplicado ${index + 1}`} loading="lazy" />
          </div>
        ))}
      </div>
    </section>
  );
}