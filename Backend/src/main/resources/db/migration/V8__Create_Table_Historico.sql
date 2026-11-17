CREATE TABLE IF NOT EXISTS historico (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    obra_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'QUERO_VER',
    data_inicio DATE,
    data_conclusao DATE,
    progresso VARCHAR(50),
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historico_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_historico_obra FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    CONSTRAINT uq_historico_usuario_obra UNIQUE (usuario_id, obra_id),
    CONSTRAINT chk_historico_status CHECK (status IN ('QUERO_VER', 'ASSISTINDO', 'ASSISTIDO', 'ABANDONADO'))
);

CREATE INDEX idx_historico_usuario ON historico(usuario_id);
CREATE INDEX idx_historico_obra ON historico(obra_id);
CREATE INDEX idx_historico_status ON historico(status);