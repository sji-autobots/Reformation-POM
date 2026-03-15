package com.reformation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class AccountPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By accountTrigger   = By.id("accountFlyoutTriggerDesktop");
    private final By myAccountLink    = By.cssSelector("a[data-account-link][href*='account']");
    private final By addressBookLink  = By.xpath("//a[contains(@href,'addresses')]");
    private final By orderHistoryLink = By.cssSelector("a.account-navigation--order-history");
    private final By passwordLink     = By.cssSelector("a.account-navigation--password");

    public AccountPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Hovers the account flyout and clicks My Account. */
    public void openMyAccount() throws InterruptedException {
        WebElement accountMenu = wait.until(
            ExpectedConditions.visibilityOfElementLocated(accountTrigger)
        );
        new Actions(driver).moveToElement(accountMenu).perform();
        Thread.sleep(800);

        WebElement link = wait.until(
            ExpectedConditions.presenceOfElementLocated(myAccountLink)
        );
        new Actions(driver).moveToElement(accountMenu).perform();
        Thread.sleep(300);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles:true}));", link
        );
        wait.until(ExpectedConditions.urlContains("account"));
    }

    /** Navigates from My Account to the Address Book page. */
    public void openAddressBook() throws InterruptedException {
        openMyAccount();

        WebElement addressBook = wait.until(
            ExpectedConditions.elementToBeClickable(addressBookLink)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addressBook);
        wait.until(ExpectedConditions.urlContains("addresses"));
    }

    /** Navigates from My Account to the Order History page. */
    public void openOrderHistory() throws InterruptedException {
        openMyAccount();

        WebElement orderHistory = wait.until(
            ExpectedConditions.elementToBeClickable(orderHistoryLink)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", orderHistory);
        wait.until(ExpectedConditions.urlContains("orders"));
    }

    /** Navigates from My Account to the Change Password page. */
    public void openPasswordPage() throws InterruptedException {
        openMyAccount();

        WebElement pwd = wait.until(
            ExpectedConditions.elementToBeClickable(passwordLink)
        );
        pwd.click();
    }
}
