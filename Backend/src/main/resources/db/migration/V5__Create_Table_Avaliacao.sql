CREATE TABLE IF NOT EXISTS avaliacao (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    obra_id BIGINT NOT NULL,
    nota NUMERIC(3, 1) NOT NULL,
    comentario TEXT,
    data_avaliacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_avaliacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_avaliacao_obra FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    CONSTRAINT uq_avaliacao_usuario_obra UNIQUE (usuario_id, obra_id),
    CONSTRAINT chk_avaliacao_nota CHECK (nota >= 0 AND nota <= 10)
);

CREATE INDEX idx_avaliacao_usuario ON avaliacao(usuario_id);
CREATE INDEX idx_avaliacao_obra ON avaliacao(obra_id);