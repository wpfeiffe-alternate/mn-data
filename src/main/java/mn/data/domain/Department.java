package mn.data.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
    @SequenceGenerator(name = "department_id_seq", sequenceName = "department_id_seq", allocationSize = 1)
    private Long id;

    @NotNull
    @Size(min = 2, max = 20)
    @Column(name = "dept_code", length = 20, nullable = false)
    private String deptCode;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(name = "dept_name", length = 30, nullable = false)
    private String deptName;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID", referencedColumnName = "ID", nullable = false)
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public Department deptCode(String deptCode) {
        this.deptCode = deptCode;
        return this;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public Department deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Company getCompany() {
        return company;
    }

    public Department company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Department department = (Department) o;
        if (department.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, department.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Department{" +
            "id=" + id +
            ", deptCode='" + deptCode + "'" +
            ", deptName='" + deptName + "'" +
            '}';
    }
}
