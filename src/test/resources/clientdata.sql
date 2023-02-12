Delete FROM bank_transaction;
DELETE FROM account;
DELETE FROM client;

INSERT INTO client(id, client_name, password) VALUES (1, 'Roberto', '1234');
INSERT INTO client(id, client_name, password) VALUES (2, 'Laura', 'ab23');