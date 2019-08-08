package mn.data.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import mn.data.domain.Department;
import mn.data.repository.AbstractDepartmentRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller("/departments")
public class DepartmentController
{
    private final AbstractDepartmentRepository departmentRepository;

    @Inject
    public DepartmentController(AbstractDepartmentRepository departmentRepository)
    {
        this.departmentRepository = departmentRepository;
    }

    @Get("/")
    List<Department> all() {
        List<Department> departments = new ArrayList<>();
        departmentRepository.findAll().forEach(departments::add);
        return departments;
    }
}
