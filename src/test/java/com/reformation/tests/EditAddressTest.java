package com.reformation.tests;

import org.testng.annotations.Test;import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

public class EditAddressTest {

    @Test
    public void openWebsite() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {

            driver.manage().window().maximize();
            driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            Actions actions = new Actions(driver);

            // ── Handle Welcome Popup ─────────────────────────────
            try {
                WebElement shopNowBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector("button[data-modal-close='true']")
                        )
                );
                shopNowBtn.click();
                System.out.println("Popup handled");
            } catch (Exception e) {
                System.out.println("Popup not displayed");
            }

            // ── Hover Account ────────────────────────────────────
            WebElement accountBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.id("accountFlyoutTriggerDesktop")
                    )
            );

            js.executeScript("arguments[0].scrollIntoView({block:'center'});", accountBtn);
            actions.moveToElement(accountBtn).perform();

            js.executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles:true}));",
                    accountBtn
            );

            Thread.sleep(1200);

            // ── Click Sign In ────────────────────────────────────
            WebElement signInBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("#sign-in-button")
                    )
            );

            js.executeScript("arguments[0].click();", signInBtn);

            // ── Login ────────────────────────────────────────────
            WebElement emailField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("login-form-email")
                    )
            );
            emailField.sendKeys("refqaorders+paresh@gmail.com");

            WebElement passwordField = driver.findElement(
                    By.id("login-form-password")
            );
            passwordField.sendKeys("QA_Test123!");

            WebElement loginBtn = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("button[data-action='login']")
                    )
            );

            js.executeScript("arguments[0].click();", loginBtn);

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("account"),
                    ExpectedConditions.invisibilityOfElementLocated(
                            By.id("login-form-email")
                    )
            ));

            System.out.println("Login successful");
            Thread.sleep(3000);

            // ── Open My Account ─────────────────────────────────
            WebElement accountMenu = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("accountFlyoutTriggerDesktop")
                    )
            );

            actions.moveToElement(accountMenu).perform();
            Thread.sleep(500);

            WebElement myAccountLink = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.cssSelector("a[data-account-link][href*='account']")
                    )
            );

            js.executeScript("arguments[0].click();", myAccountLink);
            Thread.sleep(3000);

            // ── Open Address Book ───────────────────────────────
            WebElement addressBook = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath("//a[contains(@href,'addresses')]")
                    )
            );

            js.executeScript("arguments[0].click();", addressBook);
            Thread.sleep(3000);

            // ── Find Jade West Card ─────────────────────────────────────
List<WebElement> addressCards = driver.findElements(
    By.cssSelector("div.account-card__body")
);

WebElement jadeWestEditBtn = null;

for (WebElement card : addressCards) {
    if (card.getText().contains("Jade West")) {
        jadeWestEditBtn = card.findElement(
            By.cssSelector("a[aria-label*='Edit Address']")
        );
        break;
    }
}

if (jadeWestEditBtn == null) {
    throw new Exception("Could not find Jade West address card");
}

js.executeScript("arguments[0].scrollIntoView({block:'center'});", jadeWestEditBtn);
js.executeScript("arguments[0].click();", jadeWestEditBtn);
System.out.println("Clicked Edit on Jade West");

// ── Wait for modal to be fully visible ──────────────────────
WebElement modal = wait.until(
    ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector("div.window-modal__content")
    )
);

// ── Find address1 WITHIN the modal ──────────────────────────
WebElement address1 = wait.until(
    ExpectedConditions.visibilityOfElementLocated(
        By.cssSelector("div.window-modal__content #address1")
    )
);

System.out.println("Old value: " + address1.getAttribute("value"));

// ── Clear using JS then fire events ─────────────────────────
js.executeScript("arguments[0].value = '';", address1);
js.executeScript(
    "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));",
    address1
);

Thread.sleep(300);

// ── Type new value ───────────────────────────────────────────
address1.click();
address1.sendKeys("6925 Hollywood Blvd");

// ── Fire change event ────────────────────────────────────────
js.executeScript(
    "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));",
    address1
);

System.out.println("New value: " + address1.getAttribute("value"));
Thread.sleep(500);

// ── Click Save button directly (not form.submit()) ──────────
WebElement saveBtn = modal.findElement(
    By.cssSelector("button[type='submit'][name='save']")
);

js.executeScript("arguments[0].scrollIntoView({block:'center'});", saveBtn);
Thread.sleep(300);
saveBtn.click();
System.out.println("Clicked Save");

// ── Wait for modal to close (confirms success) ───────────────
wait.until(
    ExpectedConditions.invisibilityOfElementLocated(
        By.cssSelector("div.window-modal__content")
    )
);

System.out.println("Address updated successfully!");
Thread.sleep(2000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}