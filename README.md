# Org-Sweeper
This repository contains the code for my GitHub organization sweeper. It takes a GitHub organization name and S3 bucket
as input, then analyzes the GitHub organization to find users without names listed in their profile. It sends an email
to those users (if they have an email defined), and puts a line-delimited file of their GitHub usernames in the S3 bucket.

# Language Choice
I've been writing a lot of Java at work lately, so I'm most familiar with it at the moment. Let me know if you'd like me to
write this in a dynamically-typed language and I can produce an example using Python or Javascript.

Java as a platform has a lot of mature libraries, which means there's always a library to use. However, Java as a language
tends to be much more verbose than other languages. I can usually write an equivalent Python script in fewer lines of code.

I haven't been able to use Java 8 at work yet (migration issues from Java 7 to 8 that will take a while), so this is
the first time I'm trying out the new streams and lambda syntax in Java 8. So far I like them, although I've encountered
 a few more gotchas than in equivalent functional facilities in other languages I've used like Javascript and Ruby.

# Assumptions
* You have a local SMTP server installed on the machine where you are running this program.
* You have a valid GitHub organization, and the credentials you pass will be for a user is in that organization in order to read the private members.
* You have a valid S3 bucket with the appropriate IAM policies configured for your credentials to allow you to write files.
* The program should notify, but not fail, if a user's profile doesn't have a public email address to send to.
* The program should notify, but not fail, if one or more emails is unable to be sent. It will still store the file in the S3 bucket.

# Instructions
1. Make sure you have the Java 8 runtime installed. You can download it here: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
2. Make sure you have an SMTP server installed and running. The instructions will be different for each OS:
    * Mac OSX: Postfix should already be installed, so you should be able to run the following:
    ```
    sudo postfix start
    ```
    * Windows: TODO
    * Linux: Postfix may not be installed yet depending on your distro, so you might have to install it first before starting it (Debian-based distro example):
    ```
    sudo apt-get update
    sudo apt-get install postfix
    sudo service postfix start
    ```
3. Grab the precompiled JAR in the Releases section of the repository. You can also compile the JAR yourself using Maven if you wish.
4. Add your Amazon credentials using one of the following methods:
    * Set them in the environment variables AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY
    * Pass them as Java system properties when starting the program: aws.accessKeyId and aws.secretKey
    * Set them in ~/.aws/credentials (see http://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html for more details)
5. Invoke the program with the following command (Replace <githubOrg> and <s3Bucket> with your GitHub organization name and S3 bucket name):
  ```
  java -jar org-sweeper-1.0.0.jar --githubOrgName <githubOrg> --s3BucketName <s3Bucket>
  ```
6. The program will prompt for your GitHub credentials.

# Final Words
Let me know if I've misunderstood anything in the instructions and I can make changes accordingly. Thanks!