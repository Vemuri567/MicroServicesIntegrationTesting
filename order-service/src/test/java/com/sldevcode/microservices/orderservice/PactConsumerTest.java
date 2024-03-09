
package com.sldevcode.microservices.orderservice;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sldevcode.microservices.orderservice.controller.OrderController;
import com.sldevcode.microservices.orderservice.dto.OrderRequest;
import com.sldevcode.microservices.orderservice.dto.OrderResponse;
import com.sldevcode.microservices.orderservice.dto.UserCreationRequest;
import com.sldevcode.microservices.orderservice.dto.UserCreationResponse;
import com.sldevcode.microservices.orderservice.serviceImpl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "UserService")
public class PactConsumerTest {

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private RestTemplate restTemplate;
    @Pact(consumer = "OrderService")
    public RequestResponsePact GetUsers(PactDslWithProvider builder) throws JsonProcessingException {
        String fullName="Naveen";
        String age="20";
        ObjectMapper mapper=new ObjectMapper();
        String json= mapper.writeValueAsString(Map.of("fullName",fullName,"age",age));
        return builder.given("user exist")
                .uponReceiving("Getting all users")
                .path("/users")
                .method("POST")
                .body(json)
                .willRespondWith()
                .status(200)
                .body(new PactDslJsonBody()
                        .stringType("userId")
                        .stringType("message","Successfuly Create the User")).toPact();

        /*return builder
                .given("A request to create a user")
                .uponReceiving("A request to create a user")
                .path("/users")
                .method("POST")
                .headers(headers)
                .body("{\"fullName\": \"testUser\", \"age\": 30}")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(PactDslJsonArray.arrayMinLike(1)
                        .stringType("message","Successfuly Create the User").closeObject()).toPact();*/
    }
    @Test
    @PactTestFor(pactMethod ="GetUsers",port = "9998")
    public void TestALlOrders(MockServer mockServer) throws JsonProcessingException {
        OrderRequest request=new OrderRequest();
        request.setFullName("Naveen");request.setAge("20");
        request.setOrderType("Laptop");
        request.setOrderDetails("infor");
        orderService.setBaseUrl(mockServer.getUrl());
        OrderResponse response=orderService.createOrder(request);
        ObjectMapper obj = new ObjectMapper();
        String jsonActual = obj.writeValueAsString(response.getMessage());
        Assertions.assertTrue(jsonActual.contains("Successfuly Created the Order"));

    }
}



/*
package com.sldevcode.microservices.orderservice;
import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.sldevcode.microservices.orderservice.dto.OrderRequest;
import com.sldevcode.microservices.orderservice.dto.OrderResponse;
import com.sldevcode.microservices.orderservice.serviceImpl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "UserService",port = "9997")
public class PactConsumerTest {
    @Autowired
    private OrderServiceImpl orderService;
    @Pact(consumer = "OrderService")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return builder
                .given("A request to create a user")
                .uponReceiving("A request to create a user")
                .path("/users")
                .method("POST")
                .headers(headers)
                .body("{\"fullName\": \"testUser\", \"age\": 30}")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body("{\"message\": \"Successfully created the user\"}")
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    void testCreateUser(MockServer mockServer) {


        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setFullName("testUser");
        orderRequest.setAge("30");
        orderService.setBaseUrl(mockServer.getUrl());
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        assertEquals("Successfully created the order", orderResponse.getMessage());
    }
}*/
