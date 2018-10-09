package com.jgprogram.marketrates.port.adapter.jpa;

import com.jgprogram.marketrates.domain.model.MarketRate;
import com.jgprogram.marketrates.domain.model.MarketRateRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Repository
public class MarketRateRepositoryImpl implements MarketRateRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public void add(MarketRate marketRate) {
        em.persist(marketRate);
    }

    @Override
    public Date findLatestDate() {
        return em.createQuery(
                "SELECT mr.date FROM MarketRate mr ORDER BY mr.date DESC",
                Date.class)
                .setMaxResults(1)
                .getResultList().stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public MarketRate findByMarketCodeAndDate(String marketCode, Date date) {
        return em.createQuery(
                "SELECT mr FROM MarketRate mr WHERE mr.marketCode = :marketCode AND mr.date = :date",
                MarketRate.class)
                .setMaxResults(1)
                .setParameter("marketCode", marketCode)
                .setParameter("date", date)
                .getResultList().stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public MarketRate findByMarketCodeAndDayAndHour(String marketCode, Date day, Integer hour) {
        return em.createQuery(
                "SELECT mr FROM MarketRate mr WHERE mr.marketCode = :marketCode AND mr.day = :day AND mr.hour = :hour",
                MarketRate.class)
                .setMaxResults(1)
                .setParameter("marketCode", marketCode)
                .setParameter("day", day)
                .setParameter("hour", hour)
                .getResultList().stream()
                .findFirst()
                .orElse(null);
    }
}
