package jp.co.sss.spring_test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring_test.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
