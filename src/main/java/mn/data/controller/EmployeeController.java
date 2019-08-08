package mn.data.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import mn.data.domain.Employee;
import mn.data.repository.AbstractEmployeeRepository;
import mn.data.repository.EmployeeRepository;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller("/employees")
public class EmployeeController
{
    private final EmployeeRepository employeeRepository;
    private final AbstractEmployeeRepository abstractEmployeeRepository;

    @Inject
    public EmployeeController(AbstractEmployeeRepository abstractEmployeeRepository, EmployeeRepository employeeRepository)
    {
        this.abstractEmployeeRepository = abstractEmployeeRepository;
        this.employeeRepository = employeeRepository;
    }

    @Get("/")
    public List<Employee> all() {
        return (ArrayList<Employee>) employeeRepository.findAll();
    }

    @Get("/{id}")
    public HttpResponse<Employee> getById(Long id) {
        Employee found = employeeRepository.findById(id).orElse(null);
        if (found == null) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(found);
    }

    @Put("/")
    public HttpResponse create(@Body @Valid Employee employee) {
        Employee origEmployee = abstractEmployeeRepository.update(employee);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(employee).getPath());
    }

    @Post("/")
    public HttpResponse update(@Body @Valid Employee employee) {
        assert employee.getId() == null;
        final Employee updated = employeeRepository.save(employee);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(employee).getPath());
    }

    @Delete("/{id}")
    public HttpResponse<Employee> delete(Long id) {
        Employee found = employeeRepository.findById(id).orElse(null);
        if (found == null) {
            return HttpResponse.notFound();
        }
        employeeRepository.delete(found);
        return HttpResponse.ok(found);
    }

    private URI toUri(Employee employee) {
        return URI.create("/employee/" + employee.getId());
    }


}
