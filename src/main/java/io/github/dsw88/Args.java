package io.github.dsw88;

import org.apache.commons.cli.*;

/**
 * @Author dsw88
 */
public class Args {

    public static CommandLine parseArgs(String[] args) {
        /*
         * Thanks to http://stackoverflow.com/questions/367706/how-to-parse-command-line-arguments-in-java for the
         * intro on using commons-cli
         */
        Options cliOpts = new Options();

        Option s3BucketOpt = new Option("s", "s3BucketName", true, "The name of the S3 bucket to use");
        s3BucketOpt.setRequired(true);
        cliOpts.addOption(s3BucketOpt);

        Option githubOrgOpt = new Option("g", "githubOrgName", true, "The name of the GitHub organization to analyze");
        githubOrgOpt.setRequired(true);
        cliOpts.addOption(githubOrgOpt);

        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(cliOpts, args);
        }
        catch(ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("org-sweeper", cliOpts);
            return null;
        }
    }

}
