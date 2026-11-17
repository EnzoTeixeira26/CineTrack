CREATE TABLE IF NOT EXISTS lista (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nome VARCHAR(150) NOT NULL,
    tipo VARCHAR(20) NOT NULL DEFAULT 'CUSTOM',
    descricao TEXT,
    publica BOOLEAN NOT NULL DEFAULT FALSE,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_lista_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_lista_tipo CHECK (tipo IN ('FAVORITOS', 'WATCHLIST', 'CUSTOM'))
);

CREATE INDEX idx_lista_usuario ON lista(usuario_id);