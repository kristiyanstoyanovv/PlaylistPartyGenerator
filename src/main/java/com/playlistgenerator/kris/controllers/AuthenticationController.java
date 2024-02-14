package com.playlistgenerator.kris.controllers;

import com.playlistgenerator.kris.security.token.TokenEntity;
import com.playlistgenerator.kris.security.token.TokenService;
import com.playlistgenerator.kris.user.UserEntity;
import com.playlistgenerator.kris.user.UserModel;
import com.playlistgenerator.kris.user.UserService;
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
    private final TokenService tokenService;

    public AuthenticationController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        System.out.println("Login form");
        return "authentication/login-page";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        UserModel userModel = new UserModel();
        model.addAttribute("userModel", userModel);
        System.out.println("Register form");
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
        System.out.println("Successfully registered");
        String temp = "http://localhost:8080/auth/register/confirm?token="+token;
        redirectAttributes.addFlashAttribute("registrationComplete", temp);
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

        tokenService.setConfirmedAt(tokenObject.get());
        userService.enableUser(tokenObject.get().getUser().getUserId());
        redirectAttributes.addFlashAttribute("accountActivated", 200);

        return "redirect:/auth/login";
    }

}
