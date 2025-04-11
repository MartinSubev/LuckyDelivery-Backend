package LuckyDelivery.Controller;

import LuckyDelivery.Model.Order;
import LuckyDelivery.Service.SupplierOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@CrossOrigin(origins = "*")
public class SupplierOrderController {

    private final SupplierOrderService supplierOrderService;

    @Autowired
    public SupplierOrderController(SupplierOrderService supplierOrderService) {
        this.supplierOrderService = supplierOrderService;
    }

    // Get all available orders for a supplier
    @GetMapping("/available")
    public ResponseEntity<List<Order>> getAvailableOrders() {
        // These are orders with PENDING status
        List<Order> orders = supplierOrderService.getOrdersByStatusAndSupplierId(null, "PENDING");
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/claim/{orderId}")
    public ResponseEntity<String> claimOrder(@PathVariable Long orderId, @RequestParam Long supplierId) {
        boolean isClaimed = supplierOrderService.assignOrderToSupplier(orderId, supplierId);
        if (isClaimed) {
            return ResponseEntity.ok("Order successfully claimed");
        } else {
            return ResponseEntity.status(400).body("Failed to claim order");
        }
    }
    @GetMapping("/collected")
    public ResponseEntity<List<Order>> getCollectedOrders(@RequestParam Long supplierId) {
        List<Order> orders = supplierOrderService.getOrdersByStatusAndSupplierId(supplierId, "IN_TRANSIT");
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/deliver/{orderId}")
    public ResponseEntity<String> markOrderAsDelivered(@PathVariable Long orderId) {
        boolean isDelivered = supplierOrderService.markOrderAsDelivered(orderId);
        if (isDelivered) {
            return ResponseEntity.ok("Order marked as delivered");
        } else {
            return ResponseEntity.status(400).body("Failed to mark order as delivered");
        }
    }
}
