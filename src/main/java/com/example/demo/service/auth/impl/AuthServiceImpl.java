package com.example.demo.service.auth.impl;

import com.example.demo.config.SecurityConfig;
import com.example.demo.domain.PasswordResetToken;
import com.example.demo.domain.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.auth.AuthService;
import com.example.demo.utils.builder.CustomResponse;
import com.example.demo.utils.builder.DtoResponseBuilder;
import com.example.demo.utils.builder.enums.ResponseType;
import com.example.demo.utils.exception.ForbiddenAccessToTheSystem;
import com.example.demo.utils.exception.NotFoundException;
import com.example.demo.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final SecurityConfig securityConfig;
    private final DtoResponseBuilder responseBuilder;

    @Override
    public CustomResponse login(UserDto loginDto) {
        User found = checkIfExistUser(loginDto);

        if (!securityConfig.matchPasswords(loginDto.getPassword(), found.getPassword()))
            throw new ForbiddenAccessToTheSystem("Incorrect password for username -> " + loginDto.getUsername());

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Token generated", GenerateAndSaveToken(loginDto, found));
    }

    @Override
    public CustomResponse resetPassword(String code, UserDto resetPasswordDto) {
        User found = checkIfExistUser(resetPasswordDto);

        if (!found.getIsUserCanResetPassword())
            throw new ForbiddenAccessToTheSystem("Forbidden, user can not change password with username -> " + resetPasswordDto.getUsername());

        if (code.equals(""))
            throw new ForbiddenAccessToTheSystem("Forbidden, user can not change password without reset code");

        PasswordResetToken passwordResetToken = checkIfCodeExist(code);
        passwordResetTokenRepository.deleteById(passwordResetToken.getId());

        UpdatePasswordAndSaveUser(resetPasswordDto, found);

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Password successfully reset", "");
    }

    @Override
    public CustomResponse forgotPassword(String url, UserDto forgotPasswordDto) {
        User found = checkIfExistUser(forgotPasswordDto);
        String code = checkCodeOrExist(found);

        String resetUrl = generateUrl(url, code);

        return responseBuilder.buildResponse(ResponseType.SUCCESS, "Generate a url for reset password", resetUrl);
    }

    private String checkCodeOrExist(User found) {
        PasswordResetToken passwordResetToken = null;
        if (found.getIsUserCanResetPassword()) {
            passwordResetToken = passwordResetTokenRepository.findByUser(found);
        } else {
            found.setIsUserCanResetPassword(true);
        }
        return GenerateCode(passwordResetToken, found);
    }

    private String GenerateCode(PasswordResetToken passwordResetToken, User user) {
        String code = null;
        if (passwordResetToken != null) {
            code = passwordResetToken.getToken();
            if (validatePasswordResetToken(code) == null || validatePasswordResetToken(code).equals("expired")) {
                passwordResetToken.updateToken(code);
                createOrUpdatePasswordResetTokenForUser(passwordResetToken, null, code);
            } else {
                code = UUID.randomUUID().toString();
                createOrUpdatePasswordResetTokenForUser(null, user, code);
            }
        } else {
            code = UUID.randomUUID().toString();
            createOrUpdatePasswordResetTokenForUser(null, user, code);
        }
        return code;
    }

    private User checkIfExistUser(UserDto loginDto) {
        Optional<User> found = userRepository.findByUsername(loginDto.getUsername().toLowerCase());
        if (found.isEmpty())
            throw new NotFoundException("User with username -> " + loginDto.getPassword() + " not found.");
        return found.get();
    }

    private PasswordResetToken checkIfCodeExist(String code) {
        PasswordResetToken found = passwordResetTokenRepository.findByToken(code);
        if (found == null)
            throw new NotFoundException("Can not find code -> " + code);
        return found;
    }


    private String GenerateAndSaveToken(UserDto loginDto, User found) {
        String token = securityConfig.createJWT(loginDto.getUsername().toLowerCase());
        found.setToken(token);
        userRepository.save(found);
        return token;
    }

    private void UpdatePasswordAndSaveUser(UserDto resetPasswordDto, User found) {
        if (resetPasswordDto.getPassword() == null || resetPasswordDto.getPassword().equals(""))
            throw new ForbiddenAccessToTheSystem("Forbidden, user can not change password with emptu password");

        found.setPassword(securityConfig.passwordEncoder(resetPasswordDto.getNewPassword()));
        found.setIsUserCanResetPassword(false);
        userRepository.save(found);
    }

    private void createOrUpdatePasswordResetTokenForUser(PasswordResetToken passwordResetToken, User user, String code) {
        if (passwordResetToken != null) passwordResetTokenRepository.save(passwordResetToken);
        if (user != null) {
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .user(user)
                    .token(code)
                    .build();
            passwordResetTokenRepository.save(resetToken);
        }

    }

    private String generateUrl(String url, String code) {
        return url + "/api/v1/auth/reset?code=" + code;
    }

    private String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken" : isTokenExpired(passToken) ? "expired" : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

}
