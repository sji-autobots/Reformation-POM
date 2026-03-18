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

public class CheckoutAllTest extends com.reformation.base.BaseTest {

    private void loginAndSetup(WebDriverWait wait) throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.login("refqaorders+paresh@gmail.com", "QA_Test123!");
        System.out.println("✅ Signed in successfully");
        CountrySwitcher.switchToUnitedStates(driver, wait);
    }

    private void addStandardItemToCart() throws InterruptedException {
        PDPPage pdp = new PDPPage(driver);
        pdp.loadPDPWithAuth("1312627BLK0XS");
        pdp.selectColor("Black");
        pdp.selectSize("XS");
        pdp.clickAddToBag();
        Thread.sleep(5000);
        pdp.clickCheckout();
    }

    @Test
    public void testCreditCardCheckout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        loginAndSetup(wait);
        addStandardItemToCart();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickPaymentButtonWithRetry();
        // Credit Card payment flow
        checkoutPage.verifyCardSelectedContains("Visa");
        checkoutPage.proceedToReview();
// need to add step to enter confirmation code

        checkoutPage.placeOrder();
        WebDriverWait confirmWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        confirmWait.until(ExpectedConditions.urlContains("https://stage.thereformation.com/"));
        String orderId = checkoutPage.getOrderId();
        System.out.println("Order ID: " + orderId + " | Order Type: Normal US Credit Card");
    }

    @Test
    public void testPayPalCheckout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        loginAndSetup(wait);
        addStandardItemToCart();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickPaymentButtonWithRetry();
        // PayPal payment flow
        String paypalEmail = "mohittest70@gmail.com";
        String paypalPassword = "Pass@123";
        checkoutPage.completePaypalFlow(paypalEmail, paypalPassword);
        WebDriverWait confirmWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        confirmWait.until(ExpectedConditions.urlContains("https://stage.thereformation.com/"));
        String orderId = checkoutPage.getOrderId();
        System.out.println("Order ID: " + orderId + " | Order Type: Normal US PayPal");
    }

    @Test
    public void testEGCCheckout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        loginAndSetup(wait);
        addStandardItemToCart();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickPaymentButtonWithRetry();
        // EGC payment flow
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        By removeGiftCardBtn = By.cssSelector("button.checkout-giftcard__remove-action");
        int maxAttempts = 10;
        int attempt = 0;
        while (attempt < maxAttempts) {
            java.util.List<WebElement> removeBtns = driver.findElements(removeGiftCardBtn);
            System.out.println("[DEBUG] Attempt " + (attempt+1) + ": Found " + removeBtns.size() + " remove button(s)");
            if (removeBtns.isEmpty()) {
                System.out.println("[DEBUG] No more remove buttons found. Exiting loop.");
                break;
            }
            try {
                WebElement btn = driver.findElements(removeGiftCardBtn).get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", btn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                System.out.println("[DEBUG] Clicked remove button.");
                Thread.sleep(500);
            } catch (StaleElementReferenceException e) {
                System.out.println("[DEBUG] StaleElementReferenceException caught. Retrying...");
                Thread.sleep(200);
            }
            attempt++;
        }
        if (attempt == maxAttempts) {
            throw new RuntimeException("[DEBUG] Exceeded max attempts to remove gift cards. Possible infinite loop or page issue.");
        }
        WebElement giftCardTab = shortWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='Gift card']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", giftCardTab);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", giftCardTab);
        Thread.sleep(500);
        String egc_code = "VHPVJWVPRDDVYAWZ";
        WebElement egcInput = shortWait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='giftCard']")));
        egcInput.clear();
        egcInput.click();
        Thread.sleep(200);
        egcInput.click();
        Thread.sleep(200);
        egcInput.sendKeys(egc_code);
        WebElement applyBtn = shortWait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='checkout-giftcard-button button button--primary-outline']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", applyBtn);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", applyBtn);
        Thread.sleep(1000);
        java.util.List<WebElement> removeBtnsAfter = driver
                .findElements(By.cssSelector("button.checkout-giftcard__remove-action"));
        if (removeBtnsAfter.size() != 1) {
            throw new AssertionError("Expected exactly one 'remove' button after applying gift card, but found: "
                    + removeBtnsAfter.size());
        }
        System.out.println("✅ Only one 'remove' button is present after applying gift card");
        checkoutPage.proceedToReview();
        checkoutPage.placeOrder();
        WebDriverWait confirmWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        confirmWait.until(ExpectedConditions.urlContains("https://stage.thereformation.com/"));
        String orderId = checkoutPage.getOrderId();
        System.out.println("Order ID: " + orderId + " | Order Type: Normal US EGC");
    }

    @Test
    public void testAfterPayCheckout() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        loginAndSetup(wait);
        addStandardItemToCart();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.clickPaymentButtonWithRetry();
        // Afterpay payment flow
        String afterpayEmail = "mohittest70@gmail.com";
        String afterpayPassword = "Pass@123";
        checkoutPage.completeAfterpayFlow(afterpayEmail, afterpayPassword);
        WebDriverWait afterpayWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        afterpayWait.until(ExpectedConditions.urlContains("https://stage.thereformation.com/"));
        String orderId = checkoutPage.getOrderId();
        System.out.println("Order ID: " + orderId + " | Order Type: Normal US Afterpay");
    }
}
