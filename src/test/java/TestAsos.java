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

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Desktop\\drivers\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.get("https://www.asos.com/ru/");

    }

    @Test
    public void testTool() {

        Actions actions = new Actions(driver);

        WebElement element = (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions
                .presenceOfElementLocated(
                        By.xpath("//*[@id='globalBannerComponent']/div/div/div/button")));
        actions.moveToElement(element).perform();

        System.out.println(driver.findElement(By.xpath("//*[@id='globalBannerComponent']/div/div/div/div")).getText());

    }

    @Test
    public void testSort() throws InterruptedException {

        driver.findElement(By.xpath("//*[@id='chrome-app-container']/section/div/div/div[1]/div[2]/div[2]/a")).click();
        driver.findElement(By.xpath("//*[@id='chrome-app-container']/section[3]/ul/li[1]/a")).click();

        /*Сортировка по типу товара*/
        driver.findElement(By.xpath("//*[@id='plp']/div/div/div[1]/div/div/div/div[1]/ul/li[3]/div/button")).click();
        driver.findElement(By.xpath("//*[@id='plp']/div/div/div[1]/div/div/div/div[1]/ul/li[3]/div/div/div/ul/li[4]")).click();
        Thread.sleep(10000);

        /*Сортировка цены по возрастанию*/
        driver.findElement(By.xpath("//*[@id='plp']/div/div/div[1]/div/div/div/div[1]/ul/li[1]/div/button")).click();
        driver.findElement(By.xpath("//*[@id='plp_web_sort_price_low_to_high']")).click();
        Thread.sleep(10000);

        /*Собрал эелменты со страницы*/
        List<WebElement> list = driver.findElements(By.xpath("/html/body/div[1]/div/main/div/div/div/div[2]/div/div[1]/section/article/a/p/span[1]/span"));

        /*Убрал нули и "руб."*/
        List<String> list2 = list.stream().map(WebElement::getText).collect(Collectors.toList());
        list2 = list2.stream().map(s -> s.substring(0, s.indexOf(","))).collect(Collectors.toList());

        /*Закастил к листу интов*/
        List<Integer> sortedList = new ArrayList<>(list2.size());
        list2.forEach(i -> sortedList.add(Integer.parseInt(i.replaceAll(" ", ""))));

        boolean flag = IntStream.range(0, sortedList.size() - 1).allMatch(i -> sortedList.get(i) <= sortedList.get(i + 1));

        if (flag) {
            System.out.println("Соритровка отлично, сортировка all right");
        }
        else {
            System.out.println("Сортировка не отлично, сортировка не отлично сортировка не all right");
        }

    }

    @Test
    public void invalidRegistrationTest() {

        driver.findElement(By.xpath("//button[@class='_6iPIuvw _2SSHFPv']")).click();
        driver.findElement(By.xpath("//span[@class='k3_c4ux']/a[@data-testid='signup-link']")).click();

        driver.findElement(By.xpath("//input[@class='qa-email-textbox']")).sendKeys("oleg.senderov@mail.ru");
        driver.findElement(By.xpath("//input[@class='qa-firstname-textbox']")).sendKeys("Олег");
        driver.findElement(By.xpath("//input[@class='qa-lastname-textbox']")).sendKeys("Сендеров");
        driver.findElement(By.xpath("//input[@class='qa-password-textbox']")).sendKeys("qwerty123456");
        driver.findElement(By.xpath("//select[@class='qa-birthday-textbox']/option[@value='1']")).click();
        driver.findElement(By.xpath("//input[@type='submit']")).click();

        Assert.assertEquals("Дата введена не полностью", "День/Число/Месяц");

    }

}
