package tools.mailing;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	private final static String username = "momentoparis@gmail.com"; 
	private final static String password = "Mom en to";
	private final static String from="momentoparis@gmail.com"; 

	public static void sendMail(String to, String subject, String contenu) {
		
		final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		// Get a Properties object
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", "smtp.gmail.com");
		props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.store.protocol", "pop3");
		props.put("mail.transport.protocol", "smtp");

		try{
			Session session = Session.getDefaultInstance(props,new Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);}});

			// -- Create a new message --
			Message msg = new MimeMessage(session);

			// -- Set the FROM and TO fields --
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to,false));
			
			msg.setSubject(subject);
			msg.setText(contenu);
			msg.setSentDate(new Date());
			Transport.send(msg);
		}catch (MessagingException e){ System.out.println("Erreur d'envoi, cause: " + e);}
	}
	
	public static void main(String[] args) {
		sendMail("momentoparis@gmail.com", "subject", "contenu"); }
}