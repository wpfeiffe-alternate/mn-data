package mn.data.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mn.data.domain.Company;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long>
{
}
