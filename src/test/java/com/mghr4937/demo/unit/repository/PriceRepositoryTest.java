package com.mghr4937.demo.unit.repository;

import com.mghr4937.demo.model.Price;
import com.mghr4937.demo.repository.PriceRepository;
import com.mghr4937.demo.util.EntityTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PriceRepositoryTest {
    private final PriceRepository priceRepository;

    private final EntityTestUtil entityTestUtil;

    @Autowired
    public PriceRepositoryTest(PriceRepository priceRepository, EntityTestUtil entityTestUtil) {
        this.priceRepository = priceRepository;
        this.entityTestUtil = entityTestUtil;
    }

    @Test
    public void testSave() throws Exception {
        var price = entityTestUtil.createPrice(0, 45.50F);

        var pricesIterable = priceRepository.findAll();
        List<Price> pricesList = new ArrayList<>(pricesIterable);

        assertTrue(pricesList.contains(price));
    }

    @Test
    public void testFindById() throws Exception {
        var price = entityTestUtil.createPrice(0, 45.50F);

        var result = priceRepository.findById(price.getId()).orElseThrow();
        assertNotNull(price);
        assertEquals(price.getId(), result.getId());
    }

    @Test
    public void testFindAll() throws Exception {
        var price = entityTestUtil.createPrice(0, 45.50F);

        List<Price> result = new ArrayList<>(priceRepository.findAll());
        assertEquals(5, result.size());
    }

    @Test
    public void testDeleteById() throws Exception {
        var price = entityTestUtil.createPrice(0, 45.50F);

        priceRepository.deleteById(price.getId());
        List<Price> result = new ArrayList<>(priceRepository.findAll());
        assertEquals(4, result.size());
    }

    @Test
    public void testQueryPrice() throws Exception {
        var date = LocalDateTime.of(2022, Month.MARCH, 15, 0, 0, 0);
        var price = entityTestUtil.createPrice(0, 45.50F);

        var result = priceRepository.queryPrice(date, 35999L, 1L);
        assertTrue(result.isPresent());
        assertEquals(price, result.get());
    }

    @Test
    public void testQueryPriceWithSameDateAndHighPriority() throws Exception {
        var date = LocalDateTime.of(2022, Month.MARCH, 15, 0, 0, 0);
        entityTestUtil.createPrice(0, 45.50F);
        var priceHighPriority = entityTestUtil.createPrice(1, 99.99F);

        var result = priceRepository.queryPrice(date, 35999L, 1L);
        assertTrue(result.isPresent());
        assertEquals(priceHighPriority, result.get());
    }

    @Test
    public void testQueryPriceNotFound() throws Exception {
        var date = LocalDateTime.of(2022, Month.MARCH, 15, 0, 0, 0);
        var price = entityTestUtil.createPrice(0, 45.50F);

        var result = priceRepository.queryPrice(date, 35999L, 2L);
        assertFalse(result.isPresent());
    }


}
