package io.github.dsw88;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.UserService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author dsw88
 */
public class GithubActionsTest {

    @Mocked
    private OrganizationService mockOrganizationService;
    @Mocked
    private UserService mockUserService;
    private GithubActions githubActions;

    @BeforeMethod
    public void setUp() {
        githubActions = new GithubActions("fakeusername", "fakepassword");
        githubActions.organizationService = mockOrganizationService;
        githubActions.userService = mockUserService;
    }

    @Test
    public void getGithubUsername_ShouldAskForUsername(@Mocked Console mockConsole) {
        String username = "FakeUsername";
        new MockUp<System>() {
            @Mock
            Console console() {
                return mockConsole;
            }
        };
        new NonStrictExpectations() {
            {
                mockConsole.readLine(anyString);
                times = 1;
                result = username;
            }
        };
        String result = GithubActions.getGithubUsername();
        assertEquals(result, username);
    }

    @Test
    public void getGithubPassword_ShouldAskForPassword(@Mocked Console mockConsole) {
        String password = "FakePassword";
        new MockUp<System>() {
            @Mock
            Console console() {
                return mockConsole;
            }
        };
        new NonStrictExpectations() {
            {
                mockConsole.readPassword(anyString);
                times = 1;
                result = password.toCharArray();
            }
        };
        String result = GithubActions.getGithubPassword();
        assertEquals(result, password);
    }

    @Test
    public void getUsersInOrganization_ShouldReturnUserList() throws IOException {
        String githubOrg = "FakeOrg";
        List<User> orgUsers = new ArrayList<>();
        User orgUser = new User();
        orgUser.setLogin("FakeLogin");
        orgUsers.add(orgUser);

        User fullUser = new User();
        fullUser.setLogin("FakeLogin");
        fullUser.setName("FakeName");

        new NonStrictExpectations() {
            {
                mockOrganizationService.getMembers(githubOrg);
                times = 1;
                result = orgUsers;

                mockUserService.getUser(orgUser.getLogin());
                times = 1;
                result = fullUser;
            }
        };

        List<User> retUsers = githubActions.getUsersInOrganization(githubOrg);
        assertNotNull(retUsers);
        assertEquals(retUsers.size(), 1);
        assertNotNull(retUsers.get(0).getName());
    }

}
