// Global E pricing (CA)
// https://thereformation.atlassian.net/browse/ENG-42068
package com.reformation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.reformation.base.BaseTest;



public class CountryCurrencyTest extends BaseTest {

    public static void main(String[] args) throws InterruptedException {

        CountryCurrencyTest test = new CountryCurrencyTest();

        // Step 1: Open stage site with Basic Auth
        test.driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");

        WebDriverWait wait = new WebDriverWait(test.driver, Duration.ofSeconds(25));

        Thread.sleep(5000);

        // Close "Shop now" popup if present
        try {
            By shopNowBtn = By.xpath("//button[@data-modal-close='true']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(shopNowBtn));
            WebElement btn = test.driver.findElement(shopNowBtn);
            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", btn);
            System.out.println("Shop now popup closed");
        } catch (Exception e) {
            System.out.println("Popup not displayed");
        }

        // Step 2: Click Account button to open flyout
        // Click instead of hover so flyout stays open
        try {
            WebElement accountMenu = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("accountFlyoutTriggerDesktop")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", accountMenu);

            Thread.sleep(1000);
            System.out.println("Clicked Account menu - flyout opened");
        } catch (InterruptedException e) {
            System.out.println("Could not click Account menu: " + e.getMessage());
            
            return;
        }

        // Step 3: Click the country link (India)
        // Force click via JS dispatchEvent - bypasses clickable check
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[@data-action='ShippingSwitcher']")));

            WebElement countryLink = test.driver.findElement(
                    By.xpath("//span[@data-action='ShippingSwitcher']"));

            ((JavascriptExecutor) test.driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {bubbles: true, cancelable: true}));",
                    countryLink);

            Thread.sleep(1500);
            System.out.println("Clicked country link (India)");
        } catch (InterruptedException e) {
            System.out.println("Could not click country link: " + e.getMessage());
            
            return;
        }

        // Step 4: Wait for "Ship my stuff here" popup
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("gle_selectedCountry")));
            System.out.println("Ship my stuff here popup appeared");
        } catch (Exception e) {
            System.out.println("Popup did not appear: " + e.getMessage());
            
            return;
        }

        // Step 5: Select United Kingdom from Country dropdown
        //  id="gle_selectedCountry", value="GB"
        try {
            WebElement countryDropdown = test.driver.findElement(By.id("gle_selectedCountry"));
            Select countrySelect = new Select(countryDropdown);
            countrySelect.selectByValue("GB");

            Thread.sleep(1500);
            System.out.println("Selected United Kingdom from country dropdown");
        } catch (InterruptedException e) {
            System.out.println("Could not select United Kingdom: " + e.getMessage());
            
            return;
        }

        // Step 6: Verify currency auto-updated to British Pound
        try {
            WebElement currencyDropdown = test.driver.findElement(
                    By.xpath("//select[contains(@id,'urrency')]"));
            Select currencySelect = new Select(currencyDropdown);
            String selectedCurrency = currencySelect.getFirstSelectedOption().getText();
            System.out.println("Currency auto-selected: " + selectedCurrency);

            if (selectedCurrency.toLowerCase().contains("pound") ||
                selectedCurrency.toLowerCase().contains("gbp")) {
                System.out.println("PASS - Currency correctly auto-updated to British Pound");
            } else {
                System.out.println("INFO - Currency shows: " + selectedCurrency);
            }
        } catch (Exception e) {
            System.out.println("Could not verify currency: " + e.getMessage());
        }

        // Step 7: Click Save button
        // Save is an <input> not <button>, use data-action='SaveAndClose'
        try {
            WebElement saveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@data-action='SaveAndClose']")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", saveBtn);

            Thread.sleep(2000);
            System.out.println("Clicked Save button");
        } catch (InterruptedException e) {
            System.out.println("Could not click Save: " + e.getMessage());
            
            return;
        }

        // Step 8: Hover Clothing -> Click Bestsellers
        try {
            WebElement clothingMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//nav//a[contains(translate(text()," +
                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'clothing')]")));

            ((JavascriptExecutor) test.driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));",
                    clothingMenu);

            Thread.sleep(800);

            WebElement bestSellers = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//nav//a[contains(translate(text()," +
                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bestsellers')]")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", bestSellers);
            Thread.sleep(1500);
            System.out.println("Navigated to Bestsellers");
        } catch (InterruptedException e) {
            System.out.println("Could not navigate to Bestsellers: " + e.getMessage());
            
            return;
        }

        // Step 9: Click first product
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".product-tile, [data-testid='product-tile'], .product-card")));

            WebElement firstProduct = test.driver.findElements(
                    By.cssSelector(".product-tile, [data-testid='product-tile'], .product-card")).get(0);

            ((JavascriptExecutor) test.driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));",
                    firstProduct);

            Thread.sleep(600);

            WebElement productLink = firstProduct.findElement(By.cssSelector("a"));
            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", productLink);

            wait.until(ExpectedConditions.urlContains("/products/"));
            Thread.sleep(1500);
            System.out.println("Opened product page");
        } catch (InterruptedException e) {
            System.out.println("Could not open product: " + e.getMessage());
            
            return;
        }

        // Step 10: Open size dropdown and select first size
        try {
            WebElement sizeDropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[@data-sizepicker-trigger]")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", sizeDropdownTrigger);
            Thread.sleep(800);

            WebElement firstSize = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@data-attr='size' and @data-attr-selectable='true'])[1]")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].scrollIntoView(true);", firstSize);
            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", firstSize);

            Thread.sleep(500);
            System.out.println("Size selected: " + firstSize.getAttribute("aria-label"));
        } catch (InterruptedException e) {
            System.out.println("Could not select size: " + e.getMessage());
            
            return;
        }

        // Step 11: Click Add to Bag
        try {
            WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Add')]")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].scrollIntoView(true);", addToCart);
            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", addToCart);

            Thread.sleep(1500);
            System.out.println("Add to Bag clicked");
        } catch (InterruptedException e) {
            System.out.println("Could not click Add to Bag: " + e.getMessage());
            
            return;
        }

        // Step 12: Open Bag page
        try {
            WebElement bagIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(@href,'cart')]")));

            ((JavascriptExecutor) test.driver).executeScript("arguments[0].click();", bagIcon);

            Thread.sleep(2000);
            System.out.println("Opened Bag page");
        } catch (InterruptedException e) {
            System.out.println("Could not open Bag: " + e.getMessage());
            
            return;
        }

        // Step 13: Verify currency on Bag page is British Pound
        try {
            String bagPageSource = test.driver.getPageSource();

            if (bagPageSource.contains("£") || bagPageSource.contains("GBP")) {
                System.out.println("PASS - Currency on Bag page is British Pound (£)");
            } else {
                System.out.println("FAIL - British Pound currency not found on Bag page");
            }

            System.out.println("Current page URL: " + test.driver.getCurrentUrl());

        } catch (Exception e) {
            System.out.println("Could not verify currency on Bag page: " + e.getMessage());
        }

        Thread.sleep(3000);
        
    }
}