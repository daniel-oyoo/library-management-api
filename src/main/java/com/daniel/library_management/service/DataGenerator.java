package com.daniel.library_management.service;

import com.daniel.library_management.model.Book;
import com.daniel.library_management.model.Member;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Utility class for generating random test data.
 * 
 * <p>This class generates large volumes of realistic book and member
 * data for performance testing and demonstration purposes.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Component
public class DataGenerator {
    
    private static final Faker faker = new Faker(new Locale("en"));
    
    // Predefined book titles and authors for variety
    private static final String[] GENRES = {
        "Fiction", "Non-Fiction", "Science", "History", "Biography",
        "Technology", "Art", "Philosophy", "Psychology", "Business"
    };
    
    private static final String[] FIRST_NAMES = {
        "James", "Mary", "John", "Patricia", "Robert", "Jennifer",
        "Michael", "Linda", "William", "Elizabeth", "David", "Susan"
    };
    
    private static final String[] LAST_NAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia",
        "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Wilson"
    };
    
    /**
     * Generates a list of random books.
     * 
     * @param count Number of books to generate
     * @return List of generated books
     */
    public static List<Book> generateBooks(int count) {
        List<Book> books = new ArrayList<>(count);
        
        for (int i = 0; i < count; i++) {
            Book book = Book.builder()
                .id(UUID.randomUUID().toString())
                .title(generateTitle())
                .author(generateAuthor())
                .isbn(generateISBN())
                .publicationYear(generateYear())
                .available(true)
                .addedDate(LocalDate.now().minusDays(faker.random().nextInt(1, 365)))
                .build();
            books.add(book);
        }
        
        return books;
    }
    
    /**
     * Generates a list of random members.
     * 
     * @param count Number of members to generate
     * @return List of generated members
     */
    public static List<Member> generateMembers(int count) {
        List<Member> members = new ArrayList<>(count);
        
        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[faker.random().nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[faker.random().nextInt(LAST_NAMES.length)];
            String name = firstName + " " + lastName;
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + 
                          faker.random().nextInt(1, 9999) + "@example.com";
            
            Member member = Member.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .email(email)
                .membershipId(generateMembershipId())
                .phoneNumber(faker.phoneNumber().cellPhone())
                .joinDate(LocalDate.now().minusDays(faker.random().nextInt(1, 730)))
                .active(true)
                .build();
            members.add(member);
        }
        
        return members;
    }
    
    /**
     * Generates a random book title.
     * 
     * @return Random title
     */
    private static String generateTitle() {
        String genre = GENRES[faker.random().nextInt(GENRES.length)];
        String topic = faker.book().title();
        return genre + ": " + topic;
    }
    
    /**
     * Generates a random author name.
     * 
     * @return Random author name
     */
    private static String generateAuthor() {
        String firstName = FIRST_NAMES[faker.random().nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[faker.random().nextInt(LAST_NAMES.length)];
        return firstName + " " + lastName;
    }
    
    /**
     * Generates a random ISBN.
     * 
     * @return Random ISBN-13
     */
    private static String generateISBN() {
        return String.format("978-%03d-%03d-%03d-%d",
            faker.random().nextInt(100, 999),
            faker.random().nextInt(100, 999),
            faker.random().nextInt(100, 999),
            faker.random().nextInt(0, 9));
    }
    
    /**
     * Generates a random publication year.
     * 
     * @return Year between 1900 and current year
     */
    private static int generateYear() {
        int currentYear = Year.now().getValue();
        return faker.random().nextInt(1900, currentYear);
    }
    
    /**
     * Generates a random membership ID.
     * 
     * @return Random membership ID
     */
    private static String generateMembershipId() {
        String year = String.valueOf(Year.now().getValue());
        String sequence = String.format("%05d", faker.random().nextInt(1, 99999));
        return "LIB-" + year + "-" + sequence;
    }
    
    /**
     * Generates books with realistic data for testing.
     * 
     * @param count Number of books to generate
     * @return List of generated books
     */
    public static List<Book> generateRealisticBooks(int count) {
        List<Book> books = new ArrayList<>(count);
        
        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.setId(UUID.randomUUID().toString());
            book.setTitle(faker.book().title());
            book.setAuthor(faker.book().author());
            book.setIsbn(faker.code().isbn13());
            book.setPublicationYear(faker.number().numberBetween(1900, 2024));
            book.setAvailable(true);
            book.setAddedDate(LocalDate.now().minusDays(faker.number().numberBetween(1, 365)));
            books.add(book);
        }
        
        return books;
    }
}