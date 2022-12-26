package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
        Configuration.startMaximized = true;
    }

    @Test
    @DisplayName("Should successful plan and notification about reschedule meeting")
    void shouldSuccessfulPlanAndNotificationAboutRescheduleMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $(byText("Запланировать")).click();
        $("[data-test-id=success-notification]").shouldHave(text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15)).shouldBe(visible);
        $("[data-test-id=city] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=city] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=date] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("[data-test-id=name] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=name] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id=phone] input").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $(byText("Запланировать")).click();
        $("[data-test-id=replan-notification]").shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15)).shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id=success-notification]").shouldHave(text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
