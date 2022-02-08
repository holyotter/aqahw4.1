package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class TestOrderForm {

    int days = 3;

    @BeforeEach
    void shouldPrepare(){
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @AfterEach
    void shouldClose() {
        closeWindow();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    public void shouldAcceptedAndRescheduled() {
        $("[data-test-id=city] input").setValue(TestWithFaker.cityFake());
        $("[data-test-id=date] [placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] [placeholder='Дата встречи']").setValue(DateMeeting.dateInput(days));
        $("[data-test-id=name] [name='name']").setValue(TestWithFaker.nameFake());
        $("[data-test-id=phone] [name='phone']").setValue(TestWithFaker.phoneFake());
        $("[class=checkbox__box]").click();
        $(withText("Запланировать")).click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(3))
                .shouldHave(exactText("Встреча успешно запланирована на " + DateMeeting.dateInput(days)));
        days = days + 2;
        $("[data-test-id=date] [placeholder='Дата встречи']").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] [placeholder='Дата встречи']").setValue(DateMeeting.dateInput(days));
        $(withText("Запланировать")).click();
        $(withText("Перепланировать?")).shouldBe(visible, Duration.ofSeconds(3));
        $$("button").find(exactText("Перепланировать")).click();
        $(".notification__content").shouldBe(visible, Duration.ofSeconds(3))
                .shouldHave(exactText("Встреча успешно запланирована на " + DateMeeting.dateInput(days)));
    }
}
