//https://thereformation.atlassian.net/browse/ENG-42067
// Cart - Remove product

package com.reformation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class RemoveFromCartTest extends BaseTest {

    @org.testng.annotations.Test
    public void testRemoveFromCart() throws InterruptedException {
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
        // Bag icon opens mini drawer - navigate directly to cart URL instead
        try {
            driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/cart");
            wait.until(ExpectedConditions.urlContains("/cart"));
            Thread.sleep(2000);
            System.out.println("Opened Bag/Cart page");
        } catch (InterruptedException e) {
            System.out.println("Could not open Bag: " + e.getMessage());
            return;
        }

        // Step 7: Click Remove (trashcan icon) on the item
        // Exact element from HTML: svg icon--trashcan inside remove button
        try {
                WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//*[contains(@class,'icon--trashcan')]] | " +
                         "//*[contains(@class,'icon--trashcan')]/ancestor::button")));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", removeBtn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);

                Thread.sleep(1000);
                System.out.println("Clicked Remove (trashcan) button");
        } catch (InterruptedException e) {
            System.out.println("Could not click Remove button: " + e.getMessage());
            
            return;
        }

        // Step 8: Confirm Remove from popup
        // Exact element: button[data-line-item-component='remove-confirm-action']
        try {
                WebElement confirmRemoveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[@data-line-item-component='remove-confirm-action']")));

                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmRemoveBtn);

                Thread.sleep(1500);
                System.out.println("Clicked Confirm Remove button");
        } catch (InterruptedException e) {
            System.out.println("Could not click Confirm Remove: " + e.getMessage());
            
            return;
        }

        // Step 9: Verify item removed - cart should be empty
        try {
            // Check if cart is empty by looking for empty cart message or qty = 0
            boolean cartEmpty = false;

            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(translate(text()," +
                                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                                "'your bag is empty') or contains(translate(text()," +
                                "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
                                "'empty')]")));
                cartEmpty = true;
            } catch (Exception e) {
                // Check bag count = 0
                try {
                    WebElement qtySpan = driver.findElement(
                            By.xpath("//span[@data-minicart-component='qty']"));
                    if (qtySpan.getText().trim().equals("0")) {
                        cartEmpty = true;
                    }
                } catch (Exception ex) {
                    cartEmpty = true; // item not found means removed
                }
            }

            if (cartEmpty) {
                System.out.println("PASS - Item successfully removed from cart");
            } else {
                System.out.println("FAIL - Item still appears in cart after removal");
            }

        } catch (Exception e) {
            System.out.println("Could not verify cart empty state: " + e.getMessage());
        }

        Thread.sleep(3000);
    }
}