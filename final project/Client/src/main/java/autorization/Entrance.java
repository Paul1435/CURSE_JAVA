package autorization;

import java.awt.*;
import java.io.Serializable;

public record Entrance(String login, String password) implements Serializable {

}
