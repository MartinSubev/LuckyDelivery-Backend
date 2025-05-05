package LuckyDelivery.Controller;

import LuckyDelivery.Model.Order;
import LuckyDelivery.Service.SupplierOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('SUPPLIER')")
public class SupplierOrderController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierOrderController.class);

    private final SupplierOrderService supplierOrderService;

    @Autowired
    public SupplierOrderController(SupplierOrderService supplierOrderService) {
        this.supplierOrderService = supplierOrderService;
    }

    @GetMapping("/available")
    public ResponseEntity<List<Order>> getAvailableOrders() {
        logger.info("GET /api/supplier/available");
        List<Order> orders = supplierOrderService.getAvailableOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/claim/{orderId}")
    public ResponseEntity<String> claimOrder(@PathVariable Long orderId) { // Removed @RequestParam Long supplierId
        logger.info("PUT /api/supplier/claim/{}", orderId);
        boolean isClaimed = supplierOrderService.assignOrderToSupplier(orderId);
        if (isClaimed) {
            return ResponseEntity.ok("Order successfully claimed");
        } else {
            return ResponseEntity.badRequest().body("Failed to claim order");
        }
    }

    @GetMapping("/collected")
    public ResponseEntity<List<Order>> getCollectedOrders() { // Removed @RequestParam Long supplierId
        logger.info("GET /api/supplier/collected");
        List<Order> orders = supplierOrderService.getCollectedOrders();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/deliver/{orderId}")
    public ResponseEntity<String> markOrderAsDelivered(@PathVariable Long orderId) {
        logger.info("PUT /api/supplier/deliver/{}", orderId);
        boolean isDelivered = supplierOrderService.markOrderAsDelivered(orderId);
        if (isDelivered) {
            return ResponseEntity.ok("Order marked as delivered");
        } else {
            return ResponseEntity.badRequest().body("Failed to mark order as delivered");
        }
    }

    @GetMapping("/completed")
    public ResponseEntity<List<Order>> getCompletedOrders() { // Removed @RequestParam Long supplierId
        logger.info("GET /api/supplier/completed");
        List<Order> orders = supplierOrderService.getCompletedOrders();
        return ResponseEntity.ok(orders);
    }
}