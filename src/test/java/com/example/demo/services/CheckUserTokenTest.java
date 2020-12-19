package com.example.demo.services;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.CheckUserToken;
import com.example.demo.utils.InitData;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckUserTokenTest {

    @InjectMocks
    private CheckUserToken checkUserToken;

    @Mock
    private UserRepository userRepository;

    User user = null;
    String token = "some_token";

    @Before
    public void setUp() {
        user = new InitData().initAdminUser();
    }

    @Test
    public void test_loadUserByToken_basic() {
        when(userRepository.findByToken(token)).thenReturn(Optional.ofNullable(user));

        User response = checkUserToken.loadUserByToken(token);

        Assert.assertNotNull(response);
        Assert.assertEquals(user, response);
    }

    @Test(expected = NotFoundException.class)
    public void test_loadUserByToken_ifUserNotFound() {
        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        checkUserToken.loadUserByToken(token);
    }
}
