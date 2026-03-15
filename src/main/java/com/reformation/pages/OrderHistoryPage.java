package com.reformation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class OrderHistoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By orderList  = By.cssSelector("[data-order-component='order-list']");
    private final By orderItems = By.cssSelector(
        "[class*='order-history__item'], [class*='order__item'], [class*='order-card'], [class*='order-tile']"
    );
    private final By emptyMsg = By.xpath(
        "//*[contains(text(),'no orders') or contains(text(),'No orders') or contains(text(),'haven')]"
    );

    public OrderHistoryPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Waits for the order list container to load. */
    public void waitForOrderListToLoad() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(orderList));
        Thread.sleep(1500);
    }

    /** Returns the list of order item elements, or empty list if none found. */
    public List<WebElement> getOrders() {
        try {
            return wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(orderItems)
            );
        } catch (Exception e) {
            return List.of();
        }
    }

    /** Returns true if an empty-state message is visible. */
    public boolean isEmptyStateDisplayed() {
        try {
            driver.findElement(emptyMsg);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /** Returns number of orders displayed. */
    public int getOrderCount() {
        return getOrders().size();
    }
}
