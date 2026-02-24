package com.myrent.framework.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class DropdownUtils {

    private WebDriver driver;
    private WebDriverWait wait;

    public DropdownUtils(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Wait until dropdown has more than 1 option (i.e. AJAX populated)
    public Select waitForDropdown(By locator) {
        wait.until(d -> {
            List<WebElement> opts = new Select(d.findElement(locator)).getOptions();
            return opts.size() > 1;
        });
        return new Select(driver.findElement(locator));
    }

    // Get all options as a list of strings (text)
    public List<String> getAllOptionTexts(By locator) {
        return waitForDropdown(locator)
                .getOptions()
                .stream()
                .map(WebElement::getText)
                .filter(t -> !t.trim().isEmpty())
                .collect(Collectors.toList());
    }

    // Get all options as value → text map
    public List<String> getAllOptionValues(By locator) {
        return waitForDropdown(locator)
                .getOptions()
                .stream()
                .map(o -> o.getAttribute("value"))
                .filter(v -> v != null && !v.trim().isEmpty())
                .collect(Collectors.toList());
    }

    // Select by value, fallback to first available if not found
    public void selectByValueOrFirst(By locator, String targetValue) {
        Select select = waitForDropdown(locator);
        boolean found = select.getOptions()
                .stream()
                .anyMatch(o -> o.getAttribute("value").equals(targetValue));

        if (found) {
            select.selectByValue(targetValue);
        } else {
            // fallback to first non-empty option
            select.getOptions()
                    .stream()
                    .filter(o -> !o.getAttribute("value").trim().isEmpty())
                    .findFirst()
                    .ifPresent(o -> select.selectByValue(o.getAttribute("value")));
            System.out.println("⚠ Target value [" + targetValue + "] not found. Selected first available.");
        }
    }

    // Print all options to console (for debugging)
    public void printAllOptions(By locator, String dropdownName) {
        System.out.println("── Options in [" + dropdownName + "] ──");
        getAllOptionTexts(locator)
                .forEach(text -> System.out.println("  → " + text));
    }
}