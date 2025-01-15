package application.port;

import java.util.List;
import java.util.Map;
/**
 * Port für den Zugriff auf den Object Management Service.
 */
public interface ObjectManagementPort {

    Map<Integer, String> getReorderUrlsByIds(List<Integer> itemIds);
}