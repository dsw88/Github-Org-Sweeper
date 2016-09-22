package io.github.dsw88;

import org.apache.commons.cli.*;
import org.eclipse.egit.github.core.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author dsw88
 */
public class MainClass {

    public static void main(String[] args) {
        //Parse command-line args and validate credentials
        CommandLine opts = Args.parseArgs(args);
        if(opts == null) { //Null means args parsing failed due to incorrect or missing
            System.exit(1);
        }
        if(!S3Actions.validateS3Credentials()) {
            System.out.println("You must provide credentials for your S3 bucket. Please set them in one of the following manners: \n" +
                "* Environment variables: 'AWS_ACCESS_KEY_ID' and 'AWS_SECRET_ACCESS_KEY'\n" +
                "* Java system properties: 'aws.accessKeyId' and 'aws.secretKey'\n" +
                "* Credential profiles file: '~/.aws/credentials' (see http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html for more details)");
            System.exit(1);
        }

        //Get parameters and credentials
        String s3Bucket = opts.getOptionValue("s3BucketName");
        String githubOrg = opts.getOptionValue("githubOrgName");
        String githubUsername = GithubActions.getGithubUsername();
        String githubPassword = GithubActions.getGithubPassword();

        try {
            GithubActions githubActions = new GithubActions(githubUsername, githubPassword);
            List<User> noNameUsers = githubActions.getUsersInOrganization(githubOrg)
                    .stream()
                    .filter(user -> user.getName() == null || user.getName().equals(""))
                    .collect(Collectors.toList());

            if(noNameUsers.size() > 0) {
                Emailer emailer = new Emailer();
                emailer.sendNoNameUserEmails(noNameUsers);

                S3Actions s3Actions = new S3Actions(s3Bucket);
                s3Actions.saveGithubUsersToS3(noNameUsers);
            }
            else {
                System.out.println("All your organization users have names listed on their GitHub profiles!");
            }
        }
        catch(Exception e) {
            System.err.println("Exception during program execution: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

}
