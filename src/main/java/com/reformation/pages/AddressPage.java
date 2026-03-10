package com.reformation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.util.List;

public class AddressPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By addAddressBtn    = By.cssSelector("button.add-address");
    private final By addressForm      = By.id("dwfrm_address");
    private final By firstNameField   = By.id("firstName");
    private final By lastNameField    = By.id("lastName");
    private final By address1Field    = By.id("address1");
    private final By cityField        = By.id("city");
    private final By stateDropdown    = By.id("state");
    private final By zipField         = By.id("zipCode");
    private final By phoneField       = By.id("phone");
    private final By defaultCheckbox  = By.id("dwfrm_address_makeDefaultAddressChk-add");
    private final By saveBtn          = By.cssSelector("button[name='save']");
    private final By addressCardBody  = By.cssSelector("div.account-card__body");
    private final By confirmModal     = By.cssSelector("div.window-modal__content[role='dialog']");
    private final By confirmDeleteBtn = By.cssSelector("button.modal__action--confirm");
    private final By editModal        = By.cssSelector("div.window-modal__content");

    public AddressPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait   = wait;
    }

    /** Adds a new address and marks it as default. */
    public void addNewDefaultAddress() throws InterruptedException {
        WebElement btn = wait.until(
            ExpectedConditions.presenceOfElementLocated(addAddressBtn)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(addressForm));

        driver.findElement(firstNameField).sendKeys("Jade");
        driver.findElement(lastNameField).sendKeys("West");
        driver.findElement(address1Field).sendKeys("Two Rodeo, Wilshire Blvd");
        driver.findElement(cityField).sendKeys("Beverly Hills");
        new Select(driver.findElement(stateDropdown)).selectByValue("NY");
        driver.findElement(zipField).sendKeys("90210");
        driver.findElement(phoneField).sendKeys("(209) 300-2557");

        WebElement checkbox = wait.until(
            ExpectedConditions.presenceOfElementLocated(defaultCheckbox)
        );
        if (!checkbox.isSelected()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkbox);
        }

        WebElement save = wait.until(
            ExpectedConditions.presenceOfElementLocated(saveBtn)
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", save);
    }

    /** Adds a brand-new address (no default flag). */
    public void addNewAddress(String firstName, String lastName, String address1,
                              String city, String state, String zip, String phone)
            throws InterruptedException {

        WebElement btn = wait.until(
            ExpectedConditions.presenceOfElementLocated(addAddressBtn)
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", btn
        );
        Thread.sleep(600);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(addressForm));
        Thread.sleep(2000);

        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(address1Field).sendKeys(address1);
        driver.findElement(cityField).sendKeys(city);
        new Select(driver.findElement(stateDropdown)).selectByValue(state);
        driver.findElement(zipField).sendKeys(zip);
        driver.findElement(phoneField).sendKeys(phone);

        WebElement save = wait.until(
            ExpectedConditions.presenceOfElementLocated(saveBtn)
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", save
        );
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", save);
        Thread.sleep(2000);

        // Click save a second time if modal persists (React validation quirk)
        try {
            WebElement saveAgain = wait.until(
                ExpectedConditions.presenceOfElementLocated(saveBtn)
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveAgain);
        } catch (Exception ignored) {}
    }

    /** Deletes the first visible address card. */
    public void deleteFirstAddress() throws InterruptedException {
        wait.until(ExpectedConditions.presenceOfElementLocated(addressCardBody));
        Thread.sleep(1000);

        WebElement deleteBtn = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.account-card__body button.address-summary__action[aria-label='Delete this address from your address book']")
            )
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", deleteBtn
        );
        Thread.sleep(800);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);

        WebElement modal = wait.until(
            ExpectedConditions.visibilityOfElementLocated(confirmModal)
        );
        Thread.sleep(800);

        WebElement confirm = modal.findElement(confirmDeleteBtn);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", confirm
        );
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirm);
        Thread.sleep(2500);
    }

    /** Edits the address card belonging to the given full name. */
    public void editAddressByName(String fullName, String newAddress1) throws InterruptedException {
        List<WebElement> cards = driver.findElements(addressCardBody);

        WebElement editBtn = null;
        for (WebElement card : cards) {
            if (card.getText().contains(fullName)) {
                editBtn = card.findElement(
                    By.cssSelector("a[aria-label*='Edit Address']")
                );
                break;
            }
        }

        if (editBtn == null) {
            throw new RuntimeException("Could not find address card for: " + fullName);
        }

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", editBtn
        );
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editBtn);

        WebElement modal = wait.until(
            ExpectedConditions.visibilityOfElementLocated(editModal)
        );

        WebElement address1 = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.window-modal__content #address1")
            )
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].value = '';", address1);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));", address1
        );
        Thread.sleep(300);

        address1.click();
        address1.sendKeys(newAddress1);
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));", address1
        );
        Thread.sleep(500);

        WebElement save = modal.findElement(
            By.cssSelector("button[type='submit'][name='save']")
        );
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});", save
        );
        Thread.sleep(300);
        save.click();

        wait.until(
            ExpectedConditions.invisibilityOfElementLocated(editModal)
        );
    }

    /** Returns the count of address cards currently on the page. */
    public int getAddressCount() {
        return driver.findElements(addressCardBody).size();
    }
}
