package Classes.User;

import Classes.UserType;

public class Student extends AbstractUser {
    public Student(String firstName, String lastName, String userName, String password, String dob ) {
        super(firstName,lastName,userName,dob,password,UserType.STUDENT);

    }
}
