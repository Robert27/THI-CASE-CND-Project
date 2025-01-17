package orderlistservice.domain;

import orderlistservice.domain.model.OrderObject;
import orderlistservice.domain.model.PerformOrderResult;
import orderlistservice.domain.model.ItemDetails;
import java.util.List;

public interface OrderService {
    int generateOrderList(Integer userId, List<Integer> itemIds, String cycleDate);
    List<OrderObject> getOpenOrders(Integer userId);
    PerformOrderResult performOrder(Integer userId, Integer itemId, int quantity);
    boolean abortOrder(Integer userId, Integer itemId);
}