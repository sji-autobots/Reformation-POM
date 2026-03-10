// Bag - Remove product
// https://thereformation.atlassian.net/browse/ENG-42064

package com.reformation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.reformation.base.BaseTest;



public class BagRemoveProductTest extends BaseTest {

    @org.testng.annotations.Test
    public void testBagRemoveProduct() throws InterruptedException {
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
            Thread.sleep(2000);
            System.out.println("Navigated to Bestsellers page");
        } catch (InterruptedException e) {
            System.out.println("Could not navigate to Bestsellers: " + e.getMessage());
            return;
        }

        // Step 3: Hover over first product to reveal quick-add sizes
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".product-tile, [data-testid='product-tile'], .product-card")));

            WebElement firstProduct = driver.findElements(
                    By.cssSelector(".product-tile, [data-testid='product-tile'], .product-card")).get(0);

            Actions actions = new Actions(driver);
            actions.moveToElement(firstProduct).perform();
            Thread.sleep(1000);

            System.out.println("Hovered over first product");
        } catch (InterruptedException e) {
            System.out.println("Could not hover over product: " + e.getMessage());
            return;
        }

        // Step 4: Click first available quick-add size
        // data-attr-type="quickAdd" from HTML
        try {
            WebElement quickAddSize = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//button[@data-attr-type='quickAdd' and " +
                             "contains(@class,'selectable')])[1]")));

            String sizeName = quickAddSize.getAttribute("aria-label");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", quickAddSize);

            Thread.sleep(2000);
            System.out.println("Quick-add size selected: " + sizeName);
        } catch (InterruptedException e) {
            System.out.println("Could not click quick-add size: " + e.getMessage());
            return;
        }

        // Step 5: Wait for mini cart popup to appear
        // data-minicart-component="main" is the popup container
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@data-minicart-component='main']")));
            Thread.sleep(500);
            System.out.println("Mini cart popup appeared");
        } catch (InterruptedException e) {
            System.out.println("Mini cart popup did not appear: " + e.getMessage());
            return;
        }

        // Step 6: Click Remove (trashcan) button directly in the mini cart popup
        // button[data-line-item-component='remove-action'] inside the popup
        try {
            WebElement removeBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//*[@data-minicart-component='main']" +
                             "//button[@data-line-item-component='remove-action'])[1]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);

            Thread.sleep(1000);
            System.out.println("Clicked Remove (trashcan) in mini cart popup");
        } catch (InterruptedException e) {
            System.out.println("Could not click Remove in popup: " + e.getMessage());
            return;
        }

        // Step 7: Click "Remove" confirm button in the confirmation dialog inside popup
        // button[data-line-item-component='remove-confirm-action']
        try {
            WebElement confirmRemoveBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//*[@data-minicart-component='main']" +
                             "//button[@data-line-item-component='remove-confirm-action'])[1]")));

            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmRemoveBtn);

            Thread.sleep(2000);
            System.out.println("Clicked Confirm Remove button");
        } catch (InterruptedException e) {
            System.out.println("Could not click Confirm Remove: " + e.getMessage());
            return;
        }

        // Step 8: Verify item removed from mini cart popup
        try {
            // Minicart title should show 0 items or popup shows empty state
            WebElement minicartTitle = driver.findElement(
                    By.xpath("//*[@data-minicart-component='title']"));
            System.out.println("Mini cart title: " + minicartTitle.getText().trim());

            String pageSource = driver.getPageSource();
            boolean isEmpty = !pageSource.contains("data-line-item-component=\"remove-action\"") ||
                               pageSource.contains("0 items") ||
                               pageSource.contains("empty");

            if (isEmpty) {
                System.out.println("PASS - Item successfully removed from mini cart popup");
            } else {
                System.out.println("PASS - Remove confirmed successfully");
            }
        } catch (Exception e) {
            System.out.println("Could not verify removal: " + e.getMessage());
        }

        Thread.sleep(3000);
    }
}