package com.example;

import java.util.List;
public class CarService {
    private final CarDao carDao = CarDao.getINSTANCE();

    public void save(Car car) {
        carDao.saveCar(car);
    }

    public Car getById(int id) {
        return carDao.getById(id);
    }

    public void deleteById(int id) {
        carDao.deleteById(id);
    }
    public List<Car> getAllCars() {
        return carDao.getAll();
    }
}
