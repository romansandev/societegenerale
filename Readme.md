# Exercise for Societe Generale

## Requirements:
- Database that allows:
  - Save money on client's accounts
  - Withdraw money from client's accounts
  - Get a historic of transactions for a client.

## Implementation:
H2 database with 3 tables:
- Client table, to store client data
- Account talbe, to store accounts data (each client may have multiple accounts)
- BankTransaction table, to store information about the transactions made on tables

The relationships between tables are implemented in both senses where it makes sense, and as FetchType.LAZY to avoid
unnecesary queries and data retrieval from data base.

##Tests for the requirements and more:
On the tests, I have implemented a different unitary tests for each table repository, that already tests
all the requierements.
On top of that, I have implemented ExerciseApplicationTests to test the access to the endpoints.

## Endpoints for the requirements:
They are on BankTransactionController, to save and withdraw money, the same endpoint is used:
Post -> /account/{accountId}/transaction/{money}/{operationType}

There are logs that will explain failure situations, such as accountId not found, or not supported opperation, on top
of that, it is possible that the custom exception BrokeAccountException is thrown if more money than is left on an 
account is withdrawn from it. The system won't allow this operation and will throw the exception.

Get -> /client/transaction/historic : body : ClientDto client

It would be implemented also by receiving only the clientId, but since the other endpoint was already receiving the 
parameters as PathVariables, I chose to implement this endpoint differently. it will return the list of bank transactions
within a ResponseEntity object.