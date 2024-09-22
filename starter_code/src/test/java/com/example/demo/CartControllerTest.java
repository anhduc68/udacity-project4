package com.example.demo;

import com.example.demo.Utils.Utils;
import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    private CartController cartController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final ItemRepository itemRepository = mock(ItemRepository.class);


    private Item item;

    @Before
    public void setup() {
        cartController = new CartController();
        Utils.injectObject(cartController, "userRepository",
                userRepository);
        Utils.injectObject(cartController, "cartRepository",
                cartRepository);
        Utils.injectObject(cartController, "itemRepository",
                itemRepository);

    }


    @Test
    public void addToCartWithNoUser() {
        ModifyCartRequest modifyCartRequest =
                createMdfCartReq("", 0, 0);

        // get Cart
        ResponseEntity<Cart> responseEntity =
                cartController
                        .addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addToCartSuccessful(){
        //create User
        User user = createUser();
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(10.0));

        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        //set up for Return
        when(userRepository.findByUsername("test")).thenReturn(user);

        doReturn(Optional.of(item)).when(itemRepository).findById(1L);

        ModifyCartRequest modifyCartRequest = createMdfCartReq("test", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.addTocart(modifyCartRequest);
        //assertNotNull
        assertNotNull(responseEntity);
        //assertEqual
        assertEquals(200, responseEntity.getStatusCodeValue());
        //verify
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    public void addTocartButNotFoundItem(){
        // set up for Return
        when(userRepository.findByUsername("test")).thenReturn(new User());
        doReturn(Optional.empty()).when(itemRepository).findById(1L);
        ModifyCartRequest modifyCartRequest =
                createMdfCartReq("test", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.addTocart(modifyCartRequest);
        //assert NotNull
        assertNotNull(responseEntity);
        // aasert Equal
        assertEquals(404, responseEntity.getStatusCodeValue());
    }


    @Test
    public void removeCartSuccessful(){
        User user = createUser();
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(10.0));

        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        doReturn(Optional.of(item)).when(itemRepository).findById(1L);

        ModifyCartRequest modifyCartRequest = createMdfCartReq("test", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.removeFromcart(modifyCartRequest);

        //assertNotNull
        assertNotNull(responseEntity);

        //assertEqual
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCartButNotFoundUser(){
        ModifyCartRequest modifyCartRequest = createMdfCartReq("", 0, 0);
        ResponseEntity<Cart> responseEntity =
                cartController.removeFromcart(modifyCartRequest);
        //assert NotNull
        assertNotNull(responseEntity);

        //assert Equal
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void removeCartButNotFoundItem(){
        //set up for Return
        when(userRepository.findByUsername("test")).thenReturn(new User());
        doReturn(Optional.empty()).when(itemRepository).findById(1L);

        ModifyCartRequest modifyCartRequest = createMdfCartReq("test", 1, 1);
        ResponseEntity<Cart> responseEntity =
                cartController.removeFromcart(modifyCartRequest);
        //assert Not Nul
        assertNotNull(responseEntity);
        //assert Equal
        assertEquals(404, responseEntity.getStatusCodeValue());

    }



    public static ModifyCartRequest createMdfCartReq(String username, long itemId, int quantity){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;

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


