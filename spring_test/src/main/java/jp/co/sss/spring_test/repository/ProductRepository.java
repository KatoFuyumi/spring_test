package jp.co.sss.spring_test.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring_test.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	List<Product> findByProductNameContaining(String keyword);
	List<Product> findByCategory_CategoryId(Integer category_id);
	List<Product> findByProductNameContainingAndCategory_CategoryId(String keyword, Integer category_id);

}
