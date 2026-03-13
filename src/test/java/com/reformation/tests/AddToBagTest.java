package com.reformation.tests;

import com.reformation.base.BaseTest;
import com.reformation.pages.PDPPage;
import com.reformation.pages.CartPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AddToBagTest extends BaseTest {
    @Test
    public void testAddToBagAndCheckout() {
        // Step 1: Go to a PDP (reuse existing method)
        PDPPage pdpPage = new PDPPage(driver);
        pdpPage.loadPDPWithAuth("1312627BLK0XS"); // Use a valid SKU
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(25));


                // Step 2: Fetch product details
                String product = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//h1[contains(@class, 'pdp__name')]"))).getText();
                System.out.println("[DEBUG] Captured product name: " + product);

                // Try to get selected size (may not be selected by default)
                String size = "";
                try {
                        size = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                        By.xpath("//span[@class='product-attribute__selected-value'][@data-attr-size-value]"))).getText();
                } catch (Exception e) {
                        // fallback: select first size if not already selected
                        CartPage cartPage = new CartPage(driver);
                        cartPage.selectFirstSize();
                        size = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                        By.xpath("//span[@class='product-attribute__selected-value'][@data-attr-size-value]"))).getText();
                }
                System.out.println("[DEBUG] Captured size: " + size);

                // Try to get selected color (may not be present for all products)
                String color = "";
                try {
                        color = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                        By.xpath("//button[contains(@class,'swatch--color-pdp') and contains(@class,'selected') and contains(@class,'selectable')]"))).getAttribute("aria-label");
                } catch (Exception e) {
                        // fallback: get color text if available
                        try {
                                color = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//span[@class='product-attribute__selected-value'][@data-attr-color-value]"))).getText();
                        } catch (Exception ex) {
                                color = "";
                        }
                }
                System.out.println("[DEBUG] Captured color: " + color);

        // Step 3: Add to Bag using PDPPage method
        pdpPage.clickAddToBag();


        // Step 4: Verify bag drawer slides out and item is shown
        By bagDrawerLocator = By.xpath("//*[@data-minicart-component='main']");
        WebElement bagDrawer = wait.until(ExpectedConditions.visibilityOfElementLocated(bagDrawerLocator));
        String bagDrawerText = bagDrawer.getText();
        System.out.println("[DEBUG] Bag drawer text: " + bagDrawerText);
        Assert.assertTrue(bagDrawer.isDisplayed(), "Bag drawer should be visible after adding to bag");
        // Re-fetch before assertion to avoid stale element
        bagDrawer = wait.until(ExpectedConditions.visibilityOfElementLocated(bagDrawerLocator));
        Assert.assertTrue(bagDrawer.getText().contains(product), "Bag drawer should show the product name");

        // Step 5: Click Checkout
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(.,'checkout')]")));
        checkoutBtn.click();

        // Step 6: Verify correct details on checkout page
        String checkoutSize = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-line-item-component='size']"))).getText();
        String checkoutColor = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-line-item-component='color']"))).getText();
        String checkoutProduct = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@data-product-component='header']"))).getText();

        System.out.println("[DEBUG] Checkout size: " + checkoutSize);
        System.out.println("[DEBUG] Checkout color: " + checkoutColor);
        System.out.println("[DEBUG] Checkout product: " + checkoutProduct);
        System.out.println("[DEBUG] Comparing size: '" + size + "' with checkout: '" + checkoutSize + "'");
        System.out.println("[DEBUG] Comparing color: '" + color + "' with checkout: '" + checkoutColor + "'");
        System.out.println("[DEBUG] Comparing product: '" + product + "' with checkout: '" + checkoutProduct + "'");

        Assert.assertTrue(checkoutSize.contains(size), "Checkout size should match selected size");
        if (!color.isEmpty()) {
            Assert.assertTrue(color.contains(checkoutColor), "Checkout color should match selected color");
        }
        Assert.assertTrue(checkoutProduct.contains(product), "Checkout product should match selected product");
    }
}
