package pdp.scrabble.game;

import java.util.Random;

public class AI extends Player {

    public AI() {
        super(""); // need JDK 22 to insert a random name here

        String[] firstNames = {
                "John", "Jane", "Alex", "Emily", "Michael", "Sarah", "David", "Laura",
                "Christopher", "Jessica", "Daniel", "Sophia", "Matthew", "Olivia", "James", "Isabella", "Lee", "Ann", "James", "Marie", "Ray", "Lynn", "Michael", "Grace",
                "Joseph", "Kate", "Paul", "Renee", "Scott", "Dawn", "Eve", "Max"
        };

        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson",
                "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin"
        };

        Random random = new Random();
        String firstName = firstNames[random.nextInt(firstNames.length)];
        String lastName = lastNames[random.nextInt(lastNames.length)];

        this.name = firstName + " " + lastName;


    }
}
