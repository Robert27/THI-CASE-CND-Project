package urlvalidation.rest.dto;

public class ValidationResult {

    private boolean valid;       // true, wenn syntaktisch eine gültige URL
    private boolean reachable;   // true, wenn URL per HEAD-Request erreichbar
    private String message;      // optionaler Text (z.B. "URL nicht erreichbar. Response-Code: 404")

    // Für JAX-RS/JSON-Serialisierung braucht es einen Default-Konstruktor
    public ValidationResult() {
    }

    public ValidationResult(boolean valid, boolean reachable, String message) {
        this.valid = valid;
        this.reachable = reachable;
        this.message = message;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
