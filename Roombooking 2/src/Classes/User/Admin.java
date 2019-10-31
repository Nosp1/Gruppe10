package Classes.User;
import Classes.Order;
import Classes.UserType;
import java.util.ArrayList;

/**
 *  the class Admin handles the UserType of Admin
 * @author brisdalen
 */

public class Admin extends AbstractUser {
    public Admin(String firstName, String lastName, String userName, String password, String dob ) {
        super(firstName,lastName,userName,dob,password, UserType.ADMIN);
    }

    public Admin(int userId, ArrayList<Order> orderList) {
        super(userId,orderList);

    }
}