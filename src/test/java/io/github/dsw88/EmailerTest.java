package io.github.dsw88;

import mockit.Mock;
import mockit.MockUp;
import org.eclipse.egit.github.core.User;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dsw88
 */
public class EmailerTest {

    private Emailer emailer;

    @BeforeMethod
    public void setUp() {
        emailer = new Emailer("fakehost.localhost", 5908, TransportStrategy.SMTP_PLAIN);
    }

    @Test
    public void sendNoNameUserEmails_ShouldSendEmails_WhenEmailsListed() {
        List<User> noNameUsers = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("FakeEmail1");
        User user2 = new User();
        user2.setEmail("FakeEmail2");
        noNameUsers.add(user1);
        noNameUsers.add(user2);

        new MockUp<Mailer>() {
            @Mock(invocations = 2)
            public void $init(ServerConfig serverConfig, TransportStrategy transportStrategy) { }
        };
        emailer.sendNoNameUserEmails(noNameUsers);
    }

    @Test
    public void sendNoNameUserEmails_ShouldSkipSendingMail_WhenNoAddressListed() {
        List<User> noNameUsers = new ArrayList<>();
        User user1 = new User();
        user1.setEmail("FakeEmail1");
        User user2 = new User();
        user2.setEmail(null); //No email
        noNameUsers.add(user1);
        noNameUsers.add(user2);

        new MockUp<Mailer>() {
            @Mock(invocations = 1)
            public void $init(ServerConfig serverConfig, TransportStrategy transportStrategy) { }
        };
        emailer.sendNoNameUserEmails(noNameUsers);
    }


}
