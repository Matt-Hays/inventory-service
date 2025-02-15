package com.courseproject.inventoryservice;

import com.courseproject.inventoryservice.models.*;
import com.courseproject.inventoryservice.services.ProductService;
import com.courseproject.inventoryservice.services.PurchaseOrderLineItemService;
import com.courseproject.inventoryservice.services.PurchaseOrderService;
import com.courseproject.inventoryservice.services.VendorService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryServiceApplicationTests {

    //	@Autowired
//	InventoryService inventoryService;
    @Autowired
    VendorService vendorService;
    @Autowired
    ProductService productService;
    @Autowired
    PurchaseOrderService purchaseOrderService;
    @Autowired
    PurchaseOrderLineItemService purchaseOrderLineItemService;

    private static Vendor vendor = null;
    private static Product product1 = null;
    private static Product product2 = null;
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

        product1 = new Product();
        product1.setName("Product1");
        product1.setPrice(10.99);
        product1.setDescription("This is the first product.");

        product2 = new Product();
        product2.setName("Product2");
        product2.setPrice(9.49);
        product2.setDescription("This is the second product.");

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

        productService.createProduct(product1);
        productService.createProduct(product2);

        assertNotNull(product1.getId());
        assertNotNull(product2.getId());

        System.out.println(product1);
        System.out.println(product2);
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

//    @Test
//    @Order(4)
//    void createPurchaseOrderLineItem() {
////        purchaseOrderLineItem.setProduct(product);
////        purchaseOrderLineItem.setQuantity(5);
////        purchaseOrderLineItem.setPurchaseOrder(purchaseOrder);
//
//        purchaseOrderLineItem = purchaseOrderLineItemService.createPurchaseOrderLineItem(product1.getId(), 10);
//
//        assertNotNull(purchaseOrderLineItem);
//
//        System.out.println(purchaseOrderLineItem);
//    }

    @Test
    @Order(4)
    void addProductToPurchaseOrder() {
        purchaseOrder = purchaseOrderService.addProduct(purchaseOrder.getId(), product2.getId(), 20);

        assertNotNull(purchaseOrder);
        Set<PurchaseOrderLineItem> purchaseOrderLineItems = purchaseOrder.getPurchaseOrderLineItems();

        assert (2 == purchaseOrderLineItems.size());

        assert (purchaseOrderLineItems.contains(purchaseOrderLineItem));

        System.out.println(purchaseOrder);
    }


}