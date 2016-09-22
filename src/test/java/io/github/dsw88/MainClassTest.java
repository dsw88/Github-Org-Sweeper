package io.github.dsw88;

import mockit.Mock;
import mockit.MockUp;
import org.eclipse.egit.github.core.User;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dsw88
 */
public class MainClassTest {

    @Test
    public void main_ShouldPerformProgramExecution() {
        //Set up expectations
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setLogin("FakeLogin");
        users.add(user);
        new MockUp<GithubActions>() {
            @Mock(invocations = 1)
            public void $init(String username, String password) {}

            @Mock(invocations = 1)
            public List<User> getUsersInOrganization(String orgName) { return users; }

            @Mock(invocations = 1)
            public String getGithubUsername() { return "FakeUsername"; }

            @Mock(invocations = 1)
            public String getGithubPassword() { return "FakePassword"; }
        };
        new MockUp<Emailer>() {
            @Mock(invocations = 1)
            public void $init() {}

            @Mock(invocations = 1)
            public void sendNoNameUserEmails(List<User> users) {}
        };
        new MockUp<S3Actions>() {
            @Mock(invocations = 1)
            public void $init(String s3Bucket) {}

            @Mock(invocations = 1)
            public void saveGithubUsersToS3(List<User> users) {}
        };

        //Set up command line and properties
        String[] args = new String[4];
        args[0] = "--githubOrgName";
        args[1] = "FakeOrg";
        args[2] = "--s3BucketName";
        args[3] = "FakeBucket";
        System.setProperty("aws.accessKeyId", "FakeKey");
        System.setProperty("aws.secretKey", "FakeSecretKey");

        //Execute test
        MainClass.main(args);

        //Clean up
        System.clearProperty("aws.accessKeyId");
        System.clearProperty("aws.secretKey");
    }

}
