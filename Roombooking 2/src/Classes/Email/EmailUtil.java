package Classes.Email;


import java.util.Date;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Handles the construction of a html based email that sends an email to a recipient user
 * @author trym
 */
public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     * @param session parses the host, authentication TSL, port
     * @param toEmail recipients email
     * @param subject The email subject
     * @param body The body of the email
     *
     */
    public void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress("grproom@gmail.com", "NoReply-RoomBooking"));
            msg.setReplyTo(InternetAddress.parse("grproom@gmail.com", false));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}