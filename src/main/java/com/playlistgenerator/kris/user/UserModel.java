package com.playlistgenerator.kris.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserModel {
    @NotEmpty(message = "Username field required.")
    @Size(min=4, message="Username field must be at least 3 characters long.")
    private String username;
    @NotEmpty(message = "Password field is required.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;
    @NotEmpty(message = "Confirm password field is required.")
    @Size(min = 8, message = "Confirm password field must be at least 8 characters long.")
    private String confirmPassword;
    @NotEmpty(message = "Email field is required.")
    @Email(message = "Email field is not a valid email address.")
    private String email;

    public UserModel() {
    }
    public UserModel(String username, String password, String confirmPassword, String email) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
