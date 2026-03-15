package com.reformation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By popupCloseBtn = By.cssSelector("button[data-modal-close='true']");
    private final By accountTrigger = By.id("accountFlyoutTriggerDesktop");
    private final By signInBtn     = By.cssSelector("#sign-in-button");
    private final By emailField    = By.id("login-form-email");
    private final By passwordField = By.id("login-form-password");
    private final By loginBtn      = By.cssSelector("button[data-action='login']");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Dismisses the welcome popup if it appears. */
    public void dismissPopup() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(popupCloseBtn)).click();
        } catch (Exception ignored) {}
    }

    /** Performs a full login flow. */
    public void login(String email, String password) throws InterruptedException {
        dismissPopup();

        WebElement accountBtn = wait.until(
            ExpectedConditions.presenceOfElementLocated(accountTrigger)
        );
        new Actions(driver).moveToElement(accountBtn).perform();
        Thread.sleep(1000);

        WebElement signIn = wait.until(
            ExpectedConditions.presenceOfElementLocated(signInBtn)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signIn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField))
            .sendKeys(email);

        driver.findElement(passwordField).sendKeys(password);

        Thread.sleep(800);

        WebElement login = wait.until(
            ExpectedConditions.presenceOfElementLocated(loginBtn)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", login);

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("account"),
            ExpectedConditions.invisibilityOfElementLocated(emailField)
        ));
    }
}
