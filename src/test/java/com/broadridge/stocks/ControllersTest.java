package com.broadridge.stocks;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;

import com.broadridge.stocks.controller.ProductController;
import com.broadridge.stocks.model.Product;
import com.broadridge.stocks.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllersTest {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductController productController;

	@Autowired
	private ProductRepository productRepository;
	
	@Test
    public void listProductWithoutNewBetTest() throws Exception {

		Optional<Integer> page = Optional.of(1);
		Optional<Integer> pageBet = Optional.of(1);
		Optional<Integer> pageNewBet = Optional.empty();
		Optional<Long> productId = Optional.empty();
		Optional<Integer> amount = Optional.empty();
		 
		ModelAndView modelAndView = productController.products(page, pageBet, pageNewBet, productId, amount);
		LOG.info("modelAndView: " + modelAndView);

		assertEquals(modelAndView.getViewName(), "products");
		assertEquals(modelAndView.getModelMap().containsKey("pId"), false);
    }

	@Test
    public void listProductWithNewBetTest() throws Exception {
		List<Product> allProducts = productRepository.findAll();
		Product randomProduct = productRepository.findAll().get(new Random().nextInt(allProducts.size()));

		Optional<Integer> page = Optional.of(1);
		Optional<Integer> pageBet = Optional.of(1);
		Optional<Integer> pageNewBet = Optional.empty();
		Optional<Long> productId = Optional.of(randomProduct.getProductId());
		Optional<Integer> amount = Optional.empty();
		 
		ModelAndView modelAndView = productController.products(page, pageBet, pageNewBet, productId, amount);
		LOG.info("modelAndView: " + modelAndView);

		assertEquals(modelAndView.getViewName(), "products");
		assertEquals(modelAndView.getModelMap().containsKey("pId"), true);
		assertEquals(modelAndView.getModelMap().get("amount"), 0);
    }
}
