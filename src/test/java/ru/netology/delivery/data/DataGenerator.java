package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        // Генерируем дату: сегодня + shift дней, формат dd.MM.yyyy
        String date = LocalDate.now()
                .plusDays(shift)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return date;
    }

    public static String generateCity(Faker faker) {
        // Можно использовать Faker напрямую, но валидные города — только из списка
        String[] validCities = {
                "Москва", "Санкт-Петербург", "Казань",
                "Уфа", "Самара", "Новосибирск", "Екатеринбург"
        };
        String city = validCities[new Random().nextInt(validCities.length)];
        return city;
    }

    public static String generateName(Faker faker) {
        // Faker в русской локали генерирует реальные ФИО
        String name = faker.name().lastName() + " " + faker.name().firstName();
        return name;
    }

    public static String generatePhone(Faker faker) {
        // Корректный российский телефон
        String phone = faker.phoneNumber().phoneNumber();
        return phone;
    }

    public static class Registration {
        private static Faker faker;

        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            faker = new Faker(new Locale(locale));

            String city = generateCity(faker);
            String name = generateName(faker);
            String phone = generatePhone(faker);

            UserInfo user = new UserInfo(city, name, phone);
            return user;
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}

