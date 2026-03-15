package com.reformation.tests;

import org.testng.annotations.Test;import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class DefaultAddressTest {

    @Test
    public void openWebsite() {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {

            driver.manage().window().maximize();
//New address marked as default
            // Bypass staging authentication
            driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

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

            /// Locate Account Button (DESKTOP)
WebElement accountBtn = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.id("accountFlyoutTriggerDesktop")
        )
);

// Scroll into view
((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});", accountBtn
);

// Hover using Actions
Actions actions = new Actions(driver);
actions.moveToElement(accountBtn).perform();

// Fire real mouse event (important)
((JavascriptExecutor) driver).executeScript(
        "var evObj = document.createEvent('MouseEvents');" +
        "evObj.initEvent('mouseover', true, false);" +
        "arguments[0].dispatchEvent(evObj);", accountBtn
);

Thread.sleep(1500);

System.out.println("Account menu triggered");

Thread.sleep(1200);

// Give UI a moment
Thread.sleep(800);

// Direct JS click using EXACT element structure
WebElement signInBtn = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("#sign-in-button")
        )
);

// Force click via JavaScript (ignores visibility/interception)
((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", signInBtn
);

System.out.println("✅ Sign In clicked");
// Wait for Email Field
WebElement emailField = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
                By.id("login-form-email")
        )
);

emailField.click();
emailField.clear();
emailField.sendKeys("refqaorders+paresh@gmail.com");

System.out.println("Email entered");


// Wait for Password Field
WebElement passwordField = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
                By.id("login-form-password")
        )
);

passwordField.click();
passwordField.clear();
passwordField.sendKeys("QA_Test123!");

System.out.println("Password entered");

// Small wait for button state update (React validation)
Thread.sleep(800);

// Click SIGN IN inside Login Popup
WebElement loginBtn = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button[data-action='login']")
        )
);

// Use JS click (most reliable for modals)
((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", loginBtn
);

System.out.println("Login button clicked");

            // Wait for login success (URL change OR account element visible)
wait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("account"),
        ExpectedConditions.invisibilityOfElementLocated(By.id("login-form-email"))
));

System.out.println("✅ Login successful!");

// Keep browser open for verification
Thread.sleep(5000);

// Re-hover Account (keep this)
WebElement accountMenu = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
                By.id("accountFlyoutTriggerDesktop")
        )
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});", accountMenu
);

actions.moveToElement(accountMenu)
       .pause(Duration.ofSeconds(1))
       .perform();

System.out.println("Account hovered");

// Locate My Account LINK (anchor, not span)
WebElement myAccountLink = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("a[data-account-link][href*='account']")
        )
);

// Stability lock – keeps dropdown active
actions.moveToElement(accountMenu).perform();
Thread.sleep(300);

// 🔥 Synthetic click (most reliable for React menus)
((JavascriptExecutor) driver).executeScript(
        "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles:true}));",
        myAccountLink
);

System.out.println("✅ My Account clicked");

Thread.sleep(5000);

System.out.println("✅ My Account page loaded");

// Click Address Book
WebElement addressBook = wait.until(
        ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href,'addresses')]")
        )
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", addressBook
);

System.out.println("Address Book opened");

// Wait for Address Book page to fully stabilize
wait.until(ExpectedConditions.urlContains("addresses"));

System.out.println("Address Book page loaded");

// Locate Add Address button (best locator = class)
WebElement addAddressBtn = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.add-address")
        )
);

// Scroll into view (Reformation layout sensitive)
((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});", addAddressBtn
);

// Tiny UI settle delay (critical for React)
Thread.sleep(600);

// Force click via JS (more reliable than Selenium click)
((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", addAddressBtn
);

System.out.println("Add Address clicked");

Thread.sleep(5000);

wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("dwfrm_address")));
System.out.println("Address modal fully loaded");

driver.findElement(By.id("firstName")).sendKeys("Jade");
driver.findElement(By.id("lastName")).sendKeys("West");

driver.findElement(By.id("address1"))
        .sendKeys("Two Rodeo, Wilshire Blvd");

driver.findElement(By.id("address2")).sendKeys("");

driver.findElement(By.id("city")).sendKeys("Beverly Hills");

Select state = new Select(driver.findElement(By.id("state")));
state.selectByValue("NY");     // MOST STABLE
System.out.println("State selected");

driver.findElement(By.id("zipCode")).sendKeys("90210");

driver.findElement(By.id("phone")).sendKeys("(209) 300-2557");

System.out.println("Address fields entered");


By defaultCheckbox = By.id("dwfrm_address_makeDefaultAddressChk-add");

WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(defaultCheckbox));

((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", checkbox);
Thread.sleep(300); // tiny UI stabilization

if (!checkbox.isSelected()) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
}

By saveLocator = By.cssSelector("button.modal__action--confirm");
// Wait longer + use presence instead of clickable
WebElement saveBtn = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button[name='save']")
        )
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});", saveBtn
);

Thread.sleep(1000);

System.out.println("Save button disabled: " + saveBtn.getAttribute("disabled"));
System.out.println("Save button class: " + saveBtn.getAttribute("class"));

// Force click via JS
((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", saveBtn
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", saveBtn
);
System.out.println("✅ Save clicked (1st time)");

Thread.sleep(2000); // Wait for validation message to appear

// Click Save again to confirm
WebElement saveBtnAgain = wait.until(
        ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button[name='save']")
        )
);

((JavascriptExecutor) driver).executeScript(
        "arguments[0].click();", saveBtnAgain
);
System.out.println("✅ Save clicked (2nd time)");

Thread.sleep(5000); // Wait for address to be saved


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
