package se.nackademin.rest.test;

import org.junit.Test;
import static com.jayway.restassured.RestAssured.*;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.util.UUID;
import static org.junit.Assert.assertEquals;
import se.nackademin.rest.test.BookOperations;
import static com.jayway.restassured.path.json.JsonPath.*;

public class MyFirstRestTest {    
    private static final String BASE_URL = "http://localhost:8080/librarytest/rest/";
    
    public MyFirstRestTest() {
    }
    
    @Test
    public void testFetchBook(){               
        Response response = new BookOperations().getBook(3);
        System.out.println("Status code: "+ response.statusCode()); //printing the statuscode.
        assertEquals("Status code should be 200",200, response.statusCode());
        
    }
    
    @Test
    public void testFetchInvalidBookReturns404(){        
        Response response = new BookOperations().getBook(293872);
        System.out.println("Status code: "+ response.statusCode()); //printing the statuscode.
        assertEquals("Status code should be 404",404, response.statusCode());
        
    }
    
    @Test
    public void testAddNewBook(){
        BookOperations bookOperations = new BookOperations();
        Response postResponse = bookOperations.createRandomBook();
        assertEquals("Post response should have status code 201",201,postResponse.statusCode());
        
        Response getResponse = bookOperations.getAllBooks();
        
        String expectedTitle = from(bookOperations.getJsonString()).getString("book.title");
        String expectedDescription = from(bookOperations.getJsonString()).getString("book.description");
        String expectedIsbn = from(bookOperations.getJsonString()).getString("book.isbn");
        
        
        String fetchedTitle = getResponse.jsonPath().getString("books.book[-1].title");
        String fetchedDescription = getResponse.jsonPath().getString("books.book[-1].description");
        String fetchedIsbn = getResponse.jsonPath().getString("books.book[-1].isbn");      
        
        assertEquals (expectedTitle, fetchedTitle);
        assertEquals (expectedDescription, fetchedDescription);
        assertEquals (expectedIsbn, fetchedIsbn);
    }
    
    @Test
    public void testDeleteABook(){
        Response postResponse = new BookOperations().createRandomBook();        
        assertEquals("Post response should have status code 201",201,postResponse.statusCode());        
        
        Response getResponse = new BookOperations().getAllBooks();
        int fetchedId = getResponse.jsonPath().getInt("books.book[-1].id");
        
        Response deleteResponse = new BookOperations().deleteBook(fetchedId);
        assertEquals("Delete method should return 204",204,deleteResponse.statusCode());
            
        Response getDeletedBookResponse = new BookOperations().getBook(fetchedId);
        assertEquals("Fetching deleted book should return 404",404,getDeletedBookResponse.statusCode());
    }
  
}