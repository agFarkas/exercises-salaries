package hu.epam.test.exercise;

import hu.epam.test.exercise.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(AppConfig.class)
                .getBean(Application.class)
                .run(args);
    }

}