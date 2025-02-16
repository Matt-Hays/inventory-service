package com.courseproject.inventoryservice;

import com.courseproject.inventoryservice.models.*;
import com.courseproject.inventoryservice.services.InventoryService;
import com.courseproject.inventoryservice.services.ProductService;
import com.courseproject.inventoryservice.services.PurchaseOrderService;
import com.courseproject.inventoryservice.services.VendorService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryServiceApplicationTests {

    @Autowired
    VendorService vendorService;
    @Autowired
    ProductService productService;
    @Autowired
    PurchaseOrderService purchaseOrderService;
    @Autowired
    InventoryService inventoryService;

    private static Vendor vendor = null;
    private static Product product = null;
    private static Inventory inventory = null;
    private static PurchaseOrder purchaseOrder = null;
    private static PurchaseOrderLineItem purchaseOrderLineItem = null;

    @BeforeAll
    public static void setup() {

        vendor = new Vendor();
        vendor.setName("Vendor1");
        vendor.setEmail("test@test.com");
        vendor.setPhone("1-555-555-5555");
        vendor.setAddress("1234 Street, City, State 12345");

        product = new Product();
        product.setName("product");
        product.setPrice(10.99);
        product.setDescription("This is the first product.");

        purchaseOrder = new PurchaseOrder();
    }


    @Test
    @Order(1)
    void createVendor() {
        vendor = vendorService.createVendor(vendor);

        assertNotNull(vendor.getId());

        System.out.println(vendor);
    }

    @Test
    @Order(2)
    void createProduct() {

        productService.createProduct(product);
        productService.createProduct(product);

        assertNotNull(product.getId());
        assertNotNull(product.getId());

        System.out.println(product);
        System.out.println(product);
    }

    @Test
    @Order(3)
    void createPurchaseOrder() {
        purchaseOrder.setOrderDate(LocalDateTime.now());
        purchaseOrder.setVendor(vendor);

        vendorService.createVendor(vendor);
        purchaseOrder = purchaseOrderService.createPurchaseOrder(purchaseOrder);

        assertNotNull(purchaseOrder.getId());

        System.out.println(purchaseOrder);
    }

    @Test
    @Order(4)
    void addProductToPurchaseOrder() {
        purchaseOrder = purchaseOrderService.addProduct(purchaseOrder.getId(), product.getId(), 20);

        assertNotNull(purchaseOrder);
        Set<PurchaseOrderLineItem> purchaseOrderLineItems = purchaseOrder.getPurchaseOrderLineItems();

        System.out.println(purchaseOrderLineItems.size());
        assert (1 == purchaseOrderLineItems.size());

        System.out.println(purchaseOrder);
    }

    @Test
    @Order(5)
    void arrivePurchaseOrder() {
        purchaseOrderService.arrivePurchaseOrder(purchaseOrder.getId());

        Inventory inventory = inventoryService.getInventory(product.getId());
        System.out.println(inventory);

        assertEquals(inventory.getQuantity(), 20);
    }

    @Test
    @Order(6)
    void sellProduct() throws Exception {
        Inventory inventory = inventoryService.sellProduct(product.getId(), 10);

        assertNotNull(inventory);
        assertEquals(inventory.getQuantity(), 10);
    }

}