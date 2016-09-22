package io.github.dsw88;

import org.eclipse.egit.github.core.User;
import org.simplejavamail.email.Email;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;

import java.util.List;

/**
 * @Author dsw88
 */
public class Emailer {

    private TransportStrategy transportStrategy;
    private String mailHost;
    private int mailPort;

    public Emailer() {
        mailHost = "localhost";
        mailPort = 25;
        transportStrategy = TransportStrategy.SMTP_PLAIN;
    }

    public Emailer(String mailHost, int mailPort, TransportStrategy transportStrategy) {
        this.mailHost = mailHost;
        this.mailPort = mailPort;
        this.transportStrategy = transportStrategy;
    }

    public void sendNoNameUserEmails(List<User> users) {
        for (User user : users) {
            try {
                if(user.getEmail() == null) {
                    System.out.println("Could not email GitHub user " + user.getLogin() + ". They don't have a public email listed");
                    continue;
                }

                String emailText = "Hi " + user.getLogin() + "!\n\nYour GitHub profile is missing a name; please go to " +
                        "https://github.com/settings/profile to enter your name.\n\nThanks!\n-The Admin";

                Email email = new EmailBuilder()
                        .from("Admin", "admin")
                        .to(user.getLogin(), user.getEmail())
                        .subject("Missing Name in GitHub Profile")
                        .text(emailText)
                        .build();

                new Mailer(
                        new ServerConfig(mailHost, mailPort),
                        transportStrategy
                ).sendMail(email);
            }
            catch(Exception e) {
                System.err.println("Couldn't send email to " + user.getEmail() + ". Message=" + e.getMessage());
            }
        }
    }

}
