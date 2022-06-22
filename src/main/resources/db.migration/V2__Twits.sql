CREATE TABLE twit
(
    id BIGSERIAL PRIMARY KEY,
    content VARCHAR(160),
    twit_owner BIGINT REFERENCES account (id)
);

CREATE TABLE subscribes
(
    id BIGSERIAL PRIMARY KEY,
    subscriber_id BIGINT REFERENCES account (id),
    user_id BIGINT REFERENCES account (id)
);
