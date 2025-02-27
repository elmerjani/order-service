CREATE TABLE orders
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT                                                            NOT NULL,
    quantity    INT                                                               NOT NULL,
    total_price DECIMAL(10, 2)                                                    NOT NULL,
    status      VARCHAR(20) CHECK (status IN ('CREATED', 'PROCESSING', 'FAILED')) NOT NULL
);