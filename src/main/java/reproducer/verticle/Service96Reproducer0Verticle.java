package reproducer.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.serviceproxy.ServiceBinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reproducer.service.Service96Reproducer;
import reproducer.service.Service96ReproducerImpl;

public class Service96Reproducer0Verticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service96Reproducer0Verticle.class);

    @Override
    public void start(Future<Void> startFuture) {
        LOGGER.debug("start::IN");

        ServiceDiscovery discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));

        Service96Reproducer service96Reproducer = new Service96ReproducerImpl(discovery);

        new ServiceBinder(vertx)
            .setAddress("service96reproducer0")
            .register(Service96Reproducer.class, service96Reproducer);

        Record record0 = EventBusService.createRecord("service96reproducer0", "service96reproducer0", Service96Reproducer.class);

        Future<Record> publish0Result = Future.future();

        discovery.publish(record0, publish0Result);

        publish0Result
            .map((Void) null)
            .setHandler(startFuture);

        LOGGER.debug("start::OUT");
    }


}