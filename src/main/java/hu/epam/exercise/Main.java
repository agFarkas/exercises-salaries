package hu.epam.exercise;

import hu.epam.exercise.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(AppConfig.class)
                .getBean(Application.class)
                .run(args);
    }

}