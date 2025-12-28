package ru.netology.delivery.test;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class DeliveryTest {

    @BeforeAll
    static void setupALL() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {

        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        // --- Первый запрос: планирование встречи ---
        $("[data-test-id=city] input").setValue(validUser.getCity());

        // очищаем автозаполненную дату и вводим первую дату
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(Keys.BACK_SPACE);
        dateInput.setValue(firstMeetingDate);

        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();

        // нажимаем кнопку "Запланировать"
        $(Selectors.byText("Запланировать")).click();

        // проверяем успешное сообщение с первой датой
        $("[data-test-id=success-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate));

        // --- Второй запрос: изменение даты и перепланирование ---
        dateInput.doubleClick().sendKeys(Keys.BACK_SPACE);
        dateInput.setValue(secondMeetingDate);

        // снова отправляем форму
        $(Selectors.byText("Запланировать")).click();

        // проверяем уведомление о необходимости перепланировать
        $("[data-test-id=replan-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("У вас уже запланирована встреча на другую дату"));

        // жмём "Перепланировать"
        $("[data-test-id=replan-notification]")
                .$(Selectors.byText("Перепланировать"))
                .click();

        // проверяем успешное сообщение уже на вторую дату
        $("[data-test-id=success-notification]")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}
