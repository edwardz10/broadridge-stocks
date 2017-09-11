package com.broadridge.stocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.broadridge.stocks.model.Bet;
import com.broadridge.stocks.model.Product;
import com.broadridge.stocks.repository.BetRepository;
import com.broadridge.stocks.repository.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoriesTest {

	private static final Logger LOG = LoggerFactory.getLogger(RepositoriesTest.class);

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private BetRepository betRepository;

	@Test
    public void productRepositoryContainsManyRecordsTest() throws Exception {
		assertThat(productRepository.findAll().size() > 100);
    }

	@Test
    public void betRepositoryContainsManyRecordsTest() throws Exception {
		assertThat(betRepository.findAll().size() > 100);
    }

	@Test
    public void productsAndBetsAreConnectedTest() throws Exception {
		List<Product> allProducts = productRepository.findAll();
		Product randomProduct = productRepository.findAll().get(new Random().nextInt(allProducts.size()));
		LOG.info("Found a random product: " + randomProduct);
		List<Bet> betsByProduct = betRepository.getBetsByProductId(randomProduct.getProductId());
		assertThat(betsByProduct.size() > 0);
    }

	@Test
    public void betInsertionWorksFineTest() throws Exception {
		List<Product> allProducts = productRepository.findAll();
		List<Bet> allBets = betRepository.findAll();
		Product randomProduct = productRepository.findAll().get(new Random().nextInt(allProducts.size()));
		LOG.info("Found a random product: " + randomProduct);
		Bet newBet = new Bet(randomProduct, 100);
		newBet.setTimestamp(new Date());
		LOG.info("Create a new bet: " + newBet);
		betRepository.save(newBet);
		List<Bet> newAllBets = betRepository.findAll();
		assertEquals(allBets.size() + 1, newAllBets.size());
    }
}
