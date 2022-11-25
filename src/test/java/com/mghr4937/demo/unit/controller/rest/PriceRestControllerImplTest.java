package com.mghr4937.demo.unit.controller.rest;

import com.mghr4937.demo.model.Price;
import com.mghr4937.demo.repository.BrandRepository;
import com.mghr4937.demo.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Currency;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class PriceRestControllerImplTest {
    private static final String URL = "/api/price";
    private static final String PRICE_JSON = "{\"brand\":{\"id\":1,\"name\":\"ZARA\"},\"currency\":\"EUR\",\"" +
            "endDate\":\"2022-11-25T00:12:32.966Z\", \"id\":0, \"price\":0,\"priceList\": 0,\"priority\":0," +
            "\"productId\":0,\"startDate\":\"2022-11-25T00:15:30.000Z\"}";

    @Autowired
    PriceRepository priceRepository;
    @Autowired
    BrandRepository brandRepository;

    @Autowired
    private MockMvc mvc;

    @Test
    public void whenPostPrice_thenReturn200() throws Exception {
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRICE_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllPrices_thenReturn200() throws Exception {
        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void whenGetWithId_thenReturn200() throws Exception {
        var price = createPrice();
        mvc.perform(get(URL.concat("/" + price.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(price.getId()))
                .andExpect(jsonPath("$.price").value(price.getPrice()));
    }

    @Test
    public void whenGetWithId_thenReturn404() throws Exception {
        mvc.perform(get(URL.concat("/99"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Price createPrice() {
        var brand = brandRepository.getReferenceById(1L);
        var price = Price.builder().brand(brand)
                .startDate(LocalDateTime.of(2022, Month.MARCH, 1, 0, 0, 0))
                .endDate(LocalDateTime.of(2022, Month.MARCH, 31, 23, 59, 59))
                .priceList(1)
                .productId(35455)
                .priority(0)
                .price(85.50)
                .currency(Currency.getInstance("EUR"))
                .build();


        return priceRepository.save(price);
    }
}
