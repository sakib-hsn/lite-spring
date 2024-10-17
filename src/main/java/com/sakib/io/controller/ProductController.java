package com.sakib.io.controller;


import com.sakib.io.litespring.MethodType;
import com.sakib.io.litespring.annotation.*;
import com.sakib.io.models.Product;
import com.sakib.io.models.dto.AddProductRequest;
import com.sakib.io.models.dto.AddProductResponse;
import com.sakib.io.service.ProductService;


@RestController(url = "/api")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(url = "/products", type = MethodType.POST)
    public AddProductResponse addProduct(@RequestBody AddProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());

        String id = productService.addProduct(product);

        AddProductResponse addProductResponse = new AddProductResponse();
        addProductResponse.setId(id);

        return addProductResponse;
    }

    @RequestMapping(url = "/products/{id}", type = MethodType.GET)
    public Product getProduct(@PathVariable("id") String id) {
        return productService.getProduct(id);
    }

}
