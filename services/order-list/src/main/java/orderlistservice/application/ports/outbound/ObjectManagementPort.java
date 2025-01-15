package orderlistservice.application.ports.outbound;

import orderlistservice.domain.model.ItemDetails;

import java.util.List;

/**
 * Outbound Port für den Object Management Service.
 */
public interface ObjectManagementPort {
    List<ItemDetails> getItemDetails(List<Integer> itemIds);
}
