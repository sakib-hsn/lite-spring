package com.sakib.io.controller;


import com.sakib.io.litespring.enums.MethodType;
import com.sakib.io.litespring.annotation.RequestMapping;
import com.sakib.io.litespring.annotation.RequestParam;
import com.sakib.io.litespring.annotation.RestController;
import com.sakib.io.models.Product;
import com.sakib.io.models.dto.SearchResponse;
import com.sakib.io.service.ProductService;
import com.sakib.io.service.SearchService;

import java.util.List;

@RestController(url = "/api")
public class SearchController {
    private ProductService productService;

    private SearchService searchService;

    public SearchController(ProductService productService, SearchService searchService) {
        this.productService = productService;
        this.searchService = searchService;
    }

    @RequestMapping(url = "/search", type = MethodType.GET)
    public SearchResponse search(@RequestParam("query") String query) {
        List<Product> productList = searchService.search(query);
        SearchResponse searchResponse = new SearchResponse();
        searchResponse.setProducts(productList);
        return searchResponse;
    }
}
