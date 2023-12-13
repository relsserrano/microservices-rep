package com.springex.productservice.service;

import com.springex.productservice.domain.Product;
import com.springex.productservice.dto.ProductRequestDto;
import com.springex.productservice.dto.ProductResponseDto;
import com.springex.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void create(ProductRequestDto requestDto) {
        Product product = Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product {} is created", product.getId());
    }

    public List<ProductResponseDto> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> mapProductToResponse(product)).collect(Collectors.toList());
    }

    private ProductResponseDto mapProductToResponse(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
