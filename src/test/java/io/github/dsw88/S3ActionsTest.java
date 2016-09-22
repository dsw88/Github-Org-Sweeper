package io.github.dsw88;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.eclipse.egit.github.core.User;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author dsw88
 */
public class S3ActionsTest {

    @Mocked
    private AmazonS3 mockAmazonS3;
    private S3Actions s3Actions;
    private String bucketName;

    @BeforeMethod
    public void setUp() {
        bucketName = "FakeBucketName";
        s3Actions = new S3Actions(bucketName);
        s3Actions.s3 = mockAmazonS3;
    }

    @Test
    public void validateS3Credentials_ShouldReturnTrue_WhenCredsExist() {
        System.setProperty("aws.accessKeyId", "FakeAccessKey");
        System.setProperty("aws.secretKey", "FakeSecretKey");

        assertTrue(S3Actions.validateS3Credentials());

        System.clearProperty("aws.accessKeyId");
        System.clearProperty("aws.secretKey");
    }

    @Test
    public void validateS3Credentials_ShouldReturnFalse_WhenCredsDontExist() {
        //Change user.home in case they do have a creds file on their machine
        String origUserHome = System.getProperty("user.home");
        System.setProperty("user.home", "/tmp");

        assertFalse(S3Actions.validateS3Credentials());

        //Reset user home to original value
        System.setProperty("user.home", origUserHome);
    }

    @Test
    public void saveGitHubUsersToS3_ShouldPostFile() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setLogin("FakeLogin1");
        users.add(user1);
        User user2 = new User();
        user2.setLogin("FakeLogin2");
        users.add(user2);

        new NonStrictExpectations() {
            {
                mockAmazonS3.putObject((PutObjectRequest) any);
                times = 1;
            }
        };

        s3Actions.saveGithubUsersToS3(users);
    }

}
