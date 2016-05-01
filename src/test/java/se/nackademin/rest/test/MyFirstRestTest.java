package se.nackademin.rest.test;

import org.junit.Test;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import static com.jayway.restassured.path.json.JsonPath.*;
import se.nackademin.rest.test.model.Book;
import se.nackademin.rest.test.model.SingleBook;

public class MyFirstRestTest {    
    private static final String BASE_URL = "http://localhost:8080/librarytest/rest/";
    
    public MyFirstRestTest() {
    }
    
    @Test    
    public void testFetchBook(){               
       Book book = given().accept(ContentType.JSON).get(BASE_URL+"books/4").jsonPath().getObject("book",Book.class);
       System.out.println(book.getDescription());
       System.out.println(book.getAuthor());        
    }
    @Test
    public void createNewBook(){
        Book book = new Book();
        book.setDescription("Hello  description");
        book.setTitle("title of the book");
        book.setIsbn("isbn12312");
        book.setNumberOfPages(222);
        SingleBook singleBook = new SingleBook(book);
        Response response = given().contentType(ContentType.JSON).body(singleBook).log().all().post(BASE_URL+"books").prettyPeek();
        System.out.println("Status code:"+response.getStatusCode());
        
    }    
  
}