package hub.com.apiusers.projection.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "username", "email" })
public interface UserView {
    Long getId();
    String getUsername();
    String getEmail();
}
