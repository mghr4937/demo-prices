package com.mghr4937.demo.unit.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mghr4937.demo.util.EntityTestUtil;
import com.mghr4937.demo.web.dto.BrandDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BrandControllerTest {
    private static final String BRAND_NAME = "TESTBRAND";
    private static final String NOT_FOUND_NAME = "NONE";
    private static final String NOT_FOUND_ID = "/99";
    private static final String URL = "/brand";
    private static final String SEARCH_FIND_BY_NAME = "/search/findByName?name=";

    private final EntityTestUtil entityTestUtil;
    private final ObjectMapper objectMapper;

    private final MockMvc mvc;

    @Autowired
    public BrandControllerTest(EntityTestUtil entityTestUtil, ObjectMapper objectMapper, MockMvc mvc) {
        this.entityTestUtil = entityTestUtil;
        this.objectMapper = objectMapper;
        this.mvc = mvc;
    }

    @Test
    public void whenPostBrand_thenReturn200() throws Exception {
        var brand = entityTestUtil.getBrandDtoFromFile("/brand.json");
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brand)))
                .andExpect(status().isOk());

        mvc.perform(get(URL.concat(SEARCH_FIND_BY_NAME.concat(brand.getName())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(brand.getName()));
    }


    @Test
    public void whenPostBrandWithEmptyName_thenReturn400() throws Exception {
        String content = entityTestUtil.retrieveFileContent("/brand_empty_name.json");
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenGetAllBrands_thenReturn200() throws Exception {
        mvc.perform(get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void whenGetWithId_thenReturnBrand() throws Exception {
        var brand = entityTestUtil.createBrand(BRAND_NAME);

        mvc.perform(get(URL.concat("/" + brand.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(brand.getId()))
                .andExpect(jsonPath("$.name").value(brand.getName()));
    }

    @Test
    public void whenGetWithBadId_thenReturn404() throws Exception {
        mvc.perform(get(URL.concat(NOT_FOUND_ID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenFindByName_thenReturnBrand() throws Exception {
        var brand = entityTestUtil.createBrand(BRAND_NAME);

        mvc.perform(get(URL.concat(SEARCH_FIND_BY_NAME + brand.getName()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.id").value(brand.getId()))
                .andExpect(jsonPath("$.name").value(brand.getName()));
    }

    @Test
    public void whenFindByNotFoundName_thenReturn404() throws Exception {
        mvc.perform(get(URL.concat(SEARCH_FIND_BY_NAME.concat(NOT_FOUND_NAME)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
