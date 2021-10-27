### Building

### User
Current specification does not say how users should login into Deribit.
To stub this for moment application use my test user with credentials
```
ndo4HHXM   
J5UtE25BjXiq6mRQyzRnw_hAS2XtvIAEK21Z3tNQ7Fw
```
This can be changed in `app/services/DeribitURLs.scala`

To log into <https://test.deribit.com/account/BTC/me> use `arek.stryjski@gmail.com` and `CopperExc10`.


### Available queries
#### Balances
<http://localhost:9000/balances>

#### History of withdrawals and deposits 
<http://localhost:9000/history>

#### Withdraw
<http://localhost:9000/withdraw?amount=0.000005&currency=BTC&address=2NFcyFkJXPhaa5tokSKSwwwYu3HZL1Zyc8z>

#### Transfer
<http://localhost:9000/transfer?amount=0.00001&currency=BTC&destination=34624>


### Questions about specification
* "for all available currencies" - Does it mean there could be more than the 3 available in test environment?
* What should return to client after sub-account transfer?
* How to send something back from sub-account?

### Problems
* I come accros this message:
  "The withdrawal address 2NFcyFkJXPhaa5tokSKSwwwYu3HZL1Zyc8z will be available Thu, 28 Oct 2021 23:12:37 GMT"
  It was created in <https://test.deribit.com/account/BTC/withdraw> on Monday and I believe it must be in Address Book before testing withdrawals.
  It is just copied from <https://legacy.test.deribit.com/dericoins/> where I did transfer of BTC into test environment.

### TODO
* Better error handling
* Authentication token should be replaced after expire
* Tests with mocked WSClient
* Test database
* getWithdrawals and getDeposits should handle big results (>100)



### Task description
Implement the web service that returns the data from Deribit Exchange through REST API.
It is allowed to use any programming language and libraries you are familiar with. (Java, Scala, any other).

Test environment: https://test.deribit.com/    
Documentation: https://docs.deribit.com/

Methods that should be implemented:
1. Getting the user's current balances and reserved funds for all available currencies (‘reserved’ means are not available for withdrawal). This data must be stored in the relational database and updated by each call of  the method, if it is needed.
2. Getting history of withdrawals and deposits.
3. Withdraw from exchange to external crypto address.
4. Transfer between main account and sub account.

What we are expecting from you:
1. Instructions on how to build and run the service. (you can choose any build tool - sbt, gradle, etc. It is preferable if we can run the service by one-line instructions)
2. Link to Github.
3. Structure of curl queries with which we can check the service.

* Optional Task:
Delivery the service in the Docker container.

What we are waiting from you:
DockerFile for building, and instruction for running.

