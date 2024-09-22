package com.example.demo;

import com.example.demo.Utils.Utils;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepository =
            mock(UserRepository.class);
    private final OrderRepository orderRepository =
            mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        Utils.injectObject(orderController,
                "userRepository",
                userRepository);
        Utils.injectObject(orderController,
                "orderRepository",
                orderRepository);

    }

    @Test
    public void submitFunctionTest(){
        User user = createUser();
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Desciption");
        item.setPrice(BigDecimal.valueOf(10.0));

        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        doReturn(user).when(userRepository).findByUsername("anhduc");

        ResponseEntity<UserOrder> responseEntity =
                orderController.submit("anhduc");
        assertNotNull(responseEntity);
        // Status
        assertEquals(200, responseEntity.getStatusCodeValue());
        // Username
        assertEquals("anhduc", responseEntity
                .getBody()
                .getUser().getUsername());
    }

    @Test
    public void submitButNotFoundTest(){
        ResponseEntity<UserOrder> responseEntity =
                orderController.submit("anhduc");
        // Not Null
        assertNotNull(responseEntity);
        // Status
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void GetOrderByUserTest(){
        User user = createUser();
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(20.0));

        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        doReturn(user).when(userRepository)
                .findByUsername("Item");
        ResponseEntity<List<UserOrder>> responseEntity =
                orderController
                        .getOrdersForUser("Item");
        // Not Null
        assertNotNull(responseEntity);
        // Status
        assertEquals(200, responseEntity.getStatusCodeValue());

    }

    @Test
    public void GetOrderByNotFoundUser(){
        ResponseEntity<List<UserOrder>> responseEntity =
                orderController.getOrdersForUser("anhduc");
        // Not Null
        assertNotNull(responseEntity);
        // status
        assertEquals(404, responseEntity.getStatusCodeValue());

    }

    public static Cart createCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<Item>());

        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    public static User createUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("anhduc");
        user.setPassword("anhduc168");

        user.setCart(createCart());
        return user;
    }
}
