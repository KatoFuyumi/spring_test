package jp.co.sss.spring_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring_test.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer>{

	List<Review> findByProductId(Integer productId);
}
