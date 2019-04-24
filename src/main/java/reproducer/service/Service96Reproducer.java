package reproducer.service;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

@VertxGen
@ProxyGen
public interface Service96Reproducer {
    void callService0(Handler<AsyncResult<Void>> resultHandler);
    void callService1(Handler<AsyncResult<Void>> resultHandler);
    void callService2(Handler<AsyncResult<Void>> resultHandler);
}
