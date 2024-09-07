package net.sayilir.shopping.client;

import io.grpc.Channel;
import net.sayilir.stubs.order.OrderServiceGrpc;
import net.sayilir.stubs.order.Order;
import net.sayilir.stubs.order.OrderRequest;
import net.sayilir.stubs.order.OrderResponse;
import java.util.List;


/**
 * @author omersayilir
 * @Date 2024-09-07
 */
public class OrderClient {

    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    public OrderClient(Channel channel){
        orderServiceBlockingStub= OrderServiceGrpc.newBlockingStub(channel);
    }
    public List<Order> getOrders(int userId){
        OrderRequest orderRequest= OrderRequest.newBuilder().setUserId(userId).build();
        OrderResponse orderResponse =orderServiceBlockingStub.getOrdersForUser(orderRequest);
        return orderResponse.getOrderList();
    }

}
