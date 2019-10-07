package Classes.User;

import Classes.Order;
import Classes.UserType;

import java.util.ArrayList;

public class Student extends AbstractUser {
    public Student(String firstName, String lastName, String userName, String password, String dob ) {
        super(firstName,lastName,userName,dob,password,UserType.STUDENT);

    }

    public Student(int userId , ArrayList<Order> orderList) {
        super(userId,orderList);

    }
}
