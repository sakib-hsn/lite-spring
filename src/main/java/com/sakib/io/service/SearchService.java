package com.sakib.io.service;


import com.sakib.io.litespring.annotation.Autowired;
import com.sakib.io.litespring.annotation.Component;
import com.sakib.io.models.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchService {

    private ProductService productService;

    public SearchService(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> search(String name) {
        List<Product> filterProducts = new ArrayList<>();

        List<Product> products = productService.getAllProducts();
        for(Product product : products) {
            if(product.getName().toLowerCase().contains(name)) filterProducts.add(product);
        }
        return filterProducts;
    }
}
