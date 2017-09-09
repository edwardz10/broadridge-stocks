package com.broadridge.stocks.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.broadridge.stocks.model.Product;
import com.broadridge.stocks.repository.ProductRepository;

@Service
public class ProductService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

	private ProductRepository productRepository;

	
	private long pageCount;
	
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@PostConstruct
	public void initialize() {
		LOG.info("Page count: " + pageCount);
	}
	
	public List<Product> getProducts(int page) {
		List<Product> products = productRepository.getProducts(page);
		LOG.info("Products selected: " + products + ", page=" + page);
		return products;
	}

	public Page<Product> getAllProductsPageable(Pageable pageable) {
		Page<Product> products = productRepository.findAll(pageable);
		
		LOG.info("Products selected: " + products + ", pageable=" + pageable);
		return products;
	}

	public Product getProductById(Long productId) {
		return productRepository.findOne(productId);
	}
	
}
