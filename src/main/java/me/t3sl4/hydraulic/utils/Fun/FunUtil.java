package me.t3sl4.hydraulic.utils.Fun;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.Random;

public class FunUtil {
    private static final Random RANDOM = new Random();

    public static Circle createSnowflake(double width, double height) {
        Circle snowflake = new Circle(2 + RANDOM.nextInt(4)); // Kar tanesi boyutu
        snowflake.setFill(Color.WHITE);

        // X ekseni rastgele, Y ekseni ekranın biraz yukarısından başlasın
        snowflake.setLayoutX(RANDOM.nextDouble() * width);
        snowflake.setLayoutY(-RANDOM.nextDouble() * height / 2); // Ekran üst sınırının biraz üzerinde rastgele başlar
        return snowflake;
    }

    public static void animateSnowflake(Circle snowflake, double height, double width) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(snowflake.layoutYProperty(), snowflake.getLayoutY()),
                        new KeyValue(snowflake.layoutXProperty(), snowflake.getLayoutX())
                ),
                new KeyFrame(
                        Duration.seconds(5 + RANDOM.nextInt(5)), // 5-10 saniyelik düşüş süresi
                        new KeyValue(snowflake.layoutYProperty(), height - 10), // Zemine kadar iner
                        new KeyValue(snowflake.layoutXProperty(), snowflake.getLayoutX() + RANDOM.nextDouble() * 100 - 50) // Hafif sağa veya sola kayma
                )
        );

        timeline.setOnFinished(e -> {
            // Yeni rastgele başlangıç pozisyonu
            snowflake.setLayoutY(-RANDOM.nextDouble() * height / 2); // Yine üst sınırın biraz üzerinde rastgele başlar
            snowflake.setLayoutX(RANDOM.nextDouble() * width);
            animateSnowflake(snowflake, height, width); // Animasyonu tekrar başlat
        });

        timeline.play();
    }
}