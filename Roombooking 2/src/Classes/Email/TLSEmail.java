package Classes.Email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Outgoing Mail (SMTP) Server
 * requires TLS: smtp.gmail.com (use authentication)
 * Use Authentication: Yes
 * Port for TLS/STARTTLS: 587
 *
 * @author trym
 * @see EmailUtil for email cunstructon
 */
public class TLSEmail {
    private String fromEmail;
    private String password;

    public TLSEmail() throws Exception {
        fromEmail = "grproom@gmail.com"; //required valid email id
        //stores the password from local file on the password variable for sending emails
        password = readFileAsString("/Users/trym/go/src/github.com/Nosp1/is-200/Gruppe10/Roombooking 2/src/Classes/Email/password");

    }
    /**
     * Reads locally stored password file as a string to parse the password for better security
     * @return data with stored password
     */
    private static String readFileAsString(String fileName) throws Exception
    {
        //initializes data string to store the read characters
        String data = "";
        //reads the characters in the file and replaces escaped quotes with an empty character
        data = new String(Files.readAllBytes(Paths.get(fileName))).replaceAll("\"", "");
        //prints for debugging
        System.out.println(data);
        //returns the stored value of data
        return data;

    }
    /**
     * Method takes in a {@code String} object and sends it to {@code SendSecureEmail}
     *
     * @param toEmail methods takes in recipient email.
     */
    public Session NoReplyEmail(String toEmail) throws Exception {
        System.out.println("TLSEmail Start");
        //creates TlSEMAIL object
        TLSEmail tlsEmail = new TLSEmail();
        //calls on TLSE to establish host,port, authentication and starttls
        return tlsEmail.SendSecureEmail(fromEmail, password, toEmail);
    }

    /**
     * @param fromEmail is the senders Email id
     * @param password  is the senders Email Password
     * @param toEmail   is the recipient email
     */
    private Session SendSecureEmail(String fromEmail, String password, String toEmail) {
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
        getSession(props, auth);
        //returns the email session to prepare for sending;
        return getSession(props, auth);
    }

    private Session getSession(Properties props, Authenticator auth) {
        return Session.getInstance(props, auth);
    }
}