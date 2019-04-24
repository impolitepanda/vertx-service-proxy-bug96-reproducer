package reproducer.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reproducer.service.Service96Reproducer;

public class Service96ReproducerRESTVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service96ReproducerRESTVerticle.class);


    public void start(Future<Void> future) throws Exception {
        LOGGER.debug("start::IN");

        // Create a router object.
        Router router = Router.router(vertx);

        // Bind "/" to our hello message - so we are still compatible.
        router.route("/").handler(routingContext -> {

            ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));

            Future<Service96Reproducer> futureService = Future.future();
            JsonObject filter = new JsonObject().put("name", "service96reproducer0");
            EventBusService.getServiceProxyWithJsonFilter(discovery, filter, Service96Reproducer.class, futureService);

            Future<Void> futureCall0 = Future.future();
            futureService
                .compose(service96Reproducer -> service96Reproducer.callService0(futureCall0), futureCall0)
                .setHandler(voidAsyncResult -> {
                    HttpServerResponse response = routingContext.response();

                    if (voidAsyncResult.failed()) {
                        Throwable error = voidAsyncResult.cause();
                        if (error instanceof ReplyException
                            && ((ReplyException) error).failureCode() != -1) {

                                response.setStatusCode(((ReplyException) error).failureCode())
                                    .end("<h1>ERROR - SHOULD ACTUALLY HAPPEN !!! </h1>");


                            LOGGER.error("Error while treating the result: ", error.getCause());
                        } else {
                            response.setStatusCode(500)
                                .end("<h1>ERROR - BUT SHOULD ACTUALLY NOT HAPPEN IN THIS USECASE :'( </h1>");
                            LOGGER.error("Error while treating the result: ", error);
                        }

                        return;
                    }

                    response
                        .putHeader("content-type", "text/html")
                        .end("<h1>SUCCESS - BUT SHOULD ACTUALLY NOT HAPPEN IN THIS USECASE :'( </h1>");
                });


        });

        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(
                // Retrieve the port from the configuration,
                // default to 8080.
                config().getInteger("http.port", 8080),
                result -> {
                    if (result.succeeded()) {
                        future.complete();
                    } else {
                        future.fail(result.cause());
                    }
                }
            );

        LOGGER.debug("start::OUT");
    }
}