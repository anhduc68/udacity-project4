package com.example.demo;

import com.example.demo.Utils.Utils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class UserControllerTest {

    private UserController userController;

    private User user;
    private final UserRepository userRepository =
            mock(UserRepository.class);
    private final CartRepository cartRepository =
            mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder =
            mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        Utils.injectObject(userController, "userRepository",
                userRepository);
        Utils.injectObject(userController, "cartRepository",
                cartRepository);
        Utils.injectObject(userController, "bCryptPasswordEncoder",
                bCryptPasswordEncoder);
    }

    @Test
    public void testCreteUser() {
        User user = createUser();
        CreateUserRequest rq = new CreateUserRequest("anhduc", "anhduc68",
               "anhduc68");
        ResponseEntity<User> createdUser = userController.createUser(rq);
        //assert not null
        assertNotNull(createdUser);
        //assert Equal
        assertEquals( 201 ,createdUser.getStatusCodeValue());
    }
    @Test
    public void createUserFail(){
        CreateUserRequest rq = new CreateUserRequest("anhduc", "anhduc68",
                "anhduc6");
        ResponseEntity<User> createdUser = userController.createUser(rq);
        //assert Mot Null
        assertNotNull(createdUser);
        // assert Equal
        assertEquals( 400 ,createdUser.getStatusCodeValue());

    }

    @Test
    public void findByNameTest(){
        User user = createUser();
        //set up value
        doReturn(user).when(userRepository).findByUsername("anhduc");
        ResponseEntity<User> entityResponse = userController.findByUserName("anhduc");
        assertNotNull(entityResponse);
        // status is 200
        assertEquals( 200 ,entityResponse.getStatusCodeValue());
        // correct Name
        assertEquals("anhduc", entityResponse.getBody().getUsername());
    }

    @Test
    public void findByNameFailTest(){
        ResponseEntity<User> entityResponse = userController.findByUserName("anhduc");
        // Not Nul
        assertNotNull(entityResponse);
        // Equal
        assertEquals( 404 ,entityResponse.getStatusCodeValue());
    }

    @Test
    public void findByIdTest(){
        User user = createUser();
        doReturn(Optional.of(user)).when(userRepository).findById(1L);
        ResponseEntity<User> entityResponse = userController.findById(1L);
        assertNotNull(entityResponse);
        // status
        assertEquals( 200 ,entityResponse.getStatusCodeValue());
        // name
        assertEquals( "anhduc" ,entityResponse.getBody().getUsername());
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
        user.setPassword("anhduc68");

        user.setCart(createCart());
        return user;
    }


}



