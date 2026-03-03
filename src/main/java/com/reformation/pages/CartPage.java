package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Locators
    private final By firstProduct =
            By.xpath("(//a[contains(@href,'product')])[1]");

    private final By sizeButtons =
            By.xpath("//button[@data-attr='size' and @data-attr-selectable='true']");

    private final By addToCartBtn =
            By.xpath("//button[contains(.,'Add')]");

    private final By cartIcon =
            By.xpath("//a[contains(@href,'cart')]");

    // Remove flow locators (based on your HTML)
    private final By removeIcon =
            By.xpath("(//button[@data-line-item-component='remove-action'])[1]");

    private final By confirmRemoveButton =
            By.xpath("(//button[@data-line-item-component='remove-confirm-action'])[1]");

    private final By emptyCartMessage =
            By.xpath("//*[contains(text(),'Your cart is empty')]");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Open first product
    public void openFirstProduct() {
        wait.until(ExpectedConditions.elementToBeClickable(firstProduct)).click();
        System.out.println("✅ First product opened");
    }

    // Select first available size
    public void selectFirstSize() {

        wait.until(d -> !d.findElements(sizeButtons).isEmpty());

        List<WebElement> sizes = driver.findElements(sizeButtons);

        if (!sizes.isEmpty()) {

            WebElement firstSize = sizes.get(0);

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView(true);", firstSize);

            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", firstSize);

            wait.until(ExpectedConditions.attributeToBe(
                    firstSize,
                    "aria-pressed",
                    "true"
            ));

            System.out.println("✅ Size selected properly");

        } else {
            System.out.println("❌ No selectable sizes found");
        }
    }

    // Click Add to Bag
    public void clickAddToCart() {

        wait.until(d -> d.findElement(addToCartBtn).isEnabled());

        WebElement addButton = driver.findElement(addToCartBtn);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);", addButton);

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", addButton);

        System.out.println("✅ Add to Bag clicked");
    }

    // Open cart
    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        System.out.println("✅ Cart opened");
    }

    // Remove item (2-step modal process)
    public void removeItem() {

        // Step 1: Click trash icon
        WebElement removeBtn = wait.until(
                ExpectedConditions.elementToBeClickable(removeIcon)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", removeBtn);

        System.out.println("🗑️ Remove icon clicked");

        // Step 2: Click confirm remove in modal
        WebElement confirmBtn = wait.until(
                ExpectedConditions.elementToBeClickable(confirmRemoveButton)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", confirmBtn);

        System.out.println("✅ Confirm remove clicked");

        // Step 3: Wait until item disappears
        wait.until(ExpectedConditions.invisibilityOfElementLocated(removeIcon));

        System.out.println("✅ Item removed successfully");
    }

    // Verify cart is empty
    public boolean isCartEmpty() {

        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(emptyCartMessage)
            ).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}