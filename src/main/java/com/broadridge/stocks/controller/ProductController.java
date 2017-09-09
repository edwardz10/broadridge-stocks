package com.broadridge.stocks.controller;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.broadridge.stocks.model.Bet;
import com.broadridge.stocks.model.Pager;
import com.broadridge.stocks.model.Product;
import com.broadridge.stocks.repository.BetRepository;
import com.broadridge.stocks.service.BetService;
import com.broadridge.stocks.service.ProductService;

@Controller
public class ProductController {

	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

	private ProductService productService;
	private BetService betService;
	private BetRepository betRepository;

	private static final int BUTTONS_TO_SHOW = 5;
	private static final int INITIAL_PAGE = 0;

	@Value("${stocks.productsPerPage}")
	private int productsPerPage;

	private Bet newBet;
	
	@Autowired
	public ProductController(ProductService productService, BetService betService, BetRepository betRepository) {
		this.productService = productService;
		this.betService = betService;
		this.betRepository = betRepository;
	}
	
	@RequestMapping(value="/products", method = RequestMethod.GET)
	public ModelAndView products(@RequestParam("page") Optional<Integer> page,
								 @RequestParam("pageBet") Optional<Integer> pageBet,
								 @RequestParam("productId") Optional<Long> productId,
								 @RequestParam("amount") Optional<Integer> amount) {
		ModelAndView modelAndView = new ModelAndView("products");

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
		Page<Product> products = productService.getAllProductsPageable(new PageRequest(evalPage, productsPerPage));
		Pager pager = new Pager(products.getTotalPages(), products.getNumber(), BUTTONS_TO_SHOW);

		int evalPageBet = (pageBet.orElse(0) < 1) ? INITIAL_PAGE : pageBet.get() - 1;
		Page<Bet> bets = betService.getLatestBets(new PageRequest(evalPageBet, productsPerPage));
		Pager pagerBet = new Pager(bets.getTotalPages(), bets.getNumber(), BUTTONS_TO_SHOW);

		Long pId = productId.orElse(null);
		LOG.info("Product ID selected: " + pId);

		if (pId != null) {
			Product selectedProduct = productService.getProductById(pId);
			int am = amount.orElse(0);
			LOG.info("Found a product " + selectedProduct);
			LOG.info("Amount " + am);
			
			if (newBet == null) {
				newBet = new Bet();
			}

			newBet.setProduct(selectedProduct);
			newBet.setAmount(am);
			
			Page<Bet> betsByProjectId = betService.getBetsByProductId(pId, new PageRequest(evalPage, productsPerPage));
			modelAndView.addObject("pId", pId);
			modelAndView.addObject("bet", newBet);
			modelAndView.addObject("betsByProjectId", betsByProjectId);
			modelAndView.addObject("amount", am);
		}
		
		modelAndView.addObject("products", products);
		modelAndView.addObject("bets", bets);
		modelAndView.addObject("pager", pager);
		modelAndView.addObject("pagerBet", pagerBet);
		return modelAndView;
	}

	@RequestMapping(value="/newbet", method = RequestMethod.POST)
	public String addBet(@ModelAttribute Bet bet) {
		bet.setTimestamp(new Date());
		LOG.info("Bet to store: " + bet);
		betRepository.save(bet);
		return "redirect:/products";
	}

	@RequestMapping(value="/addbet", method = RequestMethod.GET)
	public ModelAndView addBet(@RequestParam("productId") Long productId, @RequestParam("page") Optional<Integer> page) {
		ModelAndView modelAndView = new ModelAndView("addbet");
		Product selectedProduct = productService.getProductById(productId);
		LOG.info("Found a product " + selectedProduct);
		Bet newBet = new Bet();
		newBet.setProduct(selectedProduct);

		int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

		Page<Bet> bets = betService.getBetsByProductId(productId, new PageRequest(evalPage, productsPerPage));
		Pager pager = new Pager(bets.getTotalPages(), bets.getNumber(), BUTTONS_TO_SHOW);
		modelAndView.addObject("bet", newBet);
		modelAndView.addObject("bets", bets);
		modelAndView.addObject("pager", pager);
		return modelAndView;
	}
}
