# vertx-service-proxy bug 96 reproducer

## ToC

- [Procedure](#procedure)
- [Expected results](#expected-results)
- [Observed behavior](#observed-behavior)


## Procedure

- Build using maven
- The unit test contains two "tests" that actually DON'T repoduce the problem and give the expected result. (and also don't test anything... I use them in debug mode with my IDE for the moment)
- To actually reproduce the problem, launch the 4 verticles using  "io.vertx.core.Launcher run" command with "-cluster" argument then call http://localhost:8080/ and look at the log for svc2 and svc1.

## Expected results

- SVC2 fails with a NotImplementedException which extends MyException, which itself extends ServiceException.
- SVC1 should display the following two lines in the logs:
  ```
  callService1::receivedError::Type::io.vertx.serviceproxy.ServiceException
  callService1::receivedError::FailureCode::501
  ```
- SCV0 should display the following two lines in the logs:
  ```
  callService1::receivedError::Type::io.vertx.serviceproxy.ServiceException
  callService1::receivedError::FailureCode::501
  ```
## Observed behavior

- SVC2 fails with a NotImplementedException which extends MyException, which itself extends ServiceException.
- SVC1 displays the following two lines in the logs:
  ```
  callService1::receivedError::Type::class io.vertx.core.eventbus.ReplyException
  callService1::receivedError::FailureCode::501
  ```
- SCV0 displays the following two lines in the logs:
  ```
  callService1::receivedError::Type::io.vertx.serviceproxy.ServiceException
  callService1::receivedError::FailureCode::-1
  ```