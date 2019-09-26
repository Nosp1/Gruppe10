package Classes.Email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class TLSEmail {
    String fromEmail = "grproom@gmail.com"; //required valid email id
    String password = "dennIS93"; //required valid password for email id

    /**
     * Outgoing Mail (SMTP) Server
     * requires TLS or SSL: smtp.gmail.com (use authentication)
     * Use Authentication: Yes
     * Port for TLS/STARTTLS: 587
     */
    public TLSEmail() {
    }

    public void NoReplyEmail(String toEmail) {
        //final String fromEmail = "grproom@gmail.com"; //requires valid gmail id
        //final String password = "dennIS93"; // correct password for gmail id
        //final String toEmail = "lisa.austad@gmail.com"; // can be any email id

        System.out.println("TLSEmail Start");
        TLSEmail tlsEmail = new TLSEmail();
        tlsEmail.SendSecureEmail(fromEmail, password, toEmail);
    }

    private void SendSecureEmail(String fromEmail, String password, String toEmail) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        EmailUtil emailUtil = new EmailUtil();
        // calls on EmailUtil send email method and parses the session, toEmail & subject + body
        emailUtil.sendEmail(session, toEmail, "Test", "Test");
    }
}