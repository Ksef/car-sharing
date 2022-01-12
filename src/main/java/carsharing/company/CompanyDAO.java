package carsharing.company;

import java.util.List;

public interface CompanyDAO {

    List<Company> getCompanies();

    void createCompany(String name);

    int getCompanyId(String name);
}
