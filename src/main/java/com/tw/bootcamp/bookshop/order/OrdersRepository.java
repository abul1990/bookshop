package com.tw.bootcamp.bookshop.order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrdersRepository extends CrudRepository<Order, Long> {
    @Query("FROM Order order where order.orderDate between ?1 and ?2")
    List<Order> findOrdersByDateRange(Date startDate, Date endDate);
}
