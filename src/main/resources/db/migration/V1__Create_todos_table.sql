CREATE TABLE todos
(
    id        INT PRIMARY KEY,
    title     VARCHAR(255),
    completed BOOLEAN
);

INSERT INTO todos
VALUES (1, 'Build test environment.', false);
