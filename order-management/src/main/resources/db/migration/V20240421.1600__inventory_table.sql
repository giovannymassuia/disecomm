CREATE TABLE inventory
(
    sku        VARCHAR(50) PRIMARY KEY,
    on_hand    INT       NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TRIGGER update_inventory_updated_at
    BEFORE UPDATE
    ON inventory
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
