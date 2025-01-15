package adapters.inbound.rest.dto;

import java.util.List;

public class BulkCheckResponse {
    private List<BulkCheckResultDTO> results;

    public BulkCheckResponse() {
    }

    public BulkCheckResponse(List<BulkCheckResultDTO> results) {
        this.results = results;
    }

    public List<BulkCheckResultDTO> getResults() {
        return results;
    }

    public void setResults(List<BulkCheckResultDTO> results) {
        this.results = results;
    }
}
