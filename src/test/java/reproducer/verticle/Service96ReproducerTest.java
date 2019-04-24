package reproducer.verticle;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import reproducer.service.Service96Reproducer;

@RunWith(VertxUnitRunner.class)
public class Service96ReproducerTest {

    Vertx vertx;
    ServiceDiscovery discovery;

    @Before
    public void before(TestContext context) {

        ClusterManager mgr = new HazelcastClusterManager();
        VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, context.asyncAssertSuccess( vertx -> {
            this.vertx = vertx;
            this.discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions());
            this.vertx.deployVerticle(Service96Reproducer0Verticle.class.getName(), context.asyncAssertSuccess(s -> {
                this.vertx.deployVerticle(Service96Reproducer1Verticle.class.getName(), context.asyncAssertSuccess(s2 -> {
                    this.vertx.deployVerticle(Service96Reproducer2Verticle.class.getName(), context.asyncAssertSuccess(s3 -> {
                        this.vertx.deployVerticle(Service96ReproducerRESTVerticle.class.getName(), context.asyncAssertSuccess());
                    }));
                }));
            }));
        }));
    }

    @After
    public void after(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void bug96reproducer(TestContext context) {
        Async async = context.async();

        Future<Service96Reproducer> futureService = Future.future();

        JsonObject filter = new JsonObject().put("name", "service96reproducer0");
        EventBusService.getServiceProxyWithJsonFilter(discovery, filter, Service96Reproducer.class, futureService);

        Future<Void> call0Future = Future.future();
        futureService
            .compose(service96Reproducer -> service96Reproducer.callService0(call0Future), call0Future)
            .setHandler(voidAsyncResult -> {
                context.assertEquals(((ReplyException)voidAsyncResult.cause()).failureCode(), 501);
                async.complete();
            });
    }

    @Test
    public void bug96reproducerThroughREST(TestContext context) {
        Async async = context.async();

        vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
            context.assertEquals(response.statusCode(), 501);
            async.complete();
        });
    }
}
