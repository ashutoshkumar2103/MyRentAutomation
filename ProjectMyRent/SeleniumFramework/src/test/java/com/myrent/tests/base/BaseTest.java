package com.myrent.tests.base;

import com.myrent.framework.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseTest {

    // ThreadLocal ensures each parallel test thread gets its OWN driver
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    protected WebDriverWait wait;

    // ── Get driver for current thread ─────────────────────────────────────────
    protected WebDriver getDriver() {
        return driverThread.get();
    }

    // ── Setup: runs before every @Test ────────────────────────────────────────
    @BeforeMethod
    public void setUp() {
        String browser = ConfigReader.get("browser").toLowerCase();
        WebDriver driver;

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                // options.addArguments("--headless"); // uncomment for CI/CD
                driver = new ChromeDriver(options);
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driverThread.set(driver);

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
        System.out.println("✔ Browser started: " + browser);
    }

    // ── Teardown: runs after every @Test ──────────────────────────────────────
    @AfterMethod
    public void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThread.remove(); // prevent memory leak
            System.out.println("✔ Browser closed");
        }
    }
}