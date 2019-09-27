package Classes.Email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;
/**
 * Outgoing Mail (SMTP) Server
 * requires TLS: smtp.gmail.com (use authentication)
 * Use Authentication: Yes
 * Port for TLS/STARTTLS: 587
 * @author trym
 * @see EmailUtil for email cunstructon
 *
 */
public class TLSEmail {
    String fromEmail = "grproom@gmail.com"; //required valid email id
    String password = "dennIS93"; //required valid password for email id


    public TLSEmail() {
    }

    /**
     * Method takes in a {@code String} object and sends it to {@code SendSecureEmail}
     * @param toEmail methods takes in recipient email.
     */
    public void NoReplyEmail(String toEmail) {
        System.out.println("TLSEmail Start");
        //creates TlSEMAIL object
        TLSEmail tlsEmail = new TLSEmail();
        //calls on TLSE to establish host,port, authentication and starttls
        tlsEmail.SendSecureEmail(fromEmail, password, toEmail);
    }
    /**
     *
     * @param fromEmail is the senders Email id
     * @param password is the senders Email Password
     * @param toEmail is the recipient email
     */
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