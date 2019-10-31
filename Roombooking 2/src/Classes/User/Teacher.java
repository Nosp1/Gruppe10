package Classes.User;
import Classes.Order;
import Classes.UserType;
import java.util.ArrayList;

/**
 *  the student class handles the UserType of Student
 * @author alena
 */

public class Teacher extends AbstractUser {
    public Teacher(String firstName, String lastName, String userName, String password, String dob ) {
        super(firstName,lastName,userName,dob,password, UserType.TEACHER);
    }

    public Teacher(int userId , ArrayList<Order> orderList) {
        super(userId,orderList);

    }
}