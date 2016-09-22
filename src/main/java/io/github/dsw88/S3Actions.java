package io.github.dsw88;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.eclipse.egit.github.core.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dsw88
 */
public class S3Actions {

    public static boolean validateS3Credentials() {
        //First check env variables
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        if(accessKey != null && secretKey != null) {
            return true;
        }

        //Next check system properties
        accessKey = System.getProperty("aws.accessKeyId");
        secretKey = System.getProperty("aws.secretKey");
        if(accessKey != null && secretKey != null) {
            return true;
        }

        //Finally check for ~/.aws/credentials
        String homeDir = System.getProperty("user.home");
        if(Files.exists(Paths.get(homeDir + "/.aws/credentials"))) {
            return true;
        }

        //Else we don't have credentials
        return false;
    }

    String bucketName;
    AmazonS3 s3;

    public S3Actions(String bucketName) {
        this.bucketName = bucketName;
        this.s3 = new AmazonS3Client();
    }

    public void saveGithubUsersToS3(List<User> users) {
        String fileContent = getLineDelimitedUsernameList(users);
        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        String key = "GitHub_NoNameUsers_" + new Date().getTime() + ".txt";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength((long) fileContent.getBytes().length);

        s3.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata));
    }

    private String getLineDelimitedUsernameList(List<User> users) {
        return users.stream()
                .map(User::getLogin)
                .collect(Collectors.joining("\n"));
    }

}
