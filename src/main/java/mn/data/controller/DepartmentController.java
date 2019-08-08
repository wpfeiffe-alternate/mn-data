package mn.data.controller;

import com.sun.istack.Nullable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import mn.data.domain.Department;
import mn.data.repository.DepartmentRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("/departments")
public class DepartmentController
{
    private final DepartmentRepository departmentRepository;

    @Inject
    public DepartmentController(DepartmentRepository departmentRepository)
    {
        this.departmentRepository = departmentRepository;
    }

    @Get("/")
    public List<Department> all() {
        return (ArrayList<Department>) departmentRepository.findAll();
    }

    @Get("/{id}")
    public HttpResponse<Department> getById(Long id) {
        Department found = departmentRepository.findById(id).orElse(null);
        if (found == null) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(found);
    }

    @Get("/findBy{?deptCode}")
    public HttpResponse<List<Department>> getFindBy(@QueryValue @Nullable String deptCode) {
        if (deptCode == null) {
            return HttpResponse.badRequest();
        }
        else {
            return HttpResponse.ok(departmentRepository.findByDeptCode(deptCode));
        }
    }



    @Put("/")
    @Transactional(value = Transactional.TxType.REQUIRED)
    public HttpResponse create(@Body @Valid Department department) {

        Department origDepartment = departmentRepository.findById(department.getId()).orElse(null);
        if (origDepartment == null) {
            return HttpResponse.notFound();
        }
        origDepartment.setDeptCode(department.getDeptCode());
        origDepartment.setDeptName(department.getDeptName());
        origDepartment = departmentRepository.save(origDepartment);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(origDepartment).getPath());
    }

    @Post("/")
    public HttpResponse update(@Body @Valid Department department) {
        assert department.getId() == null;
        final Department updated = departmentRepository.save(department);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(department).getPath());
    }

    @Delete("/{id}")
    @Transactional(value = Transactional.TxType.REQUIRED)
    public HttpResponse<Department> delete(Long id) {
        Department found = departmentRepository.findById(id).orElse(null);
        if (found == null) {
            return HttpResponse.notFound();
        }
        departmentRepository.delete(found);
        return HttpResponse.ok(found);
    }

    private URI toUri(Department department) {
        return URI.create("/department/" + department.getId());
    }
}
