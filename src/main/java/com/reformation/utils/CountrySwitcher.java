package com.reformation.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CountrySwitcher {
    /**
     * Changes the site country to United States using the ShippingSwitcher logic.
     * @param driver WebDriver instance
     * @param wait WebDriverWait instance
     */
    public static void switchToUnitedStates(WebDriver driver, WebDriverWait wait) throws InterruptedException {
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
    }
}
