package mn.data.controller;

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import mn.data.domain.Company;
import mn.data.repository.CompanyRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller("/companies")
public class CompanyController
{
    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    @Get("/")
    public List<Company> all() {
        return (ArrayList<Company>) companyRepository.findAll();
    }

    @Get("/{id}")
    public HttpResponse<Company> getById(Long id) {
        Company found = companyRepository.findById(id).orElse(null);
        if (found == null) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(found);
    }

    @Put("/{id}")
    @Transactional(value = Transactional.TxType.REQUIRED)
    public HttpResponse update(Long id, @Body @Valid Company company) {
        if (company.getId() == null || !company.getId().equals(id)) {
            return HttpResponse.badRequest();
        }
        Company origCompany = companyRepository.findById(id).orElse(null);
        if (origCompany == null) {
            return HttpResponse.notFound();
        }
        origCompany.setCompanyName(company.getCompanyName());
        origCompany = companyRepository.save(origCompany);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(origCompany).getPath());
    }

    @Post("/")
    public HttpResponse create(@Body @Valid Company company) {
        assert company.getId() == null;
        final Company updated = companyRepository.save(company);
        return HttpResponse
            .noContent()
            .header(HttpHeaders.LOCATION, toUri(company).getPath());
    }

    @Delete("/{id}")
    @Transactional(value = Transactional.TxType.REQUIRED)
    public HttpResponse<Company> delete(Long id) {
        Company found = companyRepository.findById(id).orElse(null);
        if (found == null) {
            return HttpResponse.notFound();
        }
        companyRepository.delete(found);
        return HttpResponse.ok(found);
    }

    private URI toUri(Company company) {
        return URI.create("/company/" + company.getId());
    }
}
