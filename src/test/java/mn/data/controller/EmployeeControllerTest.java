package mn.data.controller;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MicronautTest;
import mn.data.domain.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

@MicronautTest
public class EmployeeControllerTest {
    @Inject
    @Client("/employees")
    HttpClient client;

    @Test
    void testGet()
    {
        HttpResponse<List<Employee>> response = client.toBlocking().exchange(HttpRequest.GET("/"), Argument.listOf(Employee.class));
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK);
        Assertions.assertTrue(response.getBody().isPresent());
        Assertions.assertEquals(response.getBody().get().size(), 250);
    }

    @Test
    void testGetByActive()
    {
        HttpResponse<List<Employee>> response = client.toBlocking().exchange(HttpRequest.GET("/findBy?active=true"), Argument.listOf(Employee.class));
        Assertions.assertEquals(response.getStatus(), HttpStatus.OK);
        Assertions.assertTrue(response.getBody().isPresent());
        Assertions.assertEquals(response.getBody().get().size(), 185);
    }
}
