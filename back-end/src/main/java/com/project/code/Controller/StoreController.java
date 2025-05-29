package com.project.code.Controller;

import com.project.code.Model.PlaceOrderRequestDTO;
import com.project.code.Model.Store;
import com.project.code.Repo.StoreRepository;
import com.project.code.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController @RequestMapping("/store")
public class StoreController {
    @Autowired private StoreRepository storeRepository;
    @Autowired private OrderService orderService;

    @PostMapping
    public Map<String, String> addStore(@RequestBody Store store) {
        Store savedStore = storeRepository.save(store);
        return Map.of("message",  String.format("Store %d created", savedStore.getId()));
    }

    @GetMapping("validate/{storeId}")
    public boolean validateStore(@PathVariable Long storeId) {
        return storeRepository.existsById(storeId);
    }

    @PostMapping("/placeOrder")
    public Map<String, String> placeOrder(@RequestBody PlaceOrderRequestDTO por) {
        try {
            orderService.saveOrder(por);
            return Map.of("message", "Order placed successfully");
        } catch (Exception e) {
            return Map.of("Error", e.getMessage());
        }
    }
}
