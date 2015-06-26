package com.cucc.vertx.demo.VertxDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

public class OracleEventBus extends AbstractVerticle{
    private Logger logger=LoggerFactory.getLogger(OracleEventBus.class);
    public static JsonObject config = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.39:1521:ngact")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "ubak").put("password", "ubak_123")
    .put("initial_pool_size", 5)
    .put("min_pool_size", 5)
    .put("max_pool_size", 30);
    @Override
    public void start() {
        JDBCClient client=OracleClient.getInstance(vertx).getClient("ngact");
        //JDBCClient client = JDBCClient.createShared(vertx, config,"zhang");
        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("insert");
        consumer.handler(message->{
            logger.info("CustomerResourceVerticle received a query: "+message.body());
            
            client.getConnection(res -> {
                if (res.succeeded()) {

                    SQLConnection connection = res.result();
                    
                    String stmt = " select user from dual";
                    
                    connection.query(stmt,res2->{
                        message.reply(res2.result().toJson());
                    });
                    connection.close(res2 ->{
                        
                    });
                } else {
                    // Failed to get connection - deal with it
                    res.cause().printStackTrace();
                    logger.error("Failed to get connection："+res.cause().getLocalizedMessage());
                }
            });
            
        });
    }
    public static void main(String[] args)
    {
        Vertx vertx=Vertx.vertx();
        vertx.deployVerticle(new OracleEventBus());
    }

}
