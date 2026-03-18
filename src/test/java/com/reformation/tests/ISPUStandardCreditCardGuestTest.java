package com.reformation.tests;

import com.reformation.pages.PDPPage;
import com.reformation.pages.CartPage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

public class ISPUStandardCreditCardGuestTest extends com.reformation.base.BaseTest {
    @Test
    public void testISPUStandardCreditCardGuestAccountSignupPrompt() throws InterruptedException {
        // Open PDP page for ISPU SKU
        PDPPage pdp = new PDPPage(driver);
        pdp.openISPU();

        Thread.sleep(5000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Change location to United States using logic from LocalizedSizingPDPTest
        try {
            // Open Account menu (click, not hover)
            WebElement accountMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("accountFlyoutTriggerDesktop")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountMenu);
            Thread.sleep(1000);
            System.out.println("Clicked Account menu - flyout opened");

            // Click the country link (ShippingSwitcher)
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[@data-action='ShippingSwitcher']")));
            WebElement countryLink = driver.findElement(
                    By.xpath("//span[@data-action='ShippingSwitcher']"));
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));",
                    countryLink);
            Thread.sleep(1500);
            System.out.println("Clicked country link (ShippingSwitcher)");

            // Wait for country popup
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("gle_selectedCountry")));
            System.out.println("Ship my stuff here popup appeared");

            // Select United States from country dropdown
            WebElement countryDropdown = driver.findElement(By.id("gle_selectedCountry"));
            org.openqa.selenium.support.ui.Select countrySelect = new org.openqa.selenium.support.ui.Select(
                    countryDropdown);
            countrySelect.selectByValue("US");
            Thread.sleep(1500);
            System.out.println("Selected United States from country dropdown");

            // Click the Save button
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@value='Save']")));
            saveBtn.click();
            System.out.println("Clicked Save after selecting United States");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println(
                    "Country selection popup not displayed or could not select United States: " + e.getMessage());
        }

        WebElement BlackSwatch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//button[@title='Black']")));
        BlackSwatch.click();
        WebElement ivorySwatch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "//button[@title='Ivory']")));
        ivorySwatch.click();
        WebElement pickItUpBtn = driver.findElement(By.cssSelector(
                ".pdp__ispu_toggle-button"));
        // Scroll into view for reliability
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", pickItUpBtn);

        Thread.sleep(4000); // Small pause to ensure stability

        pdp.clickPickItUp();

        // Search for zipcode and add ISPU to bag
        try {
            pdp.enterPostalCodeAndFindStores("90001");
            pdp.selectStoreAndSave("Beverly Hills");
            pdp.clickAddToBag();
            Thread.sleep(10000);
        } catch (Exception e) {
            System.out.println("Popup not displayed");
            Assert.assertTrue(pdp.isChangeLocationDisplayed(), "Change Location CTA should be displayed");
            pdp.clickAddToBag();
            Thread.sleep(10000);
        }

        // Add Standard item to bag (SKU: 1312627BLK0XS)
        pdp.loadPDPWithAuth("1312627BLK0XS");
        pdp.selectColor("Black");
        pdp.selectSize("XS");
        pdp.clickAddToBag();
        Thread.sleep(5000);

        // Click checkout directly from PDPPage (cart loads automatically)
        pdp.clickCheckout();

        // Begin guest checkout flow
        com.reformation.pages.CheckoutPage checkoutPage = new com.reformation.pages.CheckoutPage(driver);
        checkoutPage.enterGuestEmail("refqaorders+guestest@gmail.com");
        checkoutPage.clickContinueAsGuest();

        // Fill shipping address fields using page object
        checkoutPage.fillShippingAddress("SJI", "Test", "9993339933", "Queens Way IL");

        // Payment and order flow
        checkoutPage.clickPaymentButtonWithRetry();
        Thread.sleep(10000);
        checkoutPage.enterCreditCardNumber("41111111111111111131901");
        checkoutPage.verifyDeliveryAddressContains("US");
        checkoutPage.verifyCardSelectedContains("Visa");
        checkoutPage.proceedToReview();
        checkoutPage.placeOrder();
        // Assert account signup section and password fields are present using page object
        checkoutPage.verifyAccountSignupSection();
        checkoutPage.verifyAccountSignupSection();
    }
        // ...existing code...
}
