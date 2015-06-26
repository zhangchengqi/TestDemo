package com.cucc.vertx.demo.VertxDemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

public class OracleEventHandle extends AbstractVerticle{
    private Logger logger=LoggerFactory.getLogger(OracleEventHandle.class);
    public static JsonObject config = new JsonObject()
    .put("url", "jdbc:oracle:thin:@10.124.0.42:1521:tact1")
    .put("driver_class", "oracle.jdbc.driver.OracleDriver")
    .put("user", "uop_act1").put("password", "uop_act1")
    .put("initial_pool_size", 8)
    .put("min_pool_size", 8)
    .put("max_pool_size", 30);
    @Override
    public void start() {
        //JDBCClient client = JDBCClient.createShared(vertx, config,"chengqi");
        JDBCClient client=OracleClient.getInstance(vertx).getClient("act1");
        MessageConsumer<JsonObject> consumer = vertx.eventBus().consumer("select");
        consumer.handler(message->{
            //logger.info("A received a query: "+message.body());
            
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
}
