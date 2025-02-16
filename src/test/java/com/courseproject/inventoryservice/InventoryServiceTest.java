package com.courseproject.inventoryservice;

import com.courseproject.inventoryservice.models.Product;
import com.courseproject.inventoryservice.models.PurchaseOrder;
import com.courseproject.inventoryservice.models.PurchaseOrderLineItem;
import com.courseproject.inventoryservice.repositories.ProductRepository;
import com.courseproject.inventoryservice.repositories.PurchaseOrderRepository;
import com.courseproject.inventoryservice.services.InventoryService;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InventoryServiceTest {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    ProductRepository productRepository;

    private UUID productId;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @BeforeEach
    void setUp() {
        // Create a product in DB with quantity=100
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Demo for concurrency test");
        product.setPrice(10.0);
        product.setQuantity(100.0); // Start quantity
        productRepository.save(product);

        this.productId = product.getId();
    }

    @Test
    void testOptimisticLockingOnConcurrentSell() throws InterruptedException, ExecutionException {
        final int THREAD_COUNT = 20;
        final double SELL_QTY = 10.0;

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            tasks.add(() -> {
                try {
                    inventoryService.sellProduct(productId, SELL_QTY);
                    return true;
                } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                    return false;
                }
            });
        }

        List<Future<Boolean>> results = executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        int successCount = 0;
        for (Future<Boolean> f : results) {
            if (f.get()) {
                successCount++;
            }
        }

        Product finalProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("Product not found after test"));

        double finalQuantity = finalProduct.getQuantity();

        double expectedQuantity = 100.0 - (successCount * SELL_QTY);

        assertEquals(expectedQuantity, finalQuantity, 0.0001,
                "Final product quantity should match the number of successful sells.");
    }

    @Test
    @Rollback
    void testOptimisticLockingOnConcurrentReceivePurchaseOrder() throws Exception {
        UUID purchaseOrderId = createDemoPurchaseOrder(productId, 20.0);

        final int THREAD_COUNT = 5;
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        List<Callable<Boolean>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_COUNT; i++) {
            tasks.add(() -> {
                try {
                    inventoryService.receivePurchaseOrder(purchaseOrderId);
                    return true;
                } catch (ObjectOptimisticLockingFailureException | OptimisticLockException e) {
                    return false;
                }
            });
        }

        List<Future<Boolean>> results = executor.invokeAll(tasks);
        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        int successCount = 0;
        for (Future<Boolean> f : results) {
            if (f.get()) {
                successCount++;
            }
        }

        Product finalProduct = productRepository.findById(productId).get();
        double finalQty = finalProduct.getQuantity();

        double expectedQty = 100.0 + (successCount * 20.0);

        assertEquals(expectedQty, finalQty, 0.0001,
                "Final product quantity should reflect how many times we received PO successfully.");
    }


    private UUID createDemoPurchaseOrder(UUID productId, double qty) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderDate(LocalDateTime.now());

        PurchaseOrderLineItem lineItem = new PurchaseOrderLineItem();
        lineItem.setQuantity(qty);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found for ID: " + productId));
        lineItem.setProduct(product);

        lineItem.setPurchaseOrder(purchaseOrder);
        purchaseOrder.getPurchaseOrderLineItems().add(lineItem);

        purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrder.getId();
    }
}
