package com.example.springreactivemongoDB.webclient;

import com.example.springreactivemongoDB.controller.ProductController;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


public class ProductConsumer {
    @Autowired
    WebClient webClient  = WebClient.create("product-service");

    @Autowired
    private ProductController productController;



}
