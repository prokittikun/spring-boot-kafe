// Kittikun Buntoyut 6510405334
package ku.cs.kafe.service;


import ku.cs.kafe.common.Status;
import ku.cs.kafe.entity.Menu;
import ku.cs.kafe.entity.OrderItem;
import ku.cs.kafe.entity.OrderItemKey;
import ku.cs.kafe.entity.PurchaseOrder;
import ku.cs.kafe.request.AddCartRequest;
import ku.cs.kafe.repository.MenuRepository;
import ku.cs.kafe.repository.OrderItemRepository;
import ku.cs.kafe.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class OrderService {


    @Autowired
    private PurchaseOrderRepository orderRepository;


    @Autowired
    private OrderItemRepository itemRepository;


    @Autowired
    private MenuRepository menuRepository;


    public PurchaseOrder getCurrentOrder() {


        PurchaseOrder currentOrder =
                orderRepository.findByStatus(Status.ORDER);


        if (currentOrder == null) {
            PurchaseOrder newOrder = new PurchaseOrder();
            newOrder.setStatus(Status.ORDER);
            currentOrder = orderRepository.save(newOrder);
        }


        return currentOrder;
    }


    public void order(UUID menuId, AddCartRequest request) {
        PurchaseOrder currentOrder = getCurrentOrder();


        Menu menu = menuRepository.findById(menuId).get();


        OrderItem item = new OrderItem();
        item.setId(new OrderItemKey(currentOrder.getId(), menuId));
        item.setPurchaseOrder(currentOrder);
        item.setMenu(menu);
        item.setQuantity(request.getQuantity());
        itemRepository.save(item);
    }

    public void submitOrder() {
        PurchaseOrder currentOrder =
                orderRepository.findByStatus(Status.ORDER);
        currentOrder.setTimestamp(LocalDateTime.now());
        currentOrder.setStatus(Status.CONFIRM);
        orderRepository.save(currentOrder);
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepository.findAll();
    }


    public PurchaseOrder getById(UUID orderId) {
        return orderRepository.findById(orderId).get();
    }

}
