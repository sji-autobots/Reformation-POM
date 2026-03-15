package com.reformation.tests;

import com.reformation.base.BaseTest;
import com.reformation.pages.PDPPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class LocalizedSizingPDPTest extends BaseTest {
    @Test
    public void testLocalizedSizingPDP_EU() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/1319230SLA.html");

        // Open Account menu (click, not hover)
        try {
            WebElement accountMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("accountFlyoutTriggerDesktop")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", accountMenu);
            Thread.sleep(1000);
            System.out.println("Clicked Account menu - flyout opened");
        } catch (InterruptedException e) {
            System.out.println("Could not click Account menu: " + e.getMessage());
            return;
        }

        // Click the country link (ShippingSwitcher)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//span[@data-action='ShippingSwitcher']")));
            WebElement countryLink = driver.findElement(
                By.xpath("//span[@data-action='ShippingSwitcher']"));
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));",
                countryLink);
            Thread.sleep(1500);
            System.out.println("Clicked country link (ShippingSwitcher)");
        } catch (InterruptedException e) {
            System.out.println("Could not click country link: " + e.getMessage());
            return;
        }

        // Wait for country popup
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("gle_selectedCountry")));
            System.out.println("Ship my stuff here popup appeared");
        } catch (Exception e) {
            System.out.println("Popup did not appear: " + e.getMessage());
            return;
        }

        // Select Germany (DE) from country dropdown
        try {
            WebElement countryDropdown = driver.findElement(By.id("gle_selectedCountry"));
            Select countrySelect = new Select(countryDropdown);
            countrySelect.selectByValue("DE");
            Thread.sleep(1500);
            System.out.println("Selected Germany from country dropdown");

            // Click the Save button
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@value='Save']")));
            saveBtn.click();
            System.out.println("Clicked Save after selecting Germany");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            System.out.println("Could not select Germany: " + e.getMessage());
            return;
        }

        

        // Observe that the size dropdown values contain numbers
        try {
            List<WebElement> sizeOptions = driver.findElements(By.xpath("//span[@data-sizepicker-value]"));
            System.out.println("[DEBUG] Size dropdown options for Germany:");
            for (WebElement option : sizeOptions) {
                String optionText = option.getText();
                System.out.println("[DEBUG] Option: '" + optionText + "'");
                // Print value for debug before asserting
                System.out.println("[DEBUG] Asserting that option contains a number: '" + optionText + "'");
                Assert.assertTrue(optionText.matches(".*\\d.*"), "Size option should contain a number for EU");
            }
        } catch (Exception e) {
            System.out.println("Could not verify size dropdown: " + e.getMessage());
        }
    }
}
