# MyRent Test Automation

Selenium Java + TestNG automation for MyRent web app.

---

## Stack
- Java 11 · Selenium 4.18.1 · TestNG 7.9.0 · Maven · WebDriverManager

---

## Setup

1. Clone the repo
2. Run `mvn install`
3. Update `src/test/resources/config.properties` with your environment

---

## Configuration

```properties
browser=chrome
base.url=https://testee.myrent.it/MyRentWeb
username=administrator
password=admin
company.code=test_en
```

---

## Run Tests

```bash
# All tests
mvn test

# Specific test
mvn test -Dtest=CreateQuotationTest

# Full suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

---

## Test Coverage

| Test | Status |
|---|---|
| Login | ✅ |
| Create Quotation | ✅ |
| Create Reservation | 🚧 |
| Create Rental Agreement | 🚧 |