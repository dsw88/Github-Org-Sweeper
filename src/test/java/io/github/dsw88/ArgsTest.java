package io.github.dsw88;

import org.apache.commons.cli.CommandLine;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * @Author dsw88
 */
public class ArgsTest {

    @Test
    public void parseArgs_ShouldReturnParsedOptions_WhenGivenCorrectArgs() {
        String githubOrg = "my-fake-org";
        String s3Bucket = "my-fake-bucket-name";
        String[] args = new String[4];
        args[0] = "--githubOrgName";
        args[1] = githubOrg;
        args[2] = "--s3BucketName";
        args[3] = s3Bucket;

        CommandLine cli = Args.parseArgs(args);

        assertNotNull(cli);
        assertEquals(cli.getOptionValue("githubOrgName"), githubOrg);
        assertEquals(cli.getOptionValue("s3BucketName"), s3Bucket);
    }

    @Test
    public void parseArgs_ShouldReturnNull_WhenMissingArgs() {
        String githubOrg = "my-fake-org";
        String[] args = new String[2];
        args[0] = "--githubOrgName";
        args[1] = githubOrg;

        CommandLine cli = Args.parseArgs(args);

        assertNull(cli);
    }

    @Test
    public void parseArgs_ShouldReturnNull_WhenExtraArgs() {
        String githubOrg = "my-fake-org";
        String s3Bucket = "my-fake-bucket-name";
        String[] args = new String[6];
        args[0] = "--githubOrgName";
        args[1] = githubOrg;
        args[2] = "--s3BucketName";
        args[3] = s3Bucket;
        args[4] = "--somethingElse";
        args[5] = "SomeOtherValue";

        CommandLine cli = Args.parseArgs(args);

        assertNull(cli);
    }

}
