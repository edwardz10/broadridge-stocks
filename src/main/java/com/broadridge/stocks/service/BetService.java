package com.broadridge.stocks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.broadridge.stocks.model.Bet;
import com.broadridge.stocks.repository.BetRepository;

@Service
public class BetService {
	private static final Logger LOG = LoggerFactory.getLogger(BetService.class);

	private BetRepository betRepository;

	@Autowired
	public BetService(BetRepository betRepository) {
		this.betRepository = betRepository;
	}

	public Page<Bet> getBetsByProductId(Long productId, Pageable pageable) {
		Page<Bet> bets = betRepository.getBetsByProductId(productId, pageable);
		LOG.info("Bets by product id " + productId + ": " + bets.getNumberOfElements());
		return bets;
	}

	public Page<Bet> getLatestBets(Pageable pageable) {
		Page<Bet> bets = betRepository.getLatestBets(pageable);
		LOG.info("Latest bets: " + bets.getNumberOfElements());
		return bets;
	}
	
}
