package com.agh.model;

import com.agh.model.exception.BranchFullException;
import com.agh.model.exception.DuplicateLaptopException;
import com.agh.model.exception.LaptopNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BranchTest {

    @Test
    void addLaptopPutsLaptopIntoBranch() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 2);
        Laptop laptop = laptop("Air", "Apple", 2020, 1000, 1);

        branch.addLaptop(laptop);

        assertEquals(1, branch.laptopList.size());
        assertSame(laptop, branch.laptopList.get(0));
    }

    @Test
    void addLaptopRejectsLaptopWhenBranchIsFull() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 1);
        branch.addLaptop(laptop("Air", "Apple", 2020, 1000, 1));

        assertThrows(BranchFullException.class, () -> branch.addLaptop(laptop("Pro", "Apple", 2023, 2000, 2)));
    }

    @Test
    void addLaptopRejectsDuplicate() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 2);
        branch.addLaptop(laptop("Air", "Apple", 2020, 1000, 1));

        assertThrows(DuplicateLaptopException.class, () -> branch.addLaptop(laptop("Air", "Apple", 2020, 1500, 2)));
    }

    @Test
    void removeAndSellLaptopTakeItOffTheList() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 2);
        Laptop first = laptop("Air", "Apple", 2020, 1000, 1);
        Laptop second = laptop("Pro", "Apple", 2023, 2000, 2);
        branch.addLaptop(first);
        branch.addLaptop(second);

        branch.removeLaptop(first);
        branch.sellLaptop(second);

        assertTrue(branch.laptopList.isEmpty());
    }

    @Test
    void changeConditionAndYearUpdateTheLaptop() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 1);
        Laptop laptop = laptop("Air", "Apple", 2020, 1000, 1);
        branch.addLaptop(laptop);

        branch.changeCondition(laptop, LaptopCondition.DAMAGED);
        branch.changeYear(laptop, 2024);

        assertEquals(LaptopCondition.DAMAGED, laptop.laptopCondition);
        assertEquals(2024, laptop.year);
    }

    @Test
    void countByConditionCountsOnlyMatchingLaptops() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 2);
        Laptop working = laptop("Air", "Apple", 2020, 1000, 1);
        Laptop damaged = laptop("Pro", "Apple", 2023, 2000, 2);
        branch.addLaptop(working);
        branch.addLaptop(damaged);
        branch.changeCondition(damaged, LaptopCondition.DAMAGED);

        assertEquals(1, branch.countByCondition(LaptopCondition.WORKING));
        assertEquals(1, branch.countByCondition(LaptopCondition.DAMAGED));
    }

    @Test
    void sortByNameAndPriceReturnOrderedCopies() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 2);
        Laptop expensive = laptop("Pro", "Apple", 2023, 2000, 2);
        Laptop cheap = laptop("Air", "Apple", 2020, 1000, 1);
        branch.addLaptop(expensive);
        branch.addLaptop(cheap);

        assertEquals("Air", branch.sortByName().get(0).model);
        assertEquals(1000, branch.sortByPrice().get(0).price);
        assertEquals("Pro", branch.laptopList.get(0).model);
    }

    @Test
    void searchReturnsLaptopWithGivenModel() throws BranchFullException, DuplicateLaptopException, LaptopNotFoundException {
        Branch branch = new Branch("Krakow", 1);
        Laptop laptop = laptop("Air", "Apple", 2020, 1000, 1);
        branch.addLaptop(laptop);

        assertSame(laptop, branch.search("Air"));
    }

    @Test
    void searchThrowsWhenModelIsMissing() {
        Branch branch = new Branch("Krakow", 1);

        assertThrows(LaptopNotFoundException.class, () -> branch.search("Missing"));
    }

    @Test
    void searchPartialIgnoresCase() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 1);
        branch.addLaptop(laptop("XPS", "Dell", 2024, 3000, 1));

        assertEquals(1, branch.searchPartial("dell").size());
        assertEquals(1, branch.searchPartial("xp").size());
    }

    @Test
    void totalValueAndMaxUsePrice() throws BranchFullException, DuplicateLaptopException {
        Branch branch = new Branch("Krakow", 2);
        Laptop cheap = laptop("Air", "Apple", 2020, 1000, 1);
        Laptop expensive = laptop("Pro", "Apple", 2023, 2500, 2);
        branch.addLaptop(cheap);
        branch.addLaptop(expensive);

        assertEquals(3500, branch.totalValue());
        assertSame(expensive, branch.max());
        branch.summary();
    }

    static Laptop laptop(String model, String brand, int year, double price, int serial) {
        return new Laptop(LaptopCondition.WORKING, model, brand, year, price, 8, 256, "i5", 90, serial);
    }
}
