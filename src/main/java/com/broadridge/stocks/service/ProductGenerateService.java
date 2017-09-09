package com.broadridge.stocks.service;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.broadridge.stocks.model.Bet;
import com.broadridge.stocks.model.Product;
import com.broadridge.stocks.repository.BetRepository;
import com.broadridge.stocks.repository.ProductRepository;
import com.mifmif.common.regex.Generex;

@Service
public class ProductGenerateService {

	private static final Logger LOG = LoggerFactory.getLogger(ProductGenerateService.class);

	private ProductRepository productRepository;
	private BetRepository betRepository;

	private String regexp = "[A-Z]{2,6}[0-9]{4}";
	private Generex generator;
	private Random rnd;

	@Autowired
	public ProductGenerateService(ProductRepository productRepository, BetRepository betRepository) {
		this.productRepository = productRepository;
		this.betRepository = betRepository;
	}	
	
	@Value("${stocks.productAmount}")
	private Long productAmount;

	@Value("${stocks.minBetsPerProduct}")
	private Integer minBetsPerProduct;

	@Value("${stocks.maxBetsPerProduct}")
	private Integer maxBetsPerProduct;

	@Value("${stocks.maxAmount}")
	private Integer maxAmount;

	@PostConstruct
	public void initialize() {
		generator = new Generex(regexp);
		rnd = new Random();
		
		generateAndSaveProducts();
		generateAndSaveBets();
	}

	private void generateAndSaveBets() {
		List<Product> allProducts = productRepository.findAll();

		for (Product p : allProducts) {
			int betsAvailable = betRepository.getBetsByProductId(p.getProductId()).size();
			int betsNeeded = generateBetsPerProduct();

			LOG.info("Bets available per product " + p.getProductId()
				+ ": " + betsAvailable + ", bets needed: " + betsNeeded);
			
			if (betsNeeded > betsAvailable) {
				LOG.info("Need to generate " + (betsNeeded - betsAvailable) + " bets");
				
				for (int i = 0; i < betsNeeded - betsAvailable; i++) {
					Bet b = generateBet(p);
					LOG.info("Generated bet: " + b);
					betRepository.save(b);
				}
			}
		}
		
	}

	private void generateAndSaveProducts() {
		long availableProducts = productRepository.count();
		long productsMissing = productAmount - availableProducts;

		LOG.info(availableProducts + " products are available; " + productsMissing + " we have to generate");
		
		for (long i = 0; i < productsMissing; i++) {
			Product product = generateProduct();
			LOG.info("Generated a new Product: " + product);
			productRepository.save(product);
		}
	}

	private Product generateProduct() {
		String randomName = generateName();
		Float randomPrice = generatePrice();
		return new Product(randomName, randomPrice);
	}

	private Bet generateBet(Product product) {
		Bet bet = new Bet(product, generateAmount());
		bet.setTimestamp(new Date());
		return bet;
	}

	private Integer generateAmount() {
		return rnd.nextInt(maxAmount);
	}
	
	private Integer generateBetsPerProduct() {
		return minBetsPerProduct + rnd.nextInt(maxBetsPerProduct - minBetsPerProduct);
	}
	
	private String generateName() {
		String name;
		Product existingProduct;

		do {
			name = generator.random();
			existingProduct = productRepository.findByName(name);
			if (existingProduct != null) {
				LOG.warn("The product '" + name + "' already exists, regenerate...");
			}
		} while (existingProduct != null);
		
		return name;
	}

	private Float generatePrice() {
		return rnd.nextFloat() * 100.0f;
	}
	
}
