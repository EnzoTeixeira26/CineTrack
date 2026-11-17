CREATE TABLE IF NOT EXISTS obra (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    titulo_original VARCHAR(255),
    tipo VARCHAR(10) NOT NULL,
    sinopse TEXT,
    data_lancamento DATE,
    poster_url VARCHAR(500),
    backdrop_url VARCHAR(500),
    duracao_minutos INTEGER,
    numero_temporadas INTEGER,
    nota_media NUMERIC(4, 2) DEFAULT 0.00,
    tmdb_id INTEGER UNIQUE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_obra_tipo CHECK (tipo IN ('FILME', 'SERIE'))
);

CREATE INDEX idx_obra_titulo ON obra(titulo);
CREATE INDEX idx_obra_tmdb_id ON obra(tmdb_id);