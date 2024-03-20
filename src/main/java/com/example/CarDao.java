package com.example;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CarDao {
    @Getter
    private static final CarDao INSTANCE = new CarDao();
    private final List<Car> carList = new ArrayList<>();

    public void saveCar(Car car) {
        carList.add(car);
    }

    public Car getById(int id) {
        return carList.stream().filter(car -> car.getId() == id).findFirst().orElse(null);
    }

    public void deleteById(int id) {
        carList.removeIf(car -> car.getId() == id);
    }

    public List<Car> getAll() {
        return new ArrayList<>(carList);
    }

    private CarDao() {
    }
}
