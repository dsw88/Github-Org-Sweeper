package io.github.dsw88;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dsw88
 */
public class GithubActions {

    public static String getGithubUsername() {
        return System.console().readLine("Enter your GitHub username: ");
    }

    public static String getGithubPassword() {
        return new String(System.console().readPassword("Enter your GitHub password: "));
    }

    OrganizationService organizationService;
    UserService userService;

    public GithubActions(String username, String password) {
        this.organizationService = new OrganizationService();
        this.organizationService.getClient().setCredentials(username, password);
        this.userService = new UserService();
        this.userService.getClient().setCredentials(username, password);
    }

    public List<User> getUsersInOrganization(String githubOrg) throws IOException {
        List<User> retUsers = new ArrayList<>();
        List<User> orgUsers = organizationService.getMembers(githubOrg);
        for(User orgUser : orgUsers) {
            /*
             * The org members call doesn't seem to return the full user record, and there doesn't seem to be
             * a batch lookup for the list of users, so we need to call them one at a time
             */
            User user = userService.getUser(orgUser.getLogin());
            retUsers.add(user);
        }
        return retUsers;
    }

}
