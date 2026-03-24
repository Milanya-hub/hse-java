package hse.java.lectures.lecture5;

import lombok.*;

@Data
@Builder
public class User {
    private String name, surname, address;
    private int age;
}
