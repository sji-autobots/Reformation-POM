package com.reformation.tests;

import com.reformation.pages.CartPage;
import com.reformation.pages.CheckoutPage;
import com.reformation.pages.LoginPage;
import com.reformation.pages.PDPPage;
import com.reformation.utils.CountrySwitcher;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.time.Duration;

public class AfterPayCheckoutSignedInTest extends com.reformation.base.BaseTest {

    @Test
    public void testCreditCardCheckoutSignedIn() throws InterruptedException {
        // Login using LoginPage logic
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.login("refqaorders+paresh@gmail.com", "QA_Test123!");
        System.out.println("✅ Signed in successfully");

        // Change location to United States using utility
        CountrySwitcher.switchToUnitedStates(driver, wait);

        // Add Standard item to bag (SKU: 1312627BLK0XS)
        PDPPage pdp = new PDPPage(driver);
        pdp.loadPDPWithAuth("1312627BLK0XS");
        pdp.selectColor("Black");
        pdp.selectSize("XS");
        pdp.clickAddToBag();
        Thread.sleep(5000);

        // Click checkout directly from PDPPage (cart loads automatically)
        pdp.clickCheckout();
        // driver.get("https://stage.thereformation.com/checkout-begin?stage=shipping");

        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickPaymentButtonWithRetry();

        // Afterpay payment flow (mirroring CheckoutOrdersTest logic)
        String afterpayEmail = "mohittest70@gmail.com";
        String afterpayPassword = "Pass@123";
        checkoutPage.completeAfterpayFlow(afterpayEmail, afterpayPassword);
        WebDriverWait afterpayWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        afterpayWait.until(ExpectedConditions.urlContains("https://stage.thereformation.com/"));
        String orderId = checkoutPage.getOrderId();
        System.out.println("Order ID: " + orderId + " | Order Type: Normal US Afterpay");

        // // Remove all gift cards if present
        // WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // By removeGiftCardBtn = By.cssSelector("button.checkout-giftcard__remove-action");
        // int maxAttempts = 10;
        // int attempt = 0;
        // while (attempt < maxAttempts) {
        //     java.util.List<WebElement> removeBtns = driver.findElements(removeGiftCardBtn);
        //     System.out.println("[DEBUG] Attempt " + (attempt+1) + ": Found " + removeBtns.size() + " remove button(s)");
        //     if (removeBtns.isEmpty()) {
        //         System.out.println("[DEBUG] No more remove buttons found. Exiting loop.");
        //         break;
        //     }
        //     try {
        //         WebElement btn = driver.findElements(removeGiftCardBtn).get(0);
        //         ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
        //         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        //         System.out.println("[DEBUG] Clicked remove button.");
        //         Thread.sleep(500);
        //     } catch (StaleElementReferenceException e) {
        //         System.out.println("[DEBUG] StaleElementReferenceException caught. Retrying...");
        //         // Optionally, add a small sleep to avoid tight loop
        //         Thread.sleep(200);
        //     }
        //     attempt++;
        // }
        // if (attempt == maxAttempts) {
        //     throw new RuntimeException("[DEBUG] Exceeded max attempts to remove gift cards. Possible infinite loop or page issue.");
        // }

        // // Click on Gift card tab
        // WebElement giftCardTab = shortWait
        //         .until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Gift card']")));
        // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", giftCardTab);
        // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", giftCardTab);
        // Thread.sleep(500);

        // // Clear and focus input field, then enter egc_code
        // String egc_code = "VHPVJWVPRDDVYAWZ";
        // WebElement egcInput = shortWait
        //         .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='giftCard']")));
        // egcInput.clear();
        // egcInput.click();
        // Thread.sleep(200);
        // egcInput.click();
        // Thread.sleep(200);
        // egcInput.sendKeys(egc_code);

        // // Click CTA with text 'Apply to order'
        // WebElement applyBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
        //         By.xpath("//button[@class='checkout-giftcard-button button button--primary-outline']")));
        // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", applyBtn);
        // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyBtn);
        // Thread.sleep(1000);

        // // Verify only one instance of removeBtns is present
        // java.util.List<WebElement> removeBtnsAfter = driver
        //         .findElements(By.cssSelector("button.checkout-giftcard__remove-action"));
        // if (removeBtnsAfter.size() != 1) {
        //     throw new AssertionError("Expected exactly one 'remove' button after applying gift card, but found: "
        //             + removeBtnsAfter.size());
        // }
        // System.out.println("✅ Only one 'remove' button is present after applying gift card");

        // PayPal payment flow

        // String paypalEmail = "your-paypal-email@example.com";
        // String paypalPassword = "your-paypal-password";
        // checkoutPage.completePaypalFlow(paypalEmail, paypalPassword);

        // checkoutPage.placeOrder();

        // --- Payment workflow will be handled next ---
        // Steps:
        // - Choose Credit Card Payment method
        // - Proceed to checkout workflow
        // - Click Review button
        // - Click Place Order button
    }
}
