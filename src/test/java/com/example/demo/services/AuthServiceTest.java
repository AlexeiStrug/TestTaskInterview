package com.example.demo.services;

import com.example.demo.config.SecurityConfig;
import com.example.demo.domain.PasswordResetToken;
import com.example.demo.domain.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.auth.impl.AuthServiceImpl;
import com.example.demo.utils.InitData;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.DtoResponseBuilder;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import com.example.demo.web.dto.UserDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private SecurityConfig securityConfig;
    @Mock
    private DtoResponseBuilder responseBuilder;

    String token = "some_token";
    String url = "http://localhost:8080";
    String code = "123123";
    String hash = "asdasdhjasdjas";
    User user = null;
    User userWithEnableReset = null;
    User userWIthoutReset = null;
    PasswordResetToken passwordResetToken = null;
    UserDto userDto = null;
    CustomResponse customResponse = null;

    @Before
    public void setUp() {
        user = new InitData().initAdminUser();
        userWithEnableReset = new InitData().initAdminUser();
        userWIthoutReset = new InitData().initAdminUser();
        userDto = new InitData().initUserDto();
        passwordResetToken = new InitData().initPasswordResetToken();
    }

    @Test
    public void test_login_basic() {
        user.setToken(token);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Token generated", token);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(user));
        when(securityConfig.matchPasswords(any(String.class), any(String.class))).thenReturn(true);

        when(securityConfig.createJWT(any(String.class))).thenReturn(token);
        when(userRepository.save(any(User.class))).thenReturn(user);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(String.class))).thenReturn(customResponse);

        CustomResponse response = authService.login(userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Token generated");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), token);
    }

    @Test(expected = NotFoundException.class)
    public void test_login_whenUserNotFound() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        authService.login(userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_login_whenPasswordsNotMatch() {
        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(user));
        when(securityConfig.matchPasswords(any(String.class), any(String.class))).thenReturn(false);

        authService.login(userDto);
    }

    @Test
    public void test_forgotPassword_basic() {
        userWIthoutReset.setIsUserCanResetPassword(false);
        passwordResetToken.setUser(user);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Generate a url for reset password", url + "/api/v1/auth/reset?code=" + code);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWIthoutReset));
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(String.class))).thenReturn(customResponse);

        CustomResponse response = authService.forgotPassword(url, userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Generate a url for reset password");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), url + "/api/v1/auth/reset?code=" + code);
    }

    @Test
    public void test_forgotPassword_withResetFlag() {
        userWithEnableReset.setIsUserCanResetPassword(true);
        passwordResetToken.setUser(user);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Generate a url for reset password", url + "/api/v1/auth/reset?code=" + code);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWithEnableReset));
        when(passwordResetTokenRepository.findByUser(any(User.class))).thenReturn(passwordResetToken);
        when(passwordResetTokenRepository.findByToken(any(String.class))).thenReturn(passwordResetToken);
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(String.class))).thenReturn(customResponse);

        CustomResponse response = authService.forgotPassword(url, userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Generate a url for reset password");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), url + "/api/v1/auth/reset?code=" + code);
    }

    @Test
    public void test_forgotPassword_ifTokenNull() {
        userWithEnableReset.setIsUserCanResetPassword(true);
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(null);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Generate a url for reset password", url + "/api/v1/auth/reset?code=" + code);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWithEnableReset));
        when(passwordResetTokenRepository.findByUser(any(User.class))).thenReturn(passwordResetToken);
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(String.class))).thenReturn(customResponse);

        CustomResponse response = authService.forgotPassword(url, userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Generate a url for reset password");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
        Assert.assertEquals(response.getResult(), url + "/api/v1/auth/reset?code=" + code);
    }

    @Test
    public void test_resetPassword_basic() {
        userWithEnableReset.setIsUserCanResetPassword(true);
        userWIthoutReset.setIsUserCanResetPassword(false);
        userWIthoutReset.setPassword(hash);
        passwordResetToken.setUser(user);
        userDto.setPassword(hash);
        customResponse = new InitData().generateCustomResponse(ResponseType.SUCCESS, "Password successfully reset", "");

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWithEnableReset));

        when(passwordResetTokenRepository.findByToken(any(String.class))).thenReturn(passwordResetToken);
        doNothing().when(passwordResetTokenRepository).deleteById(passwordResetToken.getId());

        when(responseBuilder.buildResponse(any(ResponseType.class), any(String.class), any(Object.class))).thenReturn(customResponse);

        CustomResponse response = authService.resetPassword(code, userDto);

        Assert.assertNotNull(response);
        Assert.assertEquals(response, customResponse);
        Assert.assertEquals(response.getMessage(), "Password successfully reset");
        Assert.assertEquals(response.getResponseType(), ResponseType.SUCCESS);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_resetPassword_ifFlagForChangePasswordOnFalse() {
        userWIthoutReset.setIsUserCanResetPassword(false);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWIthoutReset));

        authService.resetPassword(code, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_resetPassword_ifCodeNull() {
        userWithEnableReset.setIsUserCanResetPassword(true);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWithEnableReset));

        authService.resetPassword("", userDto);
    }

    @Test(expected = NotFoundException.class)
    public void test_resetPassword_ifCodeDoesNotExist() {
        userWithEnableReset.setIsUserCanResetPassword(true);

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWithEnableReset));

        authService.resetPassword(code, userDto);
    }

    @Test(expected = ForbiddenAccessToTheSystem.class)
    public void test_resetPassword_ifPasswordEmpty() {
        userWithEnableReset.setIsUserCanResetPassword(true);
        userDto.setPassword("");

        when(userRepository.findByUsername(any(String.class))).thenReturn(java.util.Optional.ofNullable(userWithEnableReset));

        when(passwordResetTokenRepository.findByToken(any(String.class))).thenReturn(passwordResetToken);
        doNothing().when(passwordResetTokenRepository).deleteById(passwordResetToken.getId());

        authService.resetPassword(code, userDto);
    }
}
