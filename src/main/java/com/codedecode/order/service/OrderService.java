package com.codedecode.order.service;

import com.codedecode.order.Mapper.OrderMapper;
import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.dto.UserDTO;
import com.codedecode.order.entity.Order;
import com.codedecode.order.repo.OrderRepo;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Autowired
    private RestTemplate restTemplate;
    public OrderDTO saveOrder(OrderDTOFromFE orderDetails) {
        Integer newOrderId = sequenceGenerator.generateNextOrderId();

        UserDTO userDTO = fetchUserDetailsFromUserId(orderDetails.getUserId());

        Order savedOrder =
                new Order(newOrderId, orderDetails.getFoodItemsList(),orderDetails.getRestaurant(),userDTO);

        orderRepo.save(savedOrder);
        return OrderMapper.INSTANCE.mapOrderTOOrderDTO(savedOrder);
    }

    private UserDTO fetchUserDetailsFromUserId(Integer userId) {

       return restTemplate.getForObject("http://USER-SERVICE/user/getUserById/"+userId, UserDTO.class);
    }
}
