package com.mghr4937.demo.configuration;

import com.mghr4937.demo.model.Brand;
import com.mghr4937.demo.model.Price;
import com.mghr4937.demo.repository.BrandRepository;
import com.mghr4937.demo.repository.PriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

@Component
@Slf4j
public class LoadDatabaseBean {

    private static final String CURRENCY_CODE = "EUR";

    @Bean
    public CommandLineRunner initDatabase(BrandRepository brandRepository, PriceRepository priceRepository) {
        return args -> {
            log.info("Starting DB Preload");
            log.info("Preloading Brands");
            var brands = new ArrayList<Brand>();
            brands.add(Brand.builder().name("ZARA").build());
            brands.add(Brand.builder().name("ZARA HOME").build());
            brands.add(Brand.builder().name("ZARA KIDS").build());
            brandRepository.saveAll(brands);
            log.info("Brands Loaded: {}", brands);

            log.info("Preloading Prices");
            var prices = new ArrayList<Price>();

            prices.add(Price.builder().brand(brands.get(0))
                    .startDate(LocalDateTime.of(2020, Month.JUNE, 14, 0, 0, 0))
                    .endDate(LocalDateTime.of(2020, Month.DECEMBER, 31, 23, 59, 59))
                    .priceList(1)
                    .productId(35455L)
                    .priority(0)
                    .price(35.50F)
                    .currency(CURRENCY_CODE)
                    .build());

            prices.add(Price.builder().brand(brands.get(0))
                    .startDate(LocalDateTime.of(2020, Month.JUNE, 14, 15, 0, 0))
                    .endDate(LocalDateTime.of(2020, Month.JUNE, 14, 18, 30, 0))
                    .priceList(2)
                    .productId(35455L)
                    .priority(1)
                    .price(25.45F)
                    .currency(CURRENCY_CODE)
                    .build());

            prices.add(Price.builder().brand(brands.get(0))
                    .startDate(LocalDateTime.of(2020, Month.JUNE, 15, 0, 0, 0))
                    .endDate(LocalDateTime.of(2020, Month.JUNE, 15, 11, 0, 0))
                    .priceList(3)
                    .productId(35455L)
                    .priority(1)
                    .price(30.50F)
                    .currency(CURRENCY_CODE)
                    .build());

            prices.add(Price.builder().brand(brands.get(0))
                    .startDate(LocalDateTime.of(2020, Month.JUNE, 15, 16, 0, 0))
                    .endDate(LocalDateTime.of(2020, Month.DECEMBER, 31, 23, 59, 59))
                    .priceList(4)
                    .productId(35455L)
                    .priority(1)
                    .price(38.95F)
                    .currency(CURRENCY_CODE)
                    .build());

            priceRepository.saveAll(prices);
            log.info("Prices Loaded: {}", prices);
            log.info("Database Loaded");
        };
    }
}
