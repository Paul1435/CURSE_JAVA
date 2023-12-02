package autorization;

import java.io.Serializable;

public record EmployeeRegister(String username, String password, String lastName, String firstName, String middleName,
                               Role role) implements Serializable {
    public static enum Role {MANAGER, CANDIDATE}


}
