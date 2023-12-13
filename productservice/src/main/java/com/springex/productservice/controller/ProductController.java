package com.springex.productservice.controller;

import com.springex.productservice.dto.ProductRequestDto;
import com.springex.productservice.dto.ProductResponseDto;
import com.springex.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequestDto productRequest) {
        productService.create(productRequest);
    }

    @GetMapping
    public List<ProductResponseDto> getProducts(){
        return productService.getAll();
    }
}
