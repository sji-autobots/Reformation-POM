package com.reformation.tests;

import org.testng.annotations.Test;import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

public class UpdatePasswordTest {

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

WebElement passwordLink = wait.until(
        ExpectedConditions.elementToBeClickable(
                By.cssSelector("a.account-navigation--password")
        )
);

passwordLink.click();

System.out.println("Password link clicked");

WebElement currentPwd = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
                By.id("currentPassword")
        )
);

currentPwd.clear();
currentPwd.sendKeys("QA_Test123!");

WebElement newPwd = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
                By.name("dwfrm_profile_login_newpasswords_newpassword")
        )
);

newPwd.clear();
newPwd.sendKeys("QA_Test456!");

WebElement confirmPwd = driver.findElement(
        By.id("newPasswordConfirm")
);

confirmPwd.clear();
confirmPwd.sendKeys("QA_Test456!");

WebElement saveBtn = wait.until(
        ExpectedConditions.elementToBeClickable(
                By.name("save")
        )
);

saveBtn.click();

System.out.println("✅ Save button clicked");

Thread.sleep(5000); // Wait for password to be saved


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
