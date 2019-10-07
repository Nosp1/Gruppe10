package Tests;

import Classes.Email.EmailTemplates;
import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.User.AbstractUser;
import Classes.User.Student;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.mail.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Emailing. Tests Email Templates, EmailUtil & TSLEMail
 * @author trym
 */
public class EmailTest {
    DbTool dbTool;
    DbFunctionality dbFunctionality;
    TLSEmail tlsEmail;
    Connection testConnection;
    //the test users email address
    String testUserEmail = "trymerlend@hotmail.no";
    String testAdminEmail = "grpoom@gmail.com";
    String testAdminPw = "dennIS93";


    @Before
    public void init() {
        System.out.println("test init of email");
        dbTool = new DbTool();
        dbFunctionality = new DbFunctionality();
        testConnection = dbTool.dbLogIn();
    }

    @Test
    public void testAddAdminEmail() {
        //sends test email, pw and connection to db func method.
        dbFunctionality.addAdminEmail(testAdminEmail, testAdminPw, testConnection);
        String statement = "Select Email_name from Email where Email_name = ?";
        try {
            PreparedStatement preparedStatement = testConnection.prepareStatement(statement);
            preparedStatement.setString(1, testAdminEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                assertEquals(resultSet.getString("Email_name"), testAdminEmail);
                System.out.println(resultSet.getString("Email_name"));
            }
            assertTrue(dbFunctionality.deleteAdminEmail(testAdminEmail, testConnection));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Tests the email function
     * asserts whether recipient email is equal to test email.
     */
    @Test
    public void testSendEmail() {
        AbstractUser testUser = new Student("Ola", "Nordmann", testUserEmail, "1234", "1900-01-01");
         Session session = tlsEmail.NoReplyEmail(testUser.getUserName());
        EmailUtil emailUtil =  new EmailUtil();
        String subject = EmailTemplates.getWelcome();
        String body = EmailTemplates.welcomeMessageBody(testUserEmail);
        emailUtil.sendEmail(session,testUserEmail,subject,body);
        assertEquals(testUser.getUserName(),testUserEmail);


    }


}
