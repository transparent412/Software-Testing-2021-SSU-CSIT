import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestAsos {

    private WebDriver driver;

    @Before
    public void setUpConnection() {

        System.setProperty("webdriver.chrome.driver", "./resources/chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.asos.com/ru/");

    }

    @Test
    public void testTool() {

        Actions actions = new Actions(driver);

        WebElement element = (new WebDriverWait(driver, Duration.ofSeconds(20))).until(ExpectedConditions
                .presenceOfElementLocated(
                        By.xpath("//*[contains(@class,'src-GlobalBanner-Unit-Unit_unitLink src-GlobalBanner-Unit-Unit_unitRight')]")));
        actions.moveToElement(element).perform();
        Assert.assertEquals("Заканчивается в 11:00 МСК 20.12.2021. Акция действует, пока товар есть в наличии. Распространяется на вещи, помеченные на сайте."
                , driver.findElement(By.xpath("//*[contains(@class,'src-GlobalBanner-Tooltip-Tooltip_wrapperRight') and not(contains(@class,'src-GlobalBanner-Tooltip-Tooltip_hidden'))]/..//*[@class='src-GlobalBanner-Tooltip-Tooltip_tooltip']")).getText());
    }

    @Test
    public void testSort() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.findElement(By.xpath("//a[@class='mu__cta']/span[text()='МУЖСКОЕ']")).click();
        driver.findElement(By.xpath("//a[@data-analytics-id='ru-mwgblexcau-winteraccessories-mulink']")).click();

        driver.findElement(By.xpath("//div[text()='Тип продукта']")).click();
        driver.findElement(By.xpath("//div[text()='Перчатки']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(@data-auto-id,'productListSpinner')]")));

        driver.findElement(By.xpath("//li[@data-auto-id='sort']")).click();
        driver.findElement(By.xpath("//li[@id='plp_web_sort_price_low_to_high']")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains(@data-auto-id,'productListSpinner')]")));

        List<WebElement> list = driver.findElements(By.xpath("//*[@data-auto-id='productTilePrice']/../span[last()]"));

        List<String> list2 = list.stream().map(WebElement::getText).collect(Collectors.toList());
        list2 = list2.stream().map(s -> s.substring(0, s.indexOf(","))).collect(Collectors.toList());

        List<Integer> sortedList = new ArrayList<>(list2.size());
        list2.forEach(i -> sortedList.add(Integer.parseInt(i.replaceAll(" ", ""))));

        boolean flag = IntStream.range(0, sortedList.size() - 1).allMatch(i -> sortedList.get(i) <= sortedList.get(i + 1));

        Assert.assertTrue(flag, "Сортировка не отлично, сортировка не all right");

    }

    @Test
    public void invalidRegistrationTest() {

        driver.findElement(By.xpath("//button[@data-testid='myAccountIcon']")).click();
        driver.findElement(By.xpath("//a[@data-testid='signup-link']")).click();

        driver.findElement(By.xpath("//input[@class='qa-email-textbox']")).sendKeys("");
        driver.findElement(By.xpath("//input[@class='qa-firstname-textbox']")).sendKeys("Олег");
        driver.findElement(By.xpath("//input[@class='qa-lastname-textbox']")).sendKeys("Сендеров");
        driver.findElement(By.xpath("//input[@class='qa-password-textbox']")).sendKeys("qwerty123456");
        driver.findElement(By.xpath("//select[@class='qa-birthday-textbox']/option[@value='1']")).click();
        driver.findElement(By.xpath("//select[@class='qa-birthmonth-textbox']/option[@value='3']")).click();
        driver.findElement(By.xpath("//select[@class='qa-birthyear-textbox']/option[@value='2005']")).click();
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        String message = driver.findElement(By.xpath("//*[@id='Email-error']")).getText();
        Assert.assertTrue(message.isEmpty(), "Пустое обязательное поле Email");

    }

}
