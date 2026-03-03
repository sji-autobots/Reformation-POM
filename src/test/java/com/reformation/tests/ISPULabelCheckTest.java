package com.reformation.tests;

import static org.apache.commons.lang3.ObjectUtils.wait;

import java.time.Duration;

import com.reformation.pages.PDPPage;
import com.reformation.pages.CartPage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ISPULabelCheckTest extends BaseTest {

    @Test 
    public void testISPULabel() throws InterruptedException {
        // Go to PDP page (assume direct navigation for this test)
        PDPPage pdp = new PDPPage(driver);
        pdp.openISPU();

        Thread.sleep(5000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Handle country selection if welcome-change-country is present
        try {
            By changeCountryLink = By.xpath("//a[@class='welcome-change-country']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(changeCountryLink));
            WebElement countryLink = driver.findElement(changeCountryLink);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", countryLink);

            By countrySelect = By.xpath("//select[@id='gle_selectedCountry']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(countrySelect));
            WebElement selectElement = driver.findElement(countrySelect);
            org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(selectElement);
            select.selectByVisibleText("United States");

            By saveBtn = By.xpath("//input[@value='Save']");
            wait.until(ExpectedConditions.elementToBeClickable(saveBtn));
            WebElement saveButton = driver.findElement(saveBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
            System.out.println("Country changed to United States");
        } catch (Exception e) {
            System.out.println("Country selection popup not displayed");
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
        try {
            pdp.enterPostalCodeAndFindStores("90001");
            pdp.selectStoreAndSave("La Jolla - San Diego, CA");
        } catch (Exception e) {
            System.out.println("Popup not displayed");
            Assert.assertTrue(pdp.isChangeLocationDisplayed(), "Change Location CTA should be displayed");
            pdp.clickAddToBag();

            Thread.sleep(5000); // Wait for cart update

            CartPage cart = new CartPage(driver);
            String pickupLabel = cart.getPickupText();
            Assert.assertTrue(pickupLabel.contains("Pick up at La Jolla - San Diego, CA"),
                    "Pickup label should contain correct store name");
        }
    }
}
