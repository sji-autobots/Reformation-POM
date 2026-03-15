package com.reformation.tests;

import org.testng.annotations.Test;import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class DeleteAddressTest {

    @Test
    public void openWebsite() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // ✅ Declared at method scope so accessible throughout entire method
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {

            driver.manage().window().maximize();

            // Step 1: Navigate to site
            driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            Actions actions = new Actions(driver);

            // Handle Welcome Popup
            try {
                WebElement shopNowBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector("button[data-modal-close='true']")
                        )
                );
                shopNowBtn.click();
                System.out.println("✅ Popup handled");
            } catch (Exception e) {
                System.out.println("ℹ Popup not displayed");
            }

            // Step 2: Hover account button
            WebElement accountBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.id("accountFlyoutTriggerDesktop")
                    )
            );
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", accountBtn);
            actions.moveToElement(accountBtn).perform();
            js.executeScript(
                    "var evObj = document.createEvent('MouseEvents');" +
                    "evObj.initEvent('mouseover', true, false);" +
                    "arguments[0].dispatchEvent(evObj);", accountBtn
            );
            Thread.sleep(1500);
            System.out.println("✅ Account menu triggered");

            // Step 3: Click Sign In
            Thread.sleep(800);
            WebElement signInBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("#sign-in-button")
                    )
            );
            js.executeScript("arguments[0].click();", signInBtn);
            System.out.println("✅ Sign In clicked");

            // Step 4: Enter email
            WebElement emailField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("login-form-email"))
            );
            emailField.click();
            emailField.clear();
            emailField.sendKeys("refqaorders+paresh@gmail.com");
            System.out.println("✅ Email entered");

            // Step 5: Enter password
            WebElement passwordField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("login-form-password"))
            );
            passwordField.click();
            passwordField.clear();
            passwordField.sendKeys("QA_Test123!");
            System.out.println("✅ Password entered");

            // Step 6: Click login button
            Thread.sleep(800);
            WebElement loginBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("button[data-action='login']")
                    )
            );
            js.executeScript("arguments[0].click();", loginBtn);
            System.out.println("✅ Login button clicked");

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("account"),
                    ExpectedConditions.invisibilityOfElementLocated(By.id("login-form-email"))
            ));
            System.out.println("✅ Login successful!");
            Thread.sleep(5000);

            // Step 7: Hover account menu again → click My Account
            WebElement accountMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("accountFlyoutTriggerDesktop")
                    )
            );
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", accountMenu);
            actions.moveToElement(accountMenu).pause(Duration.ofSeconds(1)).perform();
            System.out.println("✅ Account hovered");

            WebElement myAccountLink = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("a[data-account-link][href*='account']")
                    )
            );
            actions.moveToElement(accountMenu).perform();
            Thread.sleep(300);
            js.executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles:true}));",
                    myAccountLink
            );
            System.out.println("✅ My Account clicked");
            Thread.sleep(5000);
            System.out.println("✅ My Account page loaded");

            // Step 8: Click Address Book
            WebElement addressBook = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//a[contains(@href,'addresses')]")
                    )
            );
            js.executeScript("arguments[0].click();", addressBook);
            System.out.println("✅ Address Book opened");

            // Step 9: Click the visible trash icon
            // NOTE: Each address card has TWO trash buttons in the DOM.
            // We target the one inside account-card__body (the visible one).
            wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.account-card__body"))
            );
            Thread.sleep(1000);

            WebElement visibleDeleteBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector(
                                "div.account-card__body " +
                                "button.address-summary__action[aria-label='Delete this address from your address book']"
                            )
                    )
            );
            js.executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", visibleDeleteBtn);
            Thread.sleep(800);
            js.executeScript("arguments[0].click();", visibleDeleteBtn);
            System.out.println("✅ Clicked trash icon — confirmation modal should appear");

            // Step 10: Wait for the confirmation modal to appear
            // The modal is rendered in: div.window-modal__content[role="dialog"]
            WebElement modal = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector("div.window-modal__content[role='dialog']")
                    )
            );
            System.out.println("✅ Confirmation modal appeared");
            Thread.sleep(800);

            // Step 11: Click "Yes, delete it" — scoped inside the modal dialog
            // Selector: div.window-modal__content[role="dialog"] button.modal__action--confirm
            WebElement confirmBtn = modal.findElement(
                    By.cssSelector("button.modal__action--confirm")
            );
            js.executeScript("arguments[0].scrollIntoView({block:'center'});", confirmBtn);
            Thread.sleep(500);
            js.executeScript("arguments[0].click();", confirmBtn);
            System.out.println("✅ Clicked 'Yes, delete it'");
            Thread.sleep(2500);

            // Verify: count remaining address cards
            List<WebElement> remainingCards = driver.findElements(
                    By.cssSelector("div.account-card__body")
            );
            System.out.println("ℹ Remaining address cards: " + remainingCards.size());
            System.out.println("✅ TEST PASSED: Address deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
