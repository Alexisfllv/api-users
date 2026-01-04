package hub.com.apiusers.entity;

public enum ExceptionMessages {

    RESOURCE_NOT_FOUND_ERROR("Value dont found: ");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
