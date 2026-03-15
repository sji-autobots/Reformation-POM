package com.reformation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PDPPage {
    /**
     * Scrolls to the "We're cute too" section (//div[@role='tablist'])
     */
    public void scrollToCuteTooSection() {
        WebElement tabList = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='tablist']")));
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", tabList);
    }

    /**
     * Loads a URL with authentication for the first PDP page load.
     * 
     * @param sku The SKU to load
     */
    public void loadPDPWithAuth(String sku) {
        String url = "https://storefront:2025-Ref4Eva@stage.thereformation.com/" + sku + ".html";
        driver.get(url);
    }

    /**
     * Verifies that the given SKUs are linked/displayed in the "We're cute too"
     * section.
     * 
     * @param skus Array of SKUs to check
     * @return true if all SKUs are found, false otherwise
     */
    public boolean arePDPlinksDisplayed(String[] skus) {
        WebElement recSection = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.xpath("//div[contains(@class,'pdp__recommendations')]")));
        java.util.List<WebElement> links = recSection.findElements(By.tagName("a"));
        for (String sku : skus) {
            boolean found = false;
            for (WebElement link : links) {
                String href = link.getAttribute("href");
                if (href != null && href.contains(sku)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    private WebDriver driver;
    private WebDriverWait wait;

    public PDPPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(25));
    }

    public void openISPU() {
        driver.get("https://storefront:2025-Ref4Eva@stage.thereformation.com/1313793IVO002.html");
    }

    /**
     * Selects the main product image on PDP.
     */
    public void selectMainProductImage() {
        System.out.println("[DEBUG] Waiting for main product image to be clickable...");
        WebElement mainImage = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("img[data-pdp-main-image]")));
        System.out.println("[DEBUG] Clicking main product image...");
        mainImage.click();
        try { Thread.sleep(1200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /**
     * Verifies that the image overlay is displayed.
     */
    public boolean isImageOverlayDisplayed() {
        System.out.println("[DEBUG] Waiting for image overlay to be visible...");
        boolean displayed = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.id("modal-productZoom"))).isDisplayed();
        System.out.println("[DEBUG] Image overlay displayed: " + displayed);
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return displayed;
    }

    /**
     * Clicks the zoom button in the overlay.
     */
    public void clickZoomButton() {
        System.out.println("[DEBUG] Waiting for zoom button to be clickable...");
        WebElement zoomBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[@class='product-zoom__btn']")));
        System.out.println("[DEBUG] Clicking zoom button...");
        zoomBtn.click();
        try { Thread.sleep(1200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /**
     * Verifies that the image is zoomed in (checks for 'zoom--active' class).
     */
    public boolean isImageZoomedIn() {
        System.out.println("[DEBUG] Waiting for zoomed-in image (zoom--active class)...");
        WebElement zoomedImage = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.cssSelector("img.product-zoom__image.zoom--active")));
        boolean zoomed = zoomedImage.isDisplayed();
        System.out.println("[DEBUG] Image zoomed in: " + zoomed);
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return zoomed;
    }

    /**
     * Clicks the close (X) button on the overlay.
     */
    public void closeImageOverlay() {
        System.out.println("[DEBUG] Waiting for close (X) button to be clickable...");
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[@title='Close modal']")));
        System.out.println("[DEBUG] Clicking close (X) button...");
        closeBtn.click();
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /**
     * Verifies that the image overlay is dismissed.
     */
    public boolean isImageOverlayDismissed() {
        System.out.println("[DEBUG] Waiting for image overlay to be dismissed (invisible)...");
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("modal-productZoom")));
            System.out.println("[DEBUG] Image overlay dismissed.");
            Thread.sleep(800);
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("[DEBUG] Timeout: Image overlay not dismissed.");
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
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
        if (!text.contains("'"))
            return "'" + text + "'";
        String[] parts = text.split("'");
        StringBuilder xpath = new StringBuilder("concat(");
        for (int i = 0; i < parts.length; i++) {
            xpath.append("'" + parts[i] + "'");
            if (i != parts.length - 1)
                xpath.append(", \"'\", ");
        }
        xpath.append(")");
        return xpath.toString();
    }

    public void clickRecentlyViewedButton() {
        WebElement recentlyViewedBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@data-tab-name='Product Detail Page:Recently Viewed']")));
        recentlyViewedBtn.click();
    }
}
