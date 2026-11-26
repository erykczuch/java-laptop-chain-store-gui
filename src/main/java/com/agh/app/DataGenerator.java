package com.agh.app;

import com.agh.model.Branch;
import com.agh.model.Laptop;
import com.agh.model.LaptopCondition;
import com.agh.model.StoreManager;
import com.agh.model.exception.BranchFullException;
import com.agh.model.exception.DuplicateLaptopException;

public class DataGenerator {
    private static final DataGenerator INSTANCE;
    private final StoreManager store = new StoreManager();
    static {
        try {
            INSTANCE = new DataGenerator();
        } catch (BranchFullException | DuplicateLaptopException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DataGenerator() throws BranchFullException, DuplicateLaptopException {
        store.addBranch("Krakow", 12);
        store.addBranch("Warszawa", 8);
        store.addBranch("Gdansk", 24);
        store.addBranch("Wroclaw", 8);
        store.addBranch("Poznan", 10);

        Branch krakow = store.branches.get("Krakow");
        Branch warszawa = store.branches.get("Warszawa");
        Branch gdansk = store.branches.get("Gdansk");
        Branch wroclaw = store.branches.get("Wroclaw");
        Branch poznan = store.branches.get("Poznan");

        krakow.addLaptop(new Laptop(LaptopCondition.WORKING, "MacBook Air", "Apple", 2020, 3999,
                8, 256, "M1", 92, 1001));
        krakow.addLaptop(new Laptop(LaptopCondition.DAMAGED, "MacBook Pro", "Apple", 2023, 8999,
                16, 512, "M2", 88, 1002));
        krakow.addLaptop(new Laptop(LaptopCondition.WORKING, "ThinkPad", "Lenovo", 2021, 4299,
                16, 512, "i7", 81, 1003));
        krakow.addLaptop(new Laptop(LaptopCondition.IN_REPAIR, "ZenBook", "Asus", 2022, 3599,
                16, 512, "Ryzen 7", 95, 1004));
        krakow.addLaptop(new Laptop(LaptopCondition.WORKING, "Pavilion", "HP", 2022, 2799,
                8, 512, "Ryzen 5", 90, 1005));

        warszawa.addLaptop(new Laptop(LaptopCondition.IN_TESTING, "XPS", "Dell", 2024, 6200,
                32, 1000, "i9", 100, 2001));
        warszawa.addLaptop(new Laptop(LaptopCondition.IN_TESTING, "Latitude", "Dell", 2019, 2100,
                8, 256, "i5", 70, 2002));
        warszawa.addLaptop(new Laptop(LaptopCondition.DAMAGED, "Nitro", "Acer", 2021, 3299,
                16, 512, "i5", 64, 2003));

        gdansk.addLaptop(new Laptop(LaptopCondition.WORKING, "Inspiron", "Dell", 2020, 2499,
                8, 256, "i5", 80, 3001));
        gdansk.addLaptop(new Laptop(LaptopCondition.DAMAGED, "IdeaPad", "Lenovo", 2018, 1599,
                8, 256, "Ryzen 3", 55, 3002));
        gdansk.addLaptop(new Laptop(LaptopCondition.IN_REPAIR, "VivoBook", "Asus", 2023, 2899,
                16, 512, "i7", 97, 3003));

        wroclaw.addLaptop(new Laptop(LaptopCondition.WORKING, "MacBook", "Apple", 2019, 3499,
                8, 256, "i5", 74, 4001));
        wroclaw.addLaptop(new Laptop(LaptopCondition.IN_REPAIR, "EliteBook", "HP", 2021, 4100,
                16, 512, "i7", 86, 4002));
        wroclaw.addLaptop(new Laptop(LaptopCondition.IN_TESTING, "Modern", "MSI", 2024, 4599,
                16, 512, "i7", 99, 4003));

        poznan.addLaptop(new Laptop(LaptopCondition.WORKING, "Yoga", "Lenovo", 2022, 3799,
                16, 512, "Ryzen 7", 91, 5001));
        poznan.addLaptop(new Laptop(LaptopCondition.DAMAGED, "Galaxy Book", "Samsung", 2023, 3399,
                8, 256, "i5", 77, 5002));

    }

    public static DataGenerator getInstance(){
        return INSTANCE;
    }

    public StoreManager getStore(){
        return store;
    }
}
