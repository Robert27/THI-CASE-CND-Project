package dev.eggl.application.service.category;

import dev.eggl.domain.model.StorageObject;
import dev.eggl.port.in.storageObject.ListStorageObjectUseCase;
import dev.eggl.port.out.StorageObjectPort;

import java.util.List;

public class ListStorageObjectService implements ListStorageObjectUseCase {
   private final StorageObjectPort storageObjectPort;

   public ListStorageObjectService(StorageObjectPort storageObjectPort) {
      this.storageObjectPort = storageObjectPort;
   }

    @Override
    public List<StorageObject> findAll() {
        return storageObjectPort.findAll();
    }
}
