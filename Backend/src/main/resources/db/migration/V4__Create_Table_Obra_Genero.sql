CREATE TABLE IF NOT EXISTS obra_genero (
    obra_id BIGINT NOT NULL,
    genero_id BIGINT NOT NULL,

    PRIMARY KEY (obra_id, genero_id),
    CONSTRAINT fk_obra_genero_obra FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    CONSTRAINT fk_obra_genero_genero FOREIGN KEY (genero_id) REFERENCES genero(id) ON DELETE CASCADE
);