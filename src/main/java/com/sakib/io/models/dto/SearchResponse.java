package com.sakib.io.models.dto;

import com.sakib.io.models.Product;
import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private List<Product> products;
}
