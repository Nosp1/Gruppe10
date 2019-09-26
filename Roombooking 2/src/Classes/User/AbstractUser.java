package Classes.User;

import Classes.UserType;

public abstract class AbstractUser {
    /*
    fields
     */
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String dob;
    protected String password;
    protected UserType userType;


    public AbstractUser( String firstName, String lastName,String userName, String dob, String password, UserType userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dob = dob;
        this.password = password;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
