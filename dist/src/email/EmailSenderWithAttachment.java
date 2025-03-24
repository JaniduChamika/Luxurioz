package email;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Janidu
 */
import com.sun.mail.util.MailConnectException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailSenderWithAttachment {

    String recipientEmail;
    String subject;
    String body;
    String pdfFilePath;
    String fileName;
    String status;

    public EmailSenderWithAttachment(String recipientEmail, String subject, String body, String pdfFilePath, String fileName) {
//        this.recipientEmail = "webtestjanidu@gmail.com";
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.pdfFilePath = pdfFilePath;
        this.fileName = fileName;
    }

    public String sendMail() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // Submit the task with a time limit of 5 seconds

        Future<?> future = executorService.submit(() -> {
            try {
                process();
            } catch (InterruptedException ex) {

            }
        });

        try {
            // Wait for the task to complete, but timeout after 5 seconds
            future.get(10000, TimeUnit.MILLISECONDS);

        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // Cancel the task (may not be necessary, depending on your use case)
            future.cancel(true);
            status = "timeout";

        } finally {
            // Shutdown the executor service
            executorService.shutdown();
        }
//        System.out.println(status);
        return status;
    }

    private void process() throws InterruptedException {
        // Sender's email address and password
        String senderEmail = "janprabashwara@gmail.com";
        String senderPassword = "eumn gymb wfgf yzve";

        // Mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Creating a session with authentication
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Creating a MimeMessage object
            Message message = new MimeMessage(session);

            // Setting the sender and recipient addresses
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));

            // Setting the subject and content of the email
            message.setSubject(this.subject);

            // Create the message body part for text content
            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText(this.body);

            // Create the message body part for the attachment (PDF file)
            BodyPart attachmentBodyPart = new MimeBodyPart();
            String pdfFilePath = this.pdfFilePath; // Replace with the actual path to your PDF file
            DataSource source = new FileDataSource(pdfFilePath);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(fileName); // Set the desired file name for the attachment

            // Create a multipart message and add the body parts
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            // Set the multipart message as the email content
            message.setContent(multipart);

            // Sending the email
            Transport.send(message);

            status = "Success";
        } catch (MailConnectException e) {
            status = "failed";
        } catch (MessagingException e) {
            e.printStackTrace();
            status = "failedOhter";
        }
    }
}
