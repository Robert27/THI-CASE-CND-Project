package thi.hexa.userservice.adapter.mockcollector;

import org.springframework.stereotype.Service;
import thi.hexa.userservice.ports.outgoing.MetadataCollector;

@Service
public class MetadataCollectorImpl implements MetadataCollector {
    @Override
    public void collectMetadata(String user) {
        System.out.println("Collected Metadata for user: " + user);
    }
}
