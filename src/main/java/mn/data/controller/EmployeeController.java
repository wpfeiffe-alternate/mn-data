package mn.data.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import mn.data.domain.Employee;
import mn.data.repository.AbstractEmployeeRepository;
import mn.data.repository.EmployeeRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("/employees")
public class EmployeeController
{
    private final EmployeeRepository employeeRepository;
    private final AbstractEmployeeRepository abstractEmployeeRepository;

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

    @Get("/findBy{?firstName,active}")
    public HttpResponse<List<Employee>> getFindBy(@QueryValue Optional<String> firstName, @QueryValue Optional<Boolean> active) {
        if ((firstName.isEmpty() && active.isEmpty()) || (firstName.isPresent() && active.isPresent())) {
            return HttpResponse.badRequest();
        }
        else if (firstName.isPresent()) {
           List<Employee> employees = employeeRepository.findByFirstName(firstName.get());
            if (employees == null) {
                return HttpResponse.notFound();
            }
            return HttpResponse.ok(employees);
        }
        else {
            List<Employee> employees = employeeRepository.findByActive(active.get());
            if (employees == null) {
                return HttpResponse.notFound();
            }
            return HttpResponse.ok(employees);
        }
    }

    @Put("/{id}")
    @Transactional(value = Transactional.TxType.REQUIRED)
    public HttpResponse update(Long id, @Body @Valid Employee employee) {
        if (employee.getId() == null || !employee.getId().equals(id)) {
            return HttpResponse.badRequest();
        }
        Employee origEmployee = employeeRepository.findById(id).orElse(null);
        if (origEmployee == null) {
            return HttpResponse.notFound();
        }
        origEmployee.setTitle(employee.getTitle());
        origEmployee.setFirstName(employee.getFirstName());
        origEmployee.setLastName(employee.getLastName());
        origEmployee.setStartDate(employee.getStartDate());
        origEmployee.setActive(employee.isActive());
        origEmployee = employeeRepository.save(origEmployee);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(origEmployee).getPath());
    }

    @Post("/")
    public HttpResponse create(@Body @Valid Employee employee) {
        assert employee.getId() == null;
        final Employee updated = employeeRepository.save(employee);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(employee).getPath());
    }

    @Delete("/{id}")
    @Transactional(value = Transactional.TxType.REQUIRED)
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
