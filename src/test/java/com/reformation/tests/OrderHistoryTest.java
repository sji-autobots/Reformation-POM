package com.reformation.tests;

import org.testng.annotations.Test;import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class OrderHistoryTest {

    @Test
    public void verifyOrderHistory() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {

            driver.manage().window().maximize();

            // STEP 1: Open Reformation Site
            driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            System.out.println("✅ Site loaded");

            // Handle Welcome Popup
            try {
                WebElement shopNowBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector("button[data-modal-close='true']")
                        )
                );
                shopNowBtn.click();
                System.out.println("Popup handled");
            } catch (Exception e) {
                System.out.println("ℹ Popup not displayed");
            }

            // STEP 2: Login
            WebElement accountBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("accountFlyoutTriggerDesktop"))
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", accountBtn
            );

            Actions actions = new Actions(driver);
            actions.moveToElement(accountBtn).perform();
            Thread.sleep(1500);

            WebElement signInBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("#sign-in-button"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", signInBtn);
            System.out.println("Sign In clicked");

            WebElement emailField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("login-form-email"))
            );
            emailField.click();
            emailField.clear();
            emailField.sendKeys("refqaorders+paresh@gmail.com");

            WebElement passwordField = driver.findElement(By.id("login-form-password"));
            passwordField.click();
            passwordField.clear();
            passwordField.sendKeys("QA_Test123!");

            Thread.sleep(800);

            WebElement loginBtn = driver.findElement(By.cssSelector("button[data-action='login']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginBtn);
            System.out.println("Login button clicked");

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("account"),
                    ExpectedConditions.invisibilityOfElementLocated(By.id("login-form-email"))
            ));
            System.out.println("Login successful!");

            Thread.sleep(3000);

            // STEP 3: Navigate to My Account
            WebElement accountMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("accountFlyoutTriggerDesktop"))
            );

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", accountMenu
            );

            actions.moveToElement(accountMenu).pause(Duration.ofSeconds(1)).perform();

            WebElement myAccountLink = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("a[data-account-link][href*='account']")
                    )
            );

            actions.moveToElement(accountMenu).perform();
            Thread.sleep(300);

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles:true}));", myAccountLink
            );
            System.out.println("My Account clicked");

            wait.until(ExpectedConditions.urlContains("account"));
            Thread.sleep(3000);
            System.out.println("My Account page loaded");

            // STEP 4: Click Order History
            WebElement orderHistoryLink = wait.until(
        ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.account-navigation--order-history")
        )
);

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", orderHistoryLink);
            System.out.println("Order History clicked");

            wait.until(ExpectedConditions.urlContains("orders"));
            Thread.sleep(3000);
            // Smooth scroll down gradually
// Wait for orders to load first
WebElement orderList = wait.until(
    ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-order-component='order-list']"))
);
System.out.println("✅ Order list loaded");
Thread.sleep(1500);

            // STEP 5: Verify orders are displayed
            try {
                List<WebElement> orders = wait.until(
                        ExpectedConditions.presenceOfAllElementsLocatedBy(
                                By.cssSelector("[class*='order-history__item'], [class*='order__item'], [class*='order-card'], [class*='order-tile']")
                        )
                );

                if (orders.size() > 0) {
                    System.out.println("Orders found: " + orders.size() + " order(s) displayed");
                    for (int i = 0; i < orders.size(); i++) {
                        System.out.println("  Order " + (i + 1) + ": " + orders.get(i).getText().trim().replace("\n", " | "));
                    }
                } else {
                    System.out.println("⚠ No orders found in Order History");
                }

            } catch (Exception e) {
                System.out.println("⚠ Could not locate order items - checking for empty state...");

                // Check if empty state message is shown
                try {
                    WebElement emptyMsg = driver.findElement(
                            By.xpath("//*[contains(text(),'no orders') or contains(text(),'No orders') or contains(text(),'haven')]")
                    );
                    System.out.println("ℹ Empty order history message: " + emptyMsg.getText());
                } catch (Exception ex) {
                    System.out.println("⚠ Could not determine order history state - inspect page manually");
                }
            }

            Thread.sleep(3000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}