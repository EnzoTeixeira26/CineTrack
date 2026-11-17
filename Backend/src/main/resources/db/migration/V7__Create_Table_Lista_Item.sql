CREATE TABLE IF NOT EXISTS lista_item (
    id BIGSERIAL PRIMARY KEY,
    lista_id BIGINT NOT NULL,
    obra_id BIGINT NOT NULL,
    ordem INTEGER NOT NULL DEFAULT 0,
    data_adicao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_lista_item_lista FOREIGN KEY (lista_id) REFERENCES lista(id) ON DELETE CASCADE,
    CONSTRAINT fk_lista_item_obra FOREIGN KEY (obra_id) REFERENCES obra(id) ON DELETE CASCADE,
    CONSTRAINT uq_lista_item_lista_obra UNIQUE (lista_id, obra_id)
);

CREATE INDEX idx_lista_item_lista ON lista_item(lista_id);
CREATE INDEX idx_lista_item_obra ON lista_item(obra_id);