// Cart- deduct units
//https://thereformation.atlassian.net/browse/ENG-42066
package com.reformation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;



public class DeductUnitsTest extends BaseTest {

    @org.testng.annotations.Test
    public void testDeductUnits() throws InterruptedException {
        // Step 1: Open stage site with Basic Auth
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

        // Step 2: Hover Clothing -> Click Bestsellers
        try {
            WebElement clothingMenu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//nav//a[contains(translate(text()," +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'clothing')]")));

            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].dispatchEvent(new MouseEvent('mouseover', {bubbles: true}));",
                clothingMenu);

            Thread.sleep(800);

            WebElement bestSellers = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//nav//a[contains(translate(text()," +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'bestsellers')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", bestSellers);
            Thread.sleep(1500);
            System.out.println("Navigated to Bestsellers");
        } catch (InterruptedException e) {
            System.out.println("Could not navigate to Bestsellers: " + e.getMessage());
            return;
        }

        // Step 3: Click first product
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

            wait.until(ExpectedConditions.urlContains("/products/"));
            Thread.sleep(1500);
            System.out.println("Opened product page");
        } catch (InterruptedException e) {
            System.out.println("Could not open product: " + e.getMessage());
            return;
        }

        // Step 4: Open size dropdown and select first available size
        try {
            WebElement sizeDropdownTrigger = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[@data-sizepicker-trigger]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", sizeDropdownTrigger);
            Thread.sleep(800);

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

        // Step 5: Click Add to Bag
        try {
            WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(.,'Add')]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", addToCart);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCart);

            Thread.sleep(1500);
            System.out.println("Add to Bag clicked");
        } catch (InterruptedException e) {
            System.out.println("Could not click Add to Bag: " + e.getMessage());
            return;
        }

        // Step 6: Navigate directly to cart page
        try {
            driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/cart");
            wait.until(ExpectedConditions.urlContains("/cart"));
            Thread.sleep(2000);
            System.out.println("Opened Cart page");
        } catch (InterruptedException e) {
            System.out.println("Could not open Cart: " + e.getMessage());
            return;
        }

        // Step 7: Read current quantity before change
        String qtyBefore = "1";
        try {
            WebElement qtyDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//select[@data-line-item-component='qty-action']")));

            Select qtySelect = new Select(qtyDropdown);
            qtyBefore = qtySelect.getFirstSelectedOption().getText().trim();
            System.out.println("Current quantity before change: " + qtyBefore);
        } catch (Exception e) {
            System.out.println("Could not read current quantity: " + e.getMessage());
        }

        // Step 8: Click quantity dropdown and select a higher quantity (add units)
        // then select lower quantity (deduct units)
        // ✅ Exact locator: select[data-line-item-component='qty-action']
        try {
            WebElement qtyDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@data-line-item-component='qty-action']")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", qtyDropdown);

            // First increase to 3
            Select qtySelect = new Select(qtyDropdown);
            qtySelect.selectByVisibleText("3");
            Thread.sleep(2000);
            System.out.println("Quantity increased to 3");

            // Now deduct - select back to 1
            qtyDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//select[@data-line-item-component='qty-action']")));
            qtySelect = new Select(qtyDropdown);
            qtySelect.selectByVisibleText("1");
            Thread.sleep(2000);
            System.out.println("Quantity deducted back to 1");

        } catch (InterruptedException e) {
            System.out.println("Could not change quantity: " + e.getMessage());
            return;
        }

        // Step 9: Verify quantity was updated
        try {
            WebElement qtyDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//select[@data-line-item-component='qty-action']")));

            Select qtySelect = new Select(qtyDropdown);
            String qtyAfter = qtySelect.getFirstSelectedOption().getText().trim();
            System.out.println("Quantity after deduction: " + qtyAfter);

            if (qtyAfter.equals("1")) {
                System.out.println("PASS - Quantity successfully deducted to 1");
            } else {
                System.out.println("FAIL - Quantity not updated correctly. Shows: " + qtyAfter);
            }
        } catch (Exception e) {
            System.out.println("Could not verify quantity: " + e.getMessage());
        }

        Thread.sleep(3000);
    }
}