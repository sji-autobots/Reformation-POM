package com.reformation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PDPPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public PDPPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(25));
    }

    public void openISPU() {
        driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/1313793IVO002.html"); 
    }

    public void clickPickItUp() {
        WebElement pickItUpBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button//u[contains(text(),'Pick it up')]")));
        pickItUpBtn.click();
    }

    public void enterPostalCodeAndFindStores(String postalCode) {
        WebElement postalInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//input[@id='store-postal-code']")));
        postalInput.clear();
        postalInput.sendKeys(postalCode);
        WebElement findStoresBtn = driver.findElement(By.xpath("//button[normalize-space()='Find stores']"));
        findStoresBtn.click();
    }

    public void selectStoreAndSave(String storeName) {
        WebElement storeOption = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[normalize-space()=" + escapeXpath(storeName) + "]")));
        storeOption.click();
        WebElement saveBtn = driver.findElement(By.xpath("//button[normalize-space()='Save store']"));
        saveBtn.click();
    }

    public boolean isChangeLocationDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//button[normalize-space()='Change Location']"))).isDisplayed();
    }

    public void clickAddToBag() {
        WebElement addToBagBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//span[@class='product-add__button_text font-size--18']")));
        addToBagBtn.click();
    }

    // Helper for dynamic XPaths
    private String escapeXpath(String text) {
        if (!text.contains("'")) return "'" + text + "'";
        String[] parts = text.split("'");
        StringBuilder xpath = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            xpath.append("'" + parts[i] + "'");
            if (i != parts.length - 1) xpath.append(", \"'\", ");
        }
        xpath.append(")");
        return xpath.toString();
    }
}
