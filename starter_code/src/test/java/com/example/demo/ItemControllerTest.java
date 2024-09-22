package com.example.demo;

import com.example.demo.Utils.Utils;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository =
            mock(ItemRepository.class);


    @Before
    public void setup() {
        itemController = new ItemController();
        Utils.injectObject(itemController, "itemRepository",
                itemRepository);

    }

    @Test
    public void getById(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(10.0));
        doReturn(Optional.of(item)).when(itemRepository).findById(1L);
        ResponseEntity<Item> responseEntity =
                itemController.getItemById(1L);
        assertNotNull(responseEntity);
        // name
        assertEquals("Item", responseEntity.getBody().getName());
        // status
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getByName(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Description");
        item.setPrice(BigDecimal.valueOf(20.0));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        doReturn(itemList).when(itemRepository).findByName("Item");
        ResponseEntity<List<Item>> responseEntity =
                itemController.getItemsByName("Item");
        // Not Null
        assertNotNull(responseEntity);
        // Status
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemByNameButNull(){
        List<Item> itemList = new ArrayList<>();
        doReturn(null).when(itemRepository)
                .findByName("Item");

        ResponseEntity<List<Item>> responseEntity =
                itemController.getItemsByName("Item");
        // Not Null
        assertNotNull(responseEntity);
        // Status
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void getALlItemsTest(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Item");
        item.setDescription("Desciption");
        item.setPrice(BigDecimal.valueOf(20.0));

        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        doReturn(itemList).when(itemRepository).findAll();
        ResponseEntity<List<Item>> responseEntity =
                itemController.getItems();
        // Not Null
        assertNotNull(responseEntity);
        // Status
        assertEquals(200, responseEntity.getStatusCodeValue());

    }







}
