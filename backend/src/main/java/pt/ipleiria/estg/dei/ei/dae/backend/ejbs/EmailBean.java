package pt.ipleiria.estg.dei.ei.dae.backend.ejbs;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
@Stateless(name = "EmailEJB")
public class EmailBean {
    @Resource(name = "java:/jboss/mail/fakeSMTP")
    private Session mailSession;
    private static final Logger logger = Logger.getLogger("EmailBean.logger");
    public void send(String to, String subject, String text) {
        Thread emailJob = new Thread(() -> {
            Message message = new MimeMessage(mailSession);
            Date timestamp = new Date();
            try {
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(to, false));
                message.setSubject(subject);
                message.setText(text);
                message.setSentDate(timestamp);
                Transport.send(message);
            } catch (MessagingException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
        emailJob.start();
    }

    public void retrieve(String username, String password) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "your-imap-server.com");
        properties.put("mail.imaps.port", "993");

        Session session = Session.getInstance(properties);

        try {
            Store store = session.getStore();
            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                // Process the retrieved message
                displayMessageInfo(message);
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayMessageInfo(Message message) {
        try {
            System.out.println("Subject: " + message.getSubject());
            System.out.println("From: " + message.getFrom()[0]);
            System.out.println("Sent Date: " + message.getSentDate());
            System.out.println("Content: " + ((MimeMessage) message).getContent());
            System.out.println("-----------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}