package com.mghr4937.demo.web.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mghr4937.demo.web.dto.PriceDto;
import com.mghr4937.demo.web.dto.QueryPriceResponseDto;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "Price API")
@RequestMapping("/price")
@Validated
public interface PriceOperations {

    @GetMapping
    List<PriceDto> getAll();

    @PostMapping
    public PriceDto save(@Valid @RequestBody PriceDto newPrice);

    @GetMapping("/{id}")
    public PriceDto getPrice(@PathVariable Long id);

    @DeleteMapping("/{id}")
    public void deletePrice(@PathVariable Long id);

    @GetMapping("/query")
    public QueryPriceResponseDto getQueryPrice(@RequestParam("date")
                                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'") @Valid
                                               String date,
                                               @NotNull @Valid @RequestParam("productId") Long productId,
                                               @NotNull @Valid @RequestParam("brandId") Long brandId) throws Exception;

}
