package com.tw.bootcamp.bookshop.order;

import com.tw.bootcamp.bookshop.error.DateValidator;
import com.tw.bootcamp.bookshop.error.InvalidDateException;
import com.tw.bootcamp.bookshop.purchase.OrderAddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderService {

    @Autowired
    OrdersRepository ordersRepository;


    public Order createOrder(Order order) {
        return ordersRepository.save(order);
    }


    public List<OrderResponse> fetchAll() {
        List<Order> orderList = (List<Order>) ordersRepository.findAll();
        return getOrderResponses(orderList);

    }

    public List<OrderResponse> fetchOrdersByDateRange(String startDate, String endDate) throws InvalidDateException {
        Date fromDate = DateValidator.createDate(startDate);
        Date toDate = DateValidator.createDate(endDate);
        DateValidator.validateDateRange(fromDate, toDate);
        List<Order> orderList = ordersRepository.findOrdersByDateRange(fromDate, toDate);
        return getOrderResponses(orderList);

    }


    private List<OrderResponse> getOrderResponses(List<Order> orderList) {
        List<OrderResponse> orderResponseList = new ArrayList<>();
        if (CollectionUtils.isEmpty(orderList)) {
            return orderResponseList;
        }
        for (Order order : orderList) {
            orderResponseList.add(generateOrderResponse(order));
        }
        return orderResponseList;
    }


    private OrderResponse generateOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setBookISBN(order.getBook()
                .getIsbnNumber());
        orderResponse.setBookName(order.getBook()
                .getName());
        orderResponse.setBookId(order.getBook()
                .getId());
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setCustomerName(order.getCustomerName());
        orderResponse.setCustomerPhoneNumber(order.getMobileNumber());
        orderResponse.setNoOfCopies(order.getNoOfCopies());
        orderResponse.setId(order.getId());
        orderResponse.setTotalPrice(order.getTotalPrice());

        orderResponse.setAddress(createAddress(order));
        return orderResponse;
    }

    private OrderAddressDTO createAddress(Order order) {
        OrderAddressDTO address = new OrderAddressDTO();
        address.setLineNoOne(order.getAddressLineNoOne());
        address.setLineNoTwo(order.getAddressLineNoTwo());
        address.setCity(order.getAddressCity());
        address.setCountry(order.getAddressCountry());
        address.setState(order.getAddressState());
        address.setPinCode(order.getAddressPinCode());
        return address;
    }


}
