package com.cucc.vertx.demo.VertxDemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

public class Sender extends AbstractVerticle {

    @Override
    public void start(){
      EventBus eb = vertx.eventBus();

      // Send a message every second

      vertx.setPeriodic(1000, v -> {

        eb.send("select", "ping!", reply -> {
          if (reply.succeeded()) {
            System.out.println("A reply " + reply.result().body());
          } else {
            System.out.println("No reply");
          }
        });
        
        eb.send("insert", "ping!", reply -> {
            if (reply.succeeded()) {
              System.out.println("B reply " + reply.result().body());
            } else {
              System.out.println("No reply");
            }
          });

      });
    }
  }
