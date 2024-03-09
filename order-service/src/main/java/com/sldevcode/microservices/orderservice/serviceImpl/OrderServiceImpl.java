package com.sldevcode.microservices.orderservice.serviceImpl;

import com.sldevcode.microservices.orderservice.dto.OrderRequest;
import com.sldevcode.microservices.orderservice.dto.OrderResponse;
import com.sldevcode.microservices.orderservice.dto.UserCreationRequest;
import com.sldevcode.microservices.orderservice.dto.UserCreationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//import javax.xml.ws.Response;
import java.util.UUID;

@Service
public class OrderServiceImpl {

    @Autowired
    private RestTemplate restTemplate;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    String baseUrl="http://localhost:8080";

            public OrderResponse createOrder(OrderRequest orderRequest) {
                UserCreationRequest userCreationRequest = new UserCreationRequest();
                userCreationRequest.setAge(orderRequest.getAge());
                userCreationRequest.setFullName(orderRequest.getFullName());
               HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                ResponseEntity<UserCreationResponse> userCreationResponse = restTemplate.postForEntity(baseUrl+"/users" , userCreationRequest , UserCreationResponse.class);


                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setOrderId(UUID.randomUUID().toString());
                orderResponse.setUserId("id1234");
                orderResponse.setMessage("Successfuly Created the Order");

                return orderResponse;
            }

            @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
                return builder.build();
            }

}
