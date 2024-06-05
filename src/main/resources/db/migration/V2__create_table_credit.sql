CREATE TABLE credit
(
    id                    BIGINT AUTO_INCREMENT NOT NULL,
    credit_code           BINARY(16)            NOT NULL,
    credit_value          DECIMAL               NOT NULL,
    day_first_installment date                  NOT NULL,
    number_of_installment INT                   NOT NULL,
    status                SMALLINT              NULL,
    customer_id           BIGINT                NULL,
    CONSTRAINT pk_credit PRIMARY KEY (id)
);

ALTER TABLE credit
    ADD CONSTRAINT uc_credit_creditcode UNIQUE (credit_code);

ALTER TABLE credit
    ADD CONSTRAINT FK_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);