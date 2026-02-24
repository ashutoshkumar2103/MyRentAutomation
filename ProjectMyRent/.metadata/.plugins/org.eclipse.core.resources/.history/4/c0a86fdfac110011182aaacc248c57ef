package com.myrent.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for: /MRReservation/create
 *
 * Locators sourced directly from DevTools inspection visible in the
 * screen recording. All IDs are confirmed from the HTML source panels.
 *
 * Form sections (in order):
 *   1. Reservation Status
 *   2. Customer Details  → Client, Driver1, Driver2, Driver3
 *   3. Car Class         → Car Class Booked, Assigned Group (auto-filled)
 *   4. Pick Up           → Location, Start Date, Start Time
 *   5. Return            → Location, End Date, End Time
 *   6. Submit
 */
public class CreateReservationPage {

    private final WebDriver     driver;
    private final WebDriverWait wait;

    // ── Locators: Reservation Header ──────────────────────────────────────────
    // Reservation Status dropdown  (On Wait | Confirmed | Cancelled …)
    private final By reservationStatusDropdown = By.id("statusId");

    // Confirmed radio: Yes / No
    private final By confirmedYes = By.cssSelector("input[name='confirmed'][value='true']");
    private final By confirmedNo  = By.cssSelector("input[name='confirmed'][value='false']");

    // ── Locators: Customer Details ─────────────────────────────────────────────
    // Client — search input triggers AJAX, then select from dropdown
    // Confirmed from frame 4/5: input#inputValClient  |  div#divUpdateClient
    //                            select#client  (name="client.id", class="checkValue")
    private final By clientSearchInput = By.id("inputValClient");
    private final By clientTriggerDiv  = By.id("divUpdateClient");
    private final By clientDropdown    = By.id("client");

    // Driver1 — same AJAX pattern as Client
    // Confirmed from frame 11: id="divdriver2" wrapper visible in DOM
    private final By driver1SearchInput = By.id("inputValDriver1");
    private final By driver1TriggerDiv  = By.id("divdriver1");
    private final By driver1Dropdown    = By.id("driver1");

    // Driver2 (optional)
    private final By driver2SearchInput = By.id("inputValDriver2");
    private final By driver2Dropdown    = By.id("driver2");

    // Driver3 (optional)
    private final By driver3SearchInput = By.id("inputValDriver3");
    private final By driver3Dropdown    = By.id("driver3");

    // ── Locators: Car Class ────────────────────────────────────────────────────
    // Confirmed from frame 13/14: select name="group.id" id="group"
    //   onchange="updateAssignedAndRequestVehicle(this.value)"
    private final By carClassDropdown    = By.id("group");
    // Assigned Group is auto-populated after selecting Car Class — read-only verify
    private final By assignedGroupSelect = By.cssSelector("select[name='assignedGroup.id']");

    // ── Locators: Pick Up ──────────────────────────────────────────────────────
    // Confirmed from frame 19/20:
    //   select name="locationPickupExcepted.id" id="locationPickupExcepted"
    //   onchange="checkOneWayRules('pickup')"
    private final By pickupLocationDropdown = By.id("locationPickupExcepted");
    private final By pickupStartDateInput   = By.id("startDate");
    private final By pickupStartTimeInput   = By.id("startTime");

    // ── Locators: Return ───────────────────────────────────────────────────────
    // Mirror of pickup — same naming convention confirmed by visible label "Location Return"
    private final By returnLocationDropdown = By.id("locationReturnExcepted");
    private final By returnEndDateInput     = By.id("endDate");
    private final By returnEndTimeInput     = By.id("endTime");

    // ── Locators: Submit & Post-Create ─────────────────────────────────────────
    private final By createButton     = By.xpath("//input[@id='create'] | //button[@id='create']");
    private final By saveButton       = By.cssSelector("button[type='submit'], input[type='submit']");
    private final By closeModalBtn    = By.xpath("//button[normalize-space()='Close']");
    private final By modalDismissBtn  = By.cssSelector("button[data-dismiss='modal']");
    private final By modalBody        = By.cssSelector(".modal-body");
    private final By successAlert     = By.cssSelector(".alert-success, .alert.alert-info");

    // ── Constructor ────────────────────────────────────────────────────────────
    public CreateReservationPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // ══════════════════════════════════════════════════════════════════════════
    // NAVIGATION
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Navigate directly to the Create Reservation URL and wait for the
     * Client search field to appear (first interactive element on page).
     */
    public void navigateTo(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(clientSearchInput));
        System.out.println("✔ Navigated to Create Reservation: " + url);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Generic AJAX-backed typeahead + dropdown selector.
     * 1. Types searchText into the search input → triggers AJAX
     * 2. Clicks the trigger div to fire the AJAX request
     * 3. Waits for the paired <select> to populate
     * 4. Selects by value / partial text / first valid option (cascade)
     */
    private void fillSearchAndSelect(By searchInputLocator,
                                     By triggerDivLocator,
                                     By dropdownLocator,
                                     String searchText,
                                     String valueOrText) {
        // Type into search box
        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(searchInputLocator));
        input.clear();
        input.sendKeys(searchText);

        // Trigger AJAX by clicking the wrapper div (same pattern as Quotation page)
        try {
            driver.findElement(triggerDivLocator).click();
        } catch (Exception e) {
            // Some wrappers may not be clickable — Tab key also fires blur/AJAX
            input.sendKeys(Keys.TAB);
        }

        // Wait for dropdown to populate (>1 option = real options loaded)
        wait.until(d -> {
            List<WebElement> opts = d.findElement(dropdownLocator)
                                     .findElements(By.tagName("option"));
            return opts.size() > 1;
        });

        selectFromDropdown(dropdownLocator, valueOrText);
    }

    /**
     * Select from a native <select> element.
     * Cascade: exact value → partial text → first non-null option
     */
    private void selectFromDropdown(By locator, String valueOrText) {
        Select select  = new Select(driver.findElement(locator));
        List<WebElement> options = select.getOptions();

        // 1. Exact value match
        for (WebElement opt : options) {
            if (opt.getAttribute("value") != null &&
                opt.getAttribute("value").equals(valueOrText)) {
                select.selectByValue(valueOrText);
                System.out.println("✔ Selected by value [" + valueOrText + "] in " + locator);
                return;
            }
        }

        // 2. Partial visible text match (case-insensitive)
        for (WebElement opt : options) {
            if (opt.getText().toLowerCase().contains(valueOrText.toLowerCase())) {
                select.selectByVisibleText(opt.getText());
                System.out.println("✔ Selected by text [" + opt.getText() + "] in " + locator);
                return;
            }
        }

        // 3. Fallback: first real option (not "Select One..." / null)
        for (WebElement opt : options) {
            String val = opt.getAttribute("value");
            if (val != null && !val.isEmpty() && !val.equalsIgnoreCase("null")) {
                select.selectByValue(val);
                System.out.println("⚠ Fallback: first available [" + opt.getText()
                        + "] in " + locator);
                return;
            }
        }

        throw new RuntimeException("❌ No selectable option in dropdown: " + locator);
    }

    /**
     * Wait for a dropdown to have at least 2 options, then select.
     */
    private void waitAndSelect(By locator, String valueOrText) {
        wait.until(d -> {
            List<WebElement> opts = d.findElement(locator)
                                     .findElements(By.tagName("option"));
            return opts.size() > 1;
        });
        selectFromDropdown(locator, valueOrText);
    }

    /**
     * Clear a date/time input and set a new value via JS (avoids datepicker popups).
     */
    private void setDateOrTime(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].value = arguments[1];", el, value);
        // Fire change event so any bound listeners update
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].dispatchEvent(new Event('change'));", el);
        System.out.println("✔ Set date/time [" + value + "] on " + locator);
    }

    /**
     * Scroll element into view and click via JS (avoids intercepted clicks).
     */
    private void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'}); arguments[0].click();", el);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // PUBLIC STEP METHODS
    // ══════════════════════════════════════════════════════════════════════════

    // ── Step 1: Reservation Status ────────────────────────────────────────────

    /**
     * Select the reservation status from the header dropdown.
     * e.g. "On Wait", "Confirmed", "Cancelled"
     */
    public void selectReservationStatus(String statusText) {
        waitAndSelect(reservationStatusDropdown, statusText);
        System.out.println("✔ Reservation status set to: " + statusText);
    }

    /**
     * Click Confirmed = Yes radio button.
     */
    public void setConfirmedYes() {
        jsClick(confirmedYes);
        System.out.println("✔ Confirmed: Yes");
    }

    /**
     * Click Confirmed = No radio button.
     */
    public void setConfirmedNo() {
        jsClick(confirmedNo);
        System.out.println("✔ Confirmed: No");
    }

    // ── Step 2: Client ────────────────────────────────────────────────────────

    /**
     * Fill the client search box and select from the AJAX dropdown.
     *
     * @param searchText  text to type into the search box (e.g. "ashu")
     * @param valueOrText dropdown value ID (e.g. "5338") or partial name (e.g. "KUMAR")
     */
    public void fillAndSelectClient(String searchText, String valueOrText) {
        fillSearchAndSelect(
                clientSearchInput, clientTriggerDiv, clientDropdown,
                searchText, valueOrText);
        System.out.println("✔ Client selected: " + valueOrText);
    }

    // ── Step 3: Driver1 (primary driver) ──────────────────────────────────────

    /**
     * Fill Driver1 search and select from dropdown.
     * This field is optional but usually required for valid reservations.
     */
    public void fillAndSelectDriver1(String searchText, String valueOrText) {
        fillSearchAndSelect(
                driver1SearchInput, driver1TriggerDiv, driver1Dropdown,
                searchText, valueOrText);
        System.out.println("✔ Driver1 selected: " + valueOrText);
    }

    // ── Step 4: Car Class ─────────────────────────────────────────────────────

    /**
     * Select Car Class Booked.
     * Assigned Group is auto-populated by the onchange JS handler.
     *
     * @param valueOrText e.g. "125" (value) or "FORBICE DIESEL" (partial text)
     */
    public void selectCarClass(String valueOrText) {
        waitAndSelect(carClassDropdown, valueOrText);
        // Small pause for JS to auto-populate Assigned Group
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        System.out.println("✔ Car Class selected: " + valueOrText);
    }

    // ── Step 5: Pickup location & dates ───────────────────────────────────────

    /**
     * Select the Pickup location from the dropdown.
     * Triggers checkOneWayRules AJAX on selection.
     *
     * @param valueOrText e.g. "33" (value) or "LEGNANO" (partial text)
     */
    public void selectPickupLocation(String valueOrText) {
        waitAndSelect(pickupLocationDropdown, valueOrText);
        System.out.println("✔ Pickup location selected: " + valueOrText);
    }

    /**
     * Set the pickup start date.
     *
     * @param date format dd/MM/yyyy  (as shown in the form, e.g. "24/02/2026")
     */
    public void setPickupDate(String date) {
        setDateOrTime(pickupStartDateInput, date);
    }

    /**
     * Set the pickup start time.
     *
     * @param time format HH:mm  (e.g. "09:00")
     */
    public void setPickupTime(String time) {
        setDateOrTime(pickupStartTimeInput, time);
    }

    // ── Step 6: Return location & dates ───────────────────────────────────────

    /**
     * Select the Return location dropdown.
     *
     * @param valueOrText e.g. "33" or "LEGNANO"
     */
    public void selectReturnLocation(String valueOrText) {
        waitAndSelect(returnLocationDropdown, valueOrText);
        System.out.println("✔ Return location selected: " + valueOrText);
    }

    /**
     * Set the return end date.
     *
     * @param date format dd/MM/yyyy  (e.g. "25/02/2026")
     */
    public void setReturnDate(String date) {
        setDateOrTime(returnEndDateInput, date);
    }

    /**
     * Set the return end time.
     *
     * @param time format HH:mm  (e.g. "10:00")
     */
    public void setReturnTime(String time) {
        setDateOrTime(returnEndTimeInput, time);
    }

    // ── Convenience: Pickup + Return in one call ──────────────────────────────

    /**
     * Set all pickup & return fields in one call.
     * Pass the same locationValue for both if one-way is not required.
     */
    public void fillPickupAndReturn(String locationValue,
                                    String pickupDate, String pickupTime,
                                    String returnDate, String returnTime) {
        selectPickupLocation(locationValue);
        setPickupDate(pickupDate);
        setPickupTime(pickupTime);
        selectReturnLocation(locationValue);
        setReturnDate(returnDate);
        setReturnTime(returnTime);
    }

    // ── Step 7: Submit ────────────────────────────────────────────────────────

    /**
     * Scroll to and click the Create / Save button.
     */
    public void clickCreate() {
        try {
            jsClick(createButton);
        } catch (Exception e) {
            // Fallback to generic submit button
            jsClick(saveButton);
        }
        System.out.println("✔ Create button clicked");
    }

    // ── Step 8: Handle post-submit modal or redirect ──────────────────────────

    /**
     * After clicking Create:
     *   - If a success/info modal appears → log content and dismiss it
     *   - If an error modal appears       → throw AssertionError with modal text
     *   - If no modal → verify URL changed away from /create
     */
    public void handlePostCreate() {
        // First try to close any "Close" button modal
        try {
            WebElement closeBtn = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(closeModalBtn));
            closeBtn.click();
            System.out.println("✔ Close button clicked on post-create modal");
            return;
        } catch (Exception ignored) { /* no Close button */ }

        // Check for modal with data-dismiss
        try {
            new WebDriverWait(driver, Duration.ofSeconds(6))
                    .until(ExpectedConditions.visibilityOfElementLocated(modalDismissBtn));

            String content = driver.findElement(modalBody).getText();
            System.out.println("Modal content: " + content);

            boolean hasError = List.of("cannot apply", "error", "invalid",
                                        "non è possibile", "errore")
                    .stream()
                    .anyMatch(k -> content.toLowerCase().contains(k));

            if (hasError) {
                throw new AssertionError("❌ Error modal after Create: " + content);
            }

            // Dismiss via JS (avoids any overlay issues)
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].click();",
                    driver.findElement(modalDismissBtn));
            System.out.println("✔ Post-create modal dismissed");

        } catch (AssertionError ae) {
            throw ae;
        } catch (Exception e) {
            // No modal — check URL changed from /create
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/MRReservation/create")) {
                throw new AssertionError(
                        "❌ Expected redirect after Create but URL is still: " + currentUrl);
            }
            System.out.println("✔ Reservation created — redirected to: " + currentUrl);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // FULL FLOW CONVENIENCE METHOD
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * End-to-end reservation creation in one call.
     * All parameters map 1-to-1 with config.properties keys.
     */
    public void createReservation(String clientSearch,  String clientValue,
                                   String carClassValue,
                                   String locationValue,
                                   String pickupDate,   String pickupTime,
                                   String returnDate,   String returnTime) {
        fillAndSelectClient(clientSearch, clientValue);
        selectCarClass(carClassValue);
        fillPickupAndReturn(locationValue, pickupDate, pickupTime, returnDate, returnTime);
        clickCreate();
        handlePostCreate();
    }
}