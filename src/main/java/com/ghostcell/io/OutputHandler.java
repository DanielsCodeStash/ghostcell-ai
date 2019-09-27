package com.ghostcell.io;

import com.ghostcell.container.Order;

import java.util.List;

public class OutputHandler {

    public static void executeOrders(List<Order> orders) {

        if (!orders.isEmpty()) {
            String out = "";
            for (int i = 0; i < orders.size(); i++) {
                Order order = orders.get(i);
                boolean isLast = i == orders.size() - 1;

                if(!order.isBomb()) {
                    out += "MOVE " + order.getFrom().getId() + " " + order.getTo().getId() + " " + order.getNum();
                } else {
                    out += "BOMB " + order.getFrom().getId() + " " + order.getTo().getId();
                }

                if (!isLast)
                    out += ";";
            }
            System.out.println(out);
        } else {
            System.out.println("WAIT");

        }
    }
}
