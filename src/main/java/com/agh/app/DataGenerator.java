package com.agh.app;

import com.agh.model.Branch;
import com.agh.model.Laptop;
import com.agh.model.LaptopCondition;
import com.agh.model.StoreManager;

public class DataGenerator {
    private static final DataGenerator INSTANCE = new DataGenerator();
    private final StoreManager store = new StoreManager();

    private DataGenerator(){
        store.addBranch("Krakow", 4);
        store.addBranch("Warszawa", 2);
        store.addBranch("Gdansk", 3);

        Branch krakow = store.branches.get("Krakow");
        Branch warszawa = store.branches.get("Warszawa");
        Branch gdansk = store.branches.get("Gdansk");

        Laptop air = new Laptop(LaptopCondition.WORKING, "MacBook Air", "Apple", 2020, 3999,
                8, 256, "M1", 92, 1001);
        Laptop pro = new Laptop(LaptopCondition.DAMAGED, "MacBook Pro", "Apple", 2023, 8999,
                16, 512, "M2", 88, 1002);
        Laptop thinkpad = new Laptop(LaptopCondition.WORKING, "ThinkPad", "Lenovo", 2021, 4299,
                16, 512, "i7", 81, 1003);
        Laptop zenbook = new Laptop(LaptopCondition.IN_REPAIR, "ZenBook", "Asus", 2022, 3599,
                16, 512, "Ryzen 7", 95, 1004);
        Laptop xps = new Laptop(LaptopCondition.IN_TESTING, "XPS", "Dell", 2024, 6200,
                32, 1000, "i9", 100, 2001);
        Laptop latitude = new Laptop(LaptopCondition.IN_TESTING, "Latitude", "Dell", 2019, 2100,
                8, 256, "i5", 70, 2002);

        krakow.addLaptop(air);
        krakow.addLaptop(pro);
        krakow.addLaptop(thinkpad);
        krakow.addLaptop(zenbook);
        warszawa.addLaptop(xps);
        warszawa.addLaptop(latitude);

    }

    public static DataGenerator getInstance(){
        return INSTANCE;
    }

    public StoreManager getStore(){
        return store;
    }
}
