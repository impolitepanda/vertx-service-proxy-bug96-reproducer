package reproducer.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reproducer.exception.NotImplementedException;

public class Service96ReproducerImpl implements Service96Reproducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service96ReproducerImpl.class);

    private final ServiceDiscovery discovery;

    public Service96ReproducerImpl(ServiceDiscovery discovery) {
        this.discovery = discovery;
    }

    @Override
    public void callService0(Handler<AsyncResult<Void>> resultHandler) {
        LOGGER.debug("callService0::IN");

        Future<Void> resultFuture = Future.future();
        getServiceProxy("service96reproducer1")
            .compose(service96Reproducer -> service96Reproducer.callService1(resultFuture), resultFuture)
            .setHandler(voidAsyncResult -> {
                if (voidAsyncResult.failed()) {
                    LOGGER.debug("callService0::receivedError::Type::{}", voidAsyncResult.cause().getClass());
                    LOGGER.debug("callService0::receivedError::FailureCode::{}", ((ReplyException) voidAsyncResult.cause()).failureCode());
                    resultHandler.handle(Future.failedFuture(voidAsyncResult.cause()));
                    return;
                }
                resultHandler.handle(Future.succeededFuture());
            });

        LOGGER.debug("callService0::OUT");
    }
    @Override
    public void callService1(Handler<AsyncResult<Void>> resultHandler) {
        LOGGER.debug("callService1::IN");

        Future<Void> resultFuture = Future.future();
        getServiceProxy("service96reproducer2")
            .compose(service96Reproducer -> service96Reproducer.callService2(resultFuture), resultFuture)
            .setHandler(voidAsyncResult -> {
                if (voidAsyncResult.failed()) {
                    LOGGER.debug("callService1::receivedError::Type::{}", voidAsyncResult.cause().getClass());
                    LOGGER.debug("callService1::receivedError::FailureCode::{}", ((ReplyException) voidAsyncResult.cause()).failureCode());
                    resultHandler.handle(Future.failedFuture(voidAsyncResult.cause()));
                    return;
                }
                resultHandler.handle(Future.succeededFuture());
            });

        LOGGER.debug("callService1::OUT");
    }

    @Override
    public void callService2(Handler<AsyncResult<Void>> resultHandler) {
        LOGGER.debug("callService2::IN");

        resultHandler.handle(Future.failedFuture(new NotImplementedException("Error for testing")));

        LOGGER.debug("callService2::OUT");
    }

    private Future<Service96Reproducer> getServiceProxy(final String serviceName) {
        LOGGER.debug("getService::IN");

        Future<Service96Reproducer> future = Future.future();
        JsonObject filter = new JsonObject().put("name", serviceName);
        EventBusService.getServiceProxyWithJsonFilter(discovery, filter, Service96Reproducer.class, future);

        LOGGER.debug("getService::OUT");
        return future;
    }
}
