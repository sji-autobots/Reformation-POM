package com.reformation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class PasswordPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By currentPasswordField = By.id("currentPassword");
    private final By newPasswordField     = By.name("dwfrm_profile_login_newpasswords_newpassword");
    private final By confirmPasswordField = By.id("newPasswordConfirm");
    private final By saveBtn              = By.name("save");

    public PasswordPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Fills in the password change form and submits it. */
    public void updatePassword(String currentPassword, String newPassword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(currentPasswordField))
            .sendKeys(currentPassword);

        wait.until(ExpectedConditions.visibilityOfElementLocated(newPasswordField))
            .sendKeys(newPassword);

        driver.findElement(confirmPasswordField).sendKeys(newPassword);

        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }
}
