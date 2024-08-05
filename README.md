My implementation of test task. 
It uses N-tier architecture: Repository -> Dto -> Controller.

Stack:
- SpringBoot
- Spring REST
- Spring Security
- SpringJPA
- Postrges for data storage (should work with any other SQLdb, just need to change dialect)

Сode meets the principles of SOLID as well.

Controllers are covered with tests.

## Endoints:
## GET/register
  Endpoint to create a new user.
	
* Request example:
{
    "email":"email",
    "password":"password"
}
* Response example:
{
    "id": 1,
    "email": "email"
}

The password would be encoded and stored in DB as hash.

## POST/accounts
  Create new Bank Account for user
  
  * Request example:
  {
    "accountName":"accountName2",
    "balance":"3131",
    "accountDetails":"some detailes"
  }

 * Response example:
	{
    "id": 3,
    "userEmail": "email",
    "accountName": "accountName2",
    "accountNumber": 3159667390678525750,
    "balance": 3131
	}



## GET/accounts
  Returns all user accounts (without details)
* Response example:
	[
  {
  	"id": 1,
  	"userEmail": "email",
  	"accountName": "account0",
  	"accountNumber": 6797413168527919833,
  	"balance": 3000.00
  }

## GET/accounts/detailes
Returns ALL information about account (Yes, even sensitive data. I made it this way, for it to be something diffetent from just  GET/accounts)
* Request example:
http://localhost:8080/accounts/details?accountNumber=6797413168527919833
* Response example:
{
    "id": 1,
    "user": {
        "id": 2,
        "email": "email",
        "password": "$2a$10$/KOerrMMVCLbvH8E0t3YluUlGXMLEI5Y.A317qQI8Hk2aDARM9ZbK"
    },
    "name": "account0",
    "accountNumber": 6797413168527919833,
    "balance": 3000.00,
    "accountDetails": "details0"
}

## POST/transactions/withdraw
Allows user to withdraw funds from choosen account.
* Request example:
{
    "accountNumber":"6797413168527919833",
    "amount":"1000.0"
}

* Response example:
{
    "id": 1,
    "userEmail": "email",
    "accountName": "account0",
    "accountNumber": 6797413168527919833,
    "balance": 2000.00
}

## POST/transactions/deposit
Deposit funds to chosen account. Doesn`t requires auth here.
* Request example:
  http://localhost:8080/transactions/deposit?accountNumber=6797413168527919833&value=30000000
  
* Responce example:
"true"

## POST/transactions/transfer
Allows to send money to another user`s Bank Account.
* Request example:
{
    "fromAccount": {
        "accountNumber": "3704521468010593362",
        "amount": 1032.0
    },
    "toAccount": {
        "accountNumber": "6126860087608248258"
    }
}

* Response example:
{
    "id": 2,
    "userEmail": "email1",
    "accountName": "account1",
    "accountNumber": 3704521468010593362,
    "balance": 2968.00
}





## Set up

To setup and test solution you may want to enter you DB credentials and DB Url at appliation.properties
```java
spring.datasource.url= jdbc:postgresql://localhost:5432/
spring.datasource.username= postgres
spring.datasource.password= postgres
```
