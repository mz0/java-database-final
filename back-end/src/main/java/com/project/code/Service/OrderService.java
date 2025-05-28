package com.project.code.Service;

import com.project.code.Model.*;
import com.project.code.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class OrderService {
    @Autowired  private ProductRepository productRepository;
    @Autowired  private InventoryRepository inventoryRepository;
    @Autowired  private CustomerRepository customerRepository;
    @Autowired  private StoreRepository storeRepository;
    @Autowired  private OrderDetailsRepository orderDetailsRepository;
    @Autowired  private OrderItemRepository orderItemRepository;

    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) {
        Customer cust = customerRepository.findByEmail(placeOrderRequest.getCustomerEmail());
        if (cust == null) {
            cust = new Customer();
            cust.setEmail(placeOrderRequest.getCustomerEmail());
            cust.setName(placeOrderRequest.getCustomerName());
            cust.setPhone(placeOrderRequest.getCustomerPhone());
            cust = customerRepository.save(cust);
        }
        Store store = storeRepository.findById(placeOrderRequest.getStoreId()).orElseThrow(() ->
                new NoSuchElementException(String.format("Store %s not found", placeOrderRequest.getStoreId()))
        );

        OrderDetails od = new OrderDetails(cust, store, placeOrderRequest.getTotalPrice(), LocalDateTime.now());
        od = orderDetailsRepository.save(od);
        for (PurchaseProductDTO pd : placeOrderRequest.getPurchaseProduct()) {
            Inventory inventory = inventoryRepository.findByProductIdandStoreId(pd.getId(), placeOrderRequest.getStoreId());
            inventory.setStockLevel(inventory.getStockLevel() - pd.getQuantity());
            inventoryRepository.save(inventory);
            // FIXME getById DEPRECATED
            OrderItem oi = new OrderItem(od, productRepository.findByid(pd.getId()), pd.getQuantity(), pd.getPrice());
            orderItemRepository.save(oi);
        }
    }
}
