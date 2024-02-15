package com.playlistgenerator.kris.controllers;

import com.playlistgenerator.kris.security.email.EmailService;
import com.playlistgenerator.kris.security.token.TokenEntity;
import com.playlistgenerator.kris.security.token.TokenService;
import com.playlistgenerator.kris.user.UserEntity;
import com.playlistgenerator.kris.user.UserModel;
import com.playlistgenerator.kris.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final EmailService emailService;
    private final TokenService tokenService;

    public AuthenticationController(UserService userService, EmailService emailService, TokenService tokenService) {
        this.userService = userService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        //System.out.println("Login form");
        return "authentication/login-page";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        UserModel userModel = new UserModel();
        model.addAttribute("userModel", userModel);
        //System.out.println("Register form");
        return "authentication/register-page";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("userModel") UserModel userModel,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (userService.doesUserExistsByUsername(userModel.getUsername())) {
            bindingResult.rejectValue("username",null,
                    "Account with this username already exists.");
            return "authentication/register-page";
        }
        if (userService.doesUserExistsByEmail(userModel.getEmail())) {
            bindingResult.rejectValue("email",null,
                    "Account with this email already exists.");
            return "authentication/register-page";
        }

        if (bindingResult.hasErrors()) {
            System.err.println("Error registering");
            return "authentication/register-page";
        }
        if (!userModel.getPassword().equals(userModel.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword",null,
                    "Passwords do not match.");
            return "authentication/register-page";
        }
        String token = UUID.randomUUID().toString();
        UserEntity user = new UserEntity(userModel);
        userService.createUser(user);
        tokenService.saveToken(new TokenEntity(user, token, LocalDateTime.now()));
        //System.out.println("Successfully registered");
        String link = "http://localhost:8080/auth/register/confirm?token="+token;
        emailService.send(user.getEmail(),
                          emailService.buildConfirmationEmail(user.getUsername(), link),
                "PPG | Please confirm your email");
        redirectAttributes.addFlashAttribute("registrationComplete", 200);
        return "redirect:/auth/login";
    }

    @GetMapping("/register/confirm")
    public String confirmRegistration(@RequestParam("token") String token,
                                      RedirectAttributes redirectAttributes) {
        Optional<TokenEntity> tokenObject = tokenService.getToken(token);
        if (tokenObject.isEmpty()) {
            redirectAttributes.addFlashAttribute("invalidToken", 404);
            return "redirect:/auth/login";
        }

        if (tokenObject.get().getConfirmedAt() != null) {
            redirectAttributes.addFlashAttribute("tokenUsed", 404);
            return "redirect:/auth/login";
        }


        LocalDateTime tokenExpireTime = tokenObject.get().getCreatedAt().plusMinutes(10);
        if (tokenExpireTime.isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("tokenExpired", 404);
            return "redirect:/auth/login";
        }

        tokenService.setConfirmedAt(tokenObject.get(), LocalDateTime.now());
        userService.enableUser(tokenObject.get().getUser().getUserId());
        redirectAttributes.addFlashAttribute("accountActivated", 200);

        return "redirect:/auth/login";
    }

    @GetMapping("/password")
    public String showPasswordRecoveryPage() {
        //System.out.println("Password recovery form");
        return "authentication/password-recovery-page";
    }

    @PostMapping("/password")
    public String processPasswordRecovery(@RequestParam("email") String email,
                                          RedirectAttributes redirectAttributes) {
        Optional<UserEntity> userObject = userService.findByEmail(email);
        if (userObject.isEmpty()) {
            redirectAttributes.addFlashAttribute("emailNotFound", 404);
            return "redirect:/auth/password";
        }

        String token = UUID.randomUUID().toString();
        String link = "http://localhost:8080/auth/password/reset?token=" + token;
        TokenEntity tokenObject = new TokenEntity(userObject.get(),token,LocalDateTime.now());
        tokenService.saveToken(tokenObject);
        String message = emailService.buildPasswordRecoveryEmail(userObject.get().getUsername(),link);
        emailService.send(email,message,"PPG | Reset password");
        redirectAttributes.addFlashAttribute("passwordRecoveryRequested",200);
        return "redirect:/auth/login";
    }

    @GetMapping("/password/reset")
    public String confirmPasswordRecovery(@RequestParam("token") String token,
                                          RedirectAttributes redirectAttributes,
                                          HttpSession session) {

        if (session.getAttribute("passwordResetToken") != null)
            return "authentication/password-recovery-page-two";

        Optional<TokenEntity> tokenObject = tokenService.getToken(token);

        if (tokenObject.isEmpty()) {
            redirectAttributes.addFlashAttribute("invalidToken", 404);
            return "redirect:/auth/login";
        }

        if (tokenObject.get().getConfirmedAt() != null) {
            redirectAttributes.addFlashAttribute("tokenUsed", 404);
            return "redirect:/auth/login";
        }

        LocalDateTime tokenExpireTime = tokenObject.get().getCreatedAt().plusMinutes(10);
        if (tokenExpireTime.isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("tokenExpired", 404);
            return "redirect:/auth/login";
        }

        session.setAttribute("passwordResetToken", tokenObject.get());
        return "authentication/password-recovery-page-two";
    }

    @PostMapping("/password/reset")
    public String handlePasswordChange(HttpSession session,
                                       @RequestParam("password") String password,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes) {
        TokenEntity tokenObject = (TokenEntity) session.getAttribute("passwordResetToken");
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("passwordsNotMatching","400");
            return "redirect:/auth/password/reset?token=" + tokenObject.getToken();
        }

        UserEntity user = tokenObject.getUser();
        user.setPassword(password);
        tokenService.setConfirmedAt(tokenObject, LocalDateTime.now());
        userService.updatePassword(user);
        session.removeAttribute("passwordResetToken");
        redirectAttributes.addFlashAttribute("passwordChangedSuccessfully",200);
        return "redirect:/auth/login";
    }


}
