package Tests;

import Classes.Email.EmailTemplates;
import Classes.Email.EmailUtil;
import Classes.Email.TLSEmail;
import Classes.User.AbstractUser;
import Classes.User.Student;
import Tools.DbFunctionality;
import Tools.DbTool;
import org.junit.Before;
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
    private DbTool dbTool;
    private DbFunctionality dbFunctionality;
    private TLSEmail tlsEmail;
    private Connection testConnection;
    //the test users email address
    private String testUserEmail = "trymerlend@hotmail.no";



    @Before
    public void init() {
        System.out.println("test init of email");
        dbTool = new DbTool();
        dbFunctionality = new DbFunctionality();
        testConnection = dbTool.dbLogIn();
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
