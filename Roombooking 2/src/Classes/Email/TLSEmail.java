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
        //SMTP Host: Asserts which mail server to use. -> google in this case.
        props.put("mail.smtp.host", "smtp.gmail.com");
        //TLS Port: Which outgoing port to use for TLS -> 587 default port.
        props.put("mail.smtp.port", "587");
        //enable authentication: required to send secure emails over the Internet.
        props.put("mail.smtp.auth", "true");
        //enable STARTLLS: required to send email via TLS protocol.
        props.put("mail.smtp.starttls.enable", "true");

        //create Authenticator object to login and confirm password from sender.
        Authenticator auth = new Authenticator() {
            //returns the senders EmailID and EmailPassword.
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        //gets the host,port, Sender EmailID and password
        Session session = Session.getInstance(props, auth);
        EmailUtil emailUtil = new EmailUtil();
        //Sends Email by taking: Sender EmailID, password, port
        emailUtil.sendEmail(session, toEmail, "Welcome to roombooking", "Velkommen til roombooking");
    }
}