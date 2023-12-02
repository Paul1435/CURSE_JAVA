package autorization;

import java.io.Serializable;

public record Entrance(String login, String password) implements Serializable {
}
