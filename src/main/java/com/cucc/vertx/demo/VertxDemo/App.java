package com.cucc.vertx.demo.VertxDemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Vertx;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger=LoggerFactory.getLogger(App.class);

    public static void main(String[] args)
    {
        logger.info( "Hello World!" );
        Vertx vertx=Vertx.vertx();
        OracleClient.getInstance(vertx);
        vertx.deployVerticle(new OracleEventBus(),res->{
            if (res.succeeded()) {
                vertx.deployVerticle(new OracleEventHandle(),res2->{
                    if (res.succeeded()) {
                        vertx.deployVerticle(new Sender());
                    }
                }); 
            }

        });

    }
}
