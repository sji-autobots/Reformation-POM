// https://thereformation.atlassian.net/browse/ENG-42062
// Bag - add units
package com.reformation.tests;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;



public class BagAddUnitsTest extends BaseTest {

    @org.testng.annotations.Test
    public void testBagAddUnits() throws InterruptedException {
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
        try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@data-minicart-component='main']")));
                Thread.sleep(500);
                System.out.println("Mini cart popup appeared");
        } catch (InterruptedException e) {
            System.out.println("Mini cart popup did not appear: " + e.getMessage());
            
            return;
        }

        // Step 6: Read current quantity before change
        try {
                WebElement qtyDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//*[@data-minicart-component='main']" +
                         "//select[@data-line-item-component='qty-action'])[1]")));

                Select qtySelect = new Select(qtyDropdown);
                String qtyBefore = qtySelect.getFirstSelectedOption().getText().trim();
                System.out.println("Current quantity before adding units: " + qtyBefore);
        } catch (Exception e) {
            System.out.println("Could not read current quantity: " + e.getMessage());
        }

        // Step 7: Add units - select higher quantity (3) in mini cart popup
        // select[data-line-item-component='qty-action'] inside mini cart popup
        try {
                WebElement qtyDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//*[@data-minicart-component='main']" +
                         "//select[@data-line-item-component='qty-action'])[1]")));

                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", qtyDropdown);
                Select qtySelect = new Select(qtyDropdown);
                qtySelect.selectByVisibleText("3");
                Thread.sleep(2000);
                System.out.println("Quantity increased to 3 in mini cart popup");
        } catch (InterruptedException e) {
            System.out.println("Could not add units: " + e.getMessage());
            
            return;
        }

        // Step 8: Verify final quantity is 3
        try {
            WebElement qtyDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//*[@data-minicart-component='main']" +
                             "//select[@data-line-item-component='qty-action'])[1]")));

            Select qtySelect = new Select(qtyDropdown);
            String qtyAfter = qtySelect.getFirstSelectedOption().getText().trim();
            System.out.println("Quantity after adding units: " + qtyAfter);

            if (qtyAfter.equals("3")) {
                System.out.println("PASS - Units successfully added. Quantity is now 3 in mini cart popup");
            } else {
                System.out.println("FAIL - Quantity not updated correctly. Shows: " + qtyAfter);
            }
        } catch (Exception e) {
            System.out.println("Could not verify quantity: " + e.getMessage());
        }

        Thread.sleep(3000);
    }
}