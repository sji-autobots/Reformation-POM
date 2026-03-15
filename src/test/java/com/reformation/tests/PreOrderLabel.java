// https://thereformation.atlassian.net/browse/ENG-42613
//Pre-order label: Copy: Pre-order expected to ship by [date] and removed header
package com.reformation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.reformation.base.BaseTest;



public class PreOrderLabel extends BaseTest {

    @org.testng.annotations.Test
    public void testPreOrderLabel() throws InterruptedException {
        // Open stage site with Basic Auth
        driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        Thread.sleep(5000);

        // Close "Shop now" popup if present
        try {
            By shopNowBtn = By.xpath("//button[@data-modal-close='true']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(shopNowBtn));
            WebElement btn = driver.findElement(shopNowBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
            System.out.println("Shop now popup closed");
        } catch (Exception e) {
            System.out.println("Popup not displayed");
        }

        // Step 1: Hover over Clothing menu
        try {
            WebElement clothingMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//nav//a[contains(translate(text()," +
                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'clothing')]")));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));",
                    clothingMenu);

            Thread.sleep(800);
            System.out.println("Hovered over Clothing menu");
        } catch (InterruptedException e) {
            System.out.println("Could not hover over Clothing menu: " + e.getMessage());
            return;
        }

        // Step 2: Click Bestsellers under Clothing
        try {
            WebElement bestSellers = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//nav//a[contains(translate(text()," +
                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bestsellers')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bestSellers);
            System.out.println("Clicked Bestsellers under Clothing");
        } catch (Exception e) {
            System.out.println("Could not click Bestsellers: " + e.getMessage());
            return;
        }

        // Step 3: Hover over a product card and click
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".product-tile, [data-testid='product-tile'], .product-card")));

            WebElement firstProduct = driver.findElements(
                    By.cssSelector(".product-tile, [data-testid='product-tile'], .product-card")).get(0);

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));",
                    firstProduct);

            Thread.sleep(600);

            WebElement productLink = firstProduct.findElement(By.cssSelector("a"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", productLink);

            System.out.println("Clicked on first product");
        } catch (InterruptedException e) {
            System.out.println("Could not click product: " + e.getMessage());
            return;
        }

        // Step 4: Wait for PDP to load
        try {
            wait.until(ExpectedConditions.urlContains("/products/"));
            Thread.sleep(1500);
            System.out.println("Product page loaded");
        } catch (InterruptedException e) {
            System.out.println("Product page did not load: " + e.getMessage());
            return;
        }

        // Step 5: Click "Select a size" dropdown trigger
        // Exact locator from HTML: label[data-sizepicker-trigger]
        try {
            WebElement sizeDropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[@data-sizepicker-trigger]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sizeDropdownTrigger);
            Thread.sleep(800);
            System.out.println("Size dropdown opened");
        } catch (InterruptedException e) {
            System.out.println("Could not open size dropdown: " + e.getMessage());
            return;
        }

        // Step 6: Select first available size
        // Exact locator from HTML: button[data-attr='size'][data-attr-selectable='true']
        try {
            WebElement firstSize = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@data-attr='size' and @data-attr-selectable='true'])[1]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", firstSize);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstSize);

            Thread.sleep(500);
            System.out.println("Size selected: " + firstSize.getAttribute("aria-label"));
        } catch (InterruptedException e) {
            System.out.println("Could not select size: " + e.getMessage());
            return;
        }

        // Step 7: Click Add to Bag
        try {
            WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Add')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCart);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);

            System.out.println("Add to Bag clicked");
        } catch (Exception e) {
            System.out.println("Could not click Add to Bag: " + e.getMessage());
            return;
        }

        // Step 8: Verify Pre-order label copy
        try {
            WebElement preOrderLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(translate(text()," +
                            "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                            "'pre-order expected to ship by')]")));

            String labelText = preOrderLabel.getText().trim();
            System.out.println("Pre-order label found: " + labelText);

            if (labelText.toLowerCase().contains("pre-order expected to ship by")) {
                System.out.println("PASS - Pre-order label copy is correct");
            } else {
                System.out.println("FAIL - Pre-order label copy mismatch. Actual: " + labelText);
            }

            String[] parts = labelText.toLowerCase().split("ship by");
            if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                System.out.println("PASS - Date present in label: " + parts[1].trim());
            } else {
                System.out.println("FAIL - Date missing from pre-order label");
            }

        } catch (Exception e) {
            System.out.println("FAIL - Pre-order label not found: " + e.getMessage());
        }

        // Step 8b: Verify pre-order header is removed
        try {
            java.util.List<WebElement> preOrderHeaders = driver.findElements(
                    By.xpath("//*[contains(@class,'pre-order-header') or " +
                                 "contains(@class,'preorder-header') or " +
                                 "contains(@data-testid,'preorder-header')]"));

            if (preOrderHeaders.isEmpty()) {
                System.out.println("PASS - Pre-order header is correctly removed");
            } else {
                System.out.println("FAIL - Pre-order header is still present on the page");
            }
        } catch (Exception e) {
            System.out.println("Error checking pre-order header: " + e.getMessage());
        }

    }
}