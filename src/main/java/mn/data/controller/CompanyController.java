package mn.data.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import mn.data.domain.Company;
import mn.data.repository.AbstractCompanyRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller("/companies")
public class CompanyController
{
    private final AbstractCompanyRepository companyRepository;

    @Inject
    public CompanyController(AbstractCompanyRepository companyRepository)
    {
        this.companyRepository = companyRepository;
    }

    @Get("/")
    List<Company> all() {
        List<Company> companies = new ArrayList<>();
        companyRepository.findAll().forEach(companies::add);
        return companies;
    }
}
