package net.sayilir.shopping.service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.protobuf.util.Timestamps;
import io.grpc.stub.StreamObserver;
import net.sayilir.shopping.db.Order;
import net.sayilir.shopping.db.OrderDao;
import net.sayilir.stubs.order.OrderServiceGrpc;
import net.sayilir.stubs.order.OrderRequest;
import net.sayilir.stubs.order.OrderResponse;
/**
 * @author omersayilir
 * @Date 2024-09-07
 */
public class OrderServiceImpl extends  OrderServiceGrpc.OrderServiceImplBase {
    private Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());
    private OrderDao orderDao= new OrderDao();


    @Override
    public void getOrdersForUser(OrderRequest request, StreamObserver<OrderResponse> responseObserver){
        List<Order> orders =  orderDao.getOrderDetails(request.getUserId());
       logger.info("Got orders from OrderDao and converting to order Response proto objecets");
        List<net.sayilir.stubs.order.Order> orderForUser=orders.stream().map(order-> net.sayilir.stubs.order.Order.newBuilder()
                .setUserId(order.getUserId())
                .setOrderId(order.getOrderId())
                .setNoOfItems(order.getNoOfItems())
                .setTotalAmount(order.getTotalAmount())
                .setOrderDate(Timestamps.fromMillis(order.getOrderDate().getTime()))
                .build()).collect(Collectors.toList());

        OrderResponse response = OrderResponse.newBuilder().addAllOrder(orderForUser).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
