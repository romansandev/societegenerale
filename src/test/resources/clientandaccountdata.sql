Delete FROM bank_transaction;
DELETE FROM account;
DELETE FROM client;

INSERT INTO client(id, client_name, password) VALUES (1, 'Roberto', '1234');
INSERT INTO client(id, client_name, password) VALUES (2, 'Laura', 'ab23');

INSERT INTO account(id, money, creation_time, client_id) VALUES (1, 2000, '2023-02-13 10:00:00', 1);
INSERT INTO account(id, money, creation_time, client_id) VALUES (2, 1000, '2023-02-13 10:00:00', 2);
INSERT INTO account(id, money, creation_time, client_id) VALUES (3, 100, '2023-02-13 10:00:00', 2);