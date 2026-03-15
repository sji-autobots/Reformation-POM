package com.reformation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class VIPPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By showBenefitsBtn = By.cssSelector("span.account__fwb-toggle__trigger");

    public VIPPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Clicks the Show Benefits toggle on the VIP section of My Account. */
    public void clickShowBenefits() throws InterruptedException {
        WebElement btn = wait.until(
            ExpectedConditions.presenceOfElementLocated(showBenefitsBtn)
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", btn
        );
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }
}
