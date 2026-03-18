package com.reformation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckoutPage {
        @FindBy(id = "conf-page-order-number")
        private WebElement orderIdElement;

        /**
         * Handles the Afterpay payment flow: selects Afterpay, clicks review, logs in,
         * and confirms order.
         * 
         * @param afterpayEmail    Afterpay email
         * @param afterpayPassword Afterpay password
         */
        public void completeAfterpayFlow(String afterpayEmail, String afterpayPassword) {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
                // Scroll to and select Afterpay radio button
                WebElement afterpayRadioButton = wait.until(ExpectedConditions
                                .elementToBeClickable(By.cssSelector("label[data-method-id='AFTERPAY_PBI']")));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].scrollIntoView({block: 'center'});", afterpayRadioButton);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                afterpayRadioButton);

                // Click review button to proceed
                WebElement reviewButton = wait.until(ExpectedConditions
                                .elementToBeClickable(By.xpath("//button[@id='payment-page-button']")));
                reviewButton.click();

                // Afterpay login flow
                WebElement afterpayEmailField = wait.until(ExpectedConditions
                                .visibilityOfElementLocated(By.xpath("//input[@aria-labelledby='loginEmailLabel']")));
                afterpayEmailField.sendKeys(afterpayEmail);
                WebElement afterpayContinueButton = wait
                                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
                afterpayContinueButton.click();

                WebElement afterpayPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//input[@aria-labelledby='loginPasswordLabel']")));
                afterpayPasswordField.sendKeys(afterpayPassword);
                afterpayContinueButton = wait
                                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
                afterpayContinueButton.click();

                // Confirm CTA
                WebElement afterpayConfirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[@data-dd-action-name='Confirm Checkout Button']")));
                afterpayConfirmButton.click();
        }

        /**
         * Handles the PayPal payment flow: selects PayPal, clicks the button, switches
         * window, logs in, and returns.
         * 
         * @param paypalEmail    PayPal email
         * @param paypalPassword PayPal password
         * @throws InterruptedException
         */
        public void completePaypalFlow(String paypalEmail, String paypalPassword) throws InterruptedException {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
                // Click PayPal radio button
                WebElement paypalRadioButton = wait
                                .until(ExpectedConditions.elementToBeClickable(
                                                By.cssSelector("label[data-method-id='PayPal']")));
                // Scroll PayPal radio into view before clicking
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                                paypalRadioButton);
                Thread.sleep(500); // Small pause for UI
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                paypalRadioButton);
                Thread.sleep(500); // Small pause for UI

                // Click PayPal button (triggers popup/new window)
                WebElement paypalButton = wait
                                .until(ExpectedConditions.elementToBeClickable(
                                                By.cssSelector("div#billing-paypal-button-container")));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", paypalButton);
                Thread.sleep(500); // Small pause for UI
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", paypalButton);

                // Switch to PayPal window
                String mainWindow = driver.getWindowHandle();
                for (String handle : driver.getWindowHandles()) {
                        if (!handle.equals(mainWindow)) {
                                driver.switchTo().window(handle);
                                break;
                        }
                }

                // Interact with PayPal login
                WebElement paypalEmailField = wait.until(ExpectedConditions
                                .visibilityOfElementLocated(
                                                By.cssSelector("input#email[name='login_email'][type='email']")));
                paypalEmailField.sendKeys(paypalEmail);

                // Wait for Next button and click it
                WebElement paypalNextButton = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.id("btnNext")));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                paypalNextButton);

                // Wait for password field, enter password
                WebElement paypalPasswordField = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
                paypalPasswordField.sendKeys(paypalPassword);

                // Wait for Submit/Login button and click it
                WebElement paypalLoginButton = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.id("btnLogin")));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                paypalLoginButton);

                // Wait for Continue to Review Order button and click it
                WebElement paypalContinueToReviewOrderButton = wait.until(ExpectedConditions
                                .visibilityOfElementLocated(
                                                By.cssSelector("button[data-testid='submit-button-initial']")));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                paypalContinueToReviewOrderButton);
                System.out.println("Clicked on Continue to Review Order button");

                // Switch back to the main window
                driver.switchTo().window(mainWindow);

                // Click on payment button to proceed
                WebElement placeOrderButton = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.id("place-order-page-button")));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                placeOrderButton);
                System.out.println("Clicked on Place Order button");

                // Wait for order confirmation
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#conf-page-order-number")));
                System.out.println("Order confirmation received.");
        }

        /**
         * Enters credit card details in the payment form.
         */
        public void enterCreditCardNumber(String cardNumber) {
                WebDriverWait cardWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
                // Click card-element to focus (scroll first)
                WebElement cardElement = cardWait.until(ExpectedConditions.elementToBeClickable(By.id("card-element")));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                                                cardElement);
                cardElement.click();
                // Switch to Stripe iframe
                WebElement stripeIframe = cardWait.until(ExpectedConditions.presenceOfElementLocated(
                                By.cssSelector("iframe[name^='__privateStripeFrame']")));
                driver.switchTo().frame(stripeIframe);
                // Click the card number input (scroll first)
                WebElement cardNumberInput = cardWait
                                .until(ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@placeholder='Card number']")));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                                                cardNumberInput);
                cardNumberInput.click();
                // Switch back to main context to click the radio label
                driver.switchTo().defaultContent();
                WebElement radioLabel = cardWait
                                .until(ExpectedConditions.elementToBeClickable(
                                                By.xpath("//label[@id='checkout-stripe-radio-label']")));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                                                radioLabel);
                radioLabel.click();
                // Switch again to Stripe iframe
                driver.switchTo().frame(stripeIframe);
                // Click the card number input again to ensure focus (scroll first)
                cardNumberInput = cardWait
                                .until(ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@placeholder='Card number']")));
                ((org.openqa.selenium.JavascriptExecutor) driver)
                                .executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});",
                                                cardNumberInput);
                cardNumberInput.click();
                // Press tab key once
                cardNumberInput.sendKeys(org.openqa.selenium.Keys.TAB);
                // Enter credit card number one digit at a time
                for (char digit : cardNumber.toCharArray()) {
                        String digitStr = String.valueOf(digit);
                        cardNumberInput.sendKeys(digitStr);
                        try {
                                Thread.sleep(100); // Small delay to mimic real typing
                        } catch (InterruptedException e) {
                                // Ignore
                        }
                }
                // Switch back to main context
                driver.switchTo().defaultContent();
        }

        /**
         * Fills shipping address fields and handles address autocomplete.
         */
        public void fillShippingAddress(String firstName, String lastName, String phone, String address) {
                WebDriverWait shippingWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
                WebElement firstNameInput = shippingWait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@id='shippingFirstNamedefault']")));
                firstNameInput.clear();
                firstNameInput.sendKeys(firstName);
                WebElement lastNameInput = shippingWait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@id='shippingLastNamedefault']")));
                lastNameInput.clear();
                lastNameInput.sendKeys(lastName);
                WebElement phoneInput = shippingWait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@id='shippingPhoneNumberdefault']")));
                phoneInput.clear();
                phoneInput.sendKeys(phone);

                WebElement addressInput = shippingWait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@id='shippingAddressOnedefault']")));
                addressInput.click();
                addressInput.clear();
                addressInput.sendKeys(address);
                // Simulate tab key for saving
                addressInput.sendKeys(org.openqa.selenium.Keys.TAB);

                // Fill city
                WebElement cityInput = shippingWait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@id='shippingAddressCitydefault']")));
                cityInput.clear();
                cityInput.sendKeys("Pinckneyville");

                // Fill state
                WebElement stateSelect = shippingWait
                                .until(ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//select[@id='shippingStatedefault']")));
                org.openqa.selenium.support.ui.Select stateDropdown = new org.openqa.selenium.support.ui.Select(
                                stateSelect);
                stateDropdown.selectByVisibleText("Illinois");

                // Fill zip code
                WebElement zipInput = shippingWait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//input[@id='shippingZipCodedefault']")));
                zipInput.clear();
                zipInput.sendKeys("62274");

        }

        /**
         * Verifies account signup section and password fields are present after order
         * confirmation.
         */
        public void verifyAccountSignupSection() {
                WebDriverWait confirmWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
                WebElement accountSection = confirmWait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//div[contains(@class,'order-confirmation__create-account')]")));
                if (!accountSection.isDisplayed()) {
                        throw new AssertionError("Account signup section should be visible");
                }
                WebElement passwordField = accountSection
                                .findElement(By.xpath(
                                                ".//input[@type='password' and @name='dwfrm_newPasswords_newpassword']"));
                WebElement confirmPasswordField = accountSection
                                .findElement(By.xpath(
                                                ".//input[@type='password' and @name='dwfrm_newPasswords_newpasswordconfirm']"));
                if (!passwordField.isDisplayed()) {
                        throw new AssertionError("Password field should be visible in account signup section");
                }
                if (!confirmPasswordField.isDisplayed()) {
                        throw new AssertionError("Confirm password field should be visible in account signup section");
                }
        }

        private WebDriver driver;
        private WebDriverWait wait;

        public CheckoutPage(WebDriver driver) {
                this.driver = driver;
                this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
        }

        /**
         * Enters guest email in the email field.
         */
        public void enterGuestEmail(String email) {
                System.out.println("[DEBUG] Entering guest email: " + email);
                WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//input[@id='email-guest']")));
                emailInput.clear();
                emailInput.sendKeys(email);
        }

        /**
         * Clicks the Continue as guest CTA.
         */
        public void clickContinueAsGuest() {
                System.out.println("[DEBUG] Clicking Continue as guest CTA");
                WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//span[normalize-space()='Continue as guest']")));
                continueBtn.click();
        }

        // Add stubs for the following methods, to be implemented as needed
        public void clickPaymentButtonWithRetry() {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
                WebElement paymentButton = wait
                                .until(ExpectedConditions.elementToBeClickable(
                                                By.xpath("//button[@id='shipping-page-button']")));
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", paymentButton);
                try {
                        Thread.sleep(2000); // Shorter wait, just to allow UI to update
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                try {
                        WebDriverWait shortWait = new WebDriverWait(driver, java.time.Duration.ofSeconds(3));
                        WebElement errorModal = shortWait.until(
                                        ExpectedConditions.visibilityOfElementLocated(
                                                        By.cssSelector("div.address-verification__modal")));
                        WebElement addressErrorMessage = driver
                                        .findElement(By.cssSelector("div.address-verification__modal-message"));
                        if (errorModal.isDisplayed()
                                        && addressErrorMessage.getText()
                                                        .contains("Your shipping address looks invalid")) {
                                System.out.println("Shipping address error detected. Clicking payment button again.");
                                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                                paymentButton);
                        }
                } catch (org.openqa.selenium.TimeoutException e) {
                        System.out.println("No shipping address error detected, proceeding.");
                }
        }

        public void verifyDeliveryAddressContains(String expected) {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
                WebElement deliveryAddressField = wait.until(
                                ExpectedConditions.visibilityOfElementLocated(
                                                By.cssSelector(".checkout__card.shipping-summary")));
                String address = deliveryAddressField.getText();
                if (!address.contains(expected)) {
                        throw new AssertionError(
                                        "Delivery address does not contain '" + expected + "' as expected. Actual: "
                                                        + address);
                }
        }

        public void verifyCardSelectedContains(String cardType) {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
                WebElement savedCardDropdown = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.id("stripe-card")));
                org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(
                                savedCardDropdown);
                String cardText = select.getFirstSelectedOption().getText();
                if (!cardText.contains(cardType)) {
                        throw new AssertionError("Selected card does not contain '" + cardType + "' as expected.");
                }
        }

        public void proceedToReview() {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
                WebElement reviewButton = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(
                                                By.xpath("//button[@id='payment-page-button']")));
                reviewButton.click();
        }

        public void placeOrder() {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
                try {
                        System.out.println("[DEBUG] Switching to Intrnl_CO_Container iframe for Place Order CTA...");
                        WebElement iframe = driver.findElement(By.id("Intrnl_CO_Container"));
                        driver.switchTo().frame(iframe);
                        System.out.println("[DEBUG] Switched to Intrnl_CO_Container iframe.");
                        WebElement btnPay = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPay")));
                        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                        btnPay);
                        System.out.println("[DEBUG] JavaScript click succeeded for btnPay CTA.");
                } catch (Exception e) {
                        System.out.println(
                                        "[ERROR] Could not switch to Intrnl_CO_Container iframe or click btnPay CTA: "
                                                        + e.getMessage());
                        throw new RuntimeException(e);
                } finally {
                        driver.switchTo().defaultContent();
                        System.out.println("[DEBUG] Switched back to main context after Place Order CTA.");
                }
        }

        // Method to get the order ID from the confirmation page
        public String getOrderId() {
                WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
                WebElement orderIdElem = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.id("conf-page-order-number")));
                return orderIdElem.getText();
        }
}
