package carsharing.car;

import java.util.List;

public interface CarDAO {

    List<Car> getCars(int companyId);

    void createCar(int companyId, String name);
}
