package com.broadridge.stocks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.broadridge.stocks.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Product findByName(String name);

	@Query(value = "select * from product p limit :page, 10", nativeQuery = true)
	List<Product> getProducts(@Param("page") int page);

}
