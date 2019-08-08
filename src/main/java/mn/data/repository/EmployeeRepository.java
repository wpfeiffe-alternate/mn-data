package mn.data.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import mn.data.domain.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long>
{
}
