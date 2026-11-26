package com.agh.app;

import com.agh.model.Branch;
import com.agh.model.Laptop;
import com.agh.model.LaptopCondition;
import com.agh.model.StoreManager;
import com.agh.model.exception.BranchFullException;
import com.agh.model.exception.DuplicateLaptopException;
import com.agh.model.exception.InvalidPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoreFacadeTest {
    StoreFacade facade;
    StoreManager store;

    @BeforeEach
    void setUp() {
        store = new StoreManager();
        facade = new StoreFacade(store);
    }

    @Test
    void addAndRemoveBranch() {
        facade.addBranch("Krakow", 3);

        assertTrue(facade.branches().containsKey("Krakow"));

        facade.removeBranch("Krakow");
        assertTrue(facade.branches().isEmpty());
    }

    @Test
    void addAndRemoveLaptop() throws BranchFullException, DuplicateLaptopException {
        facade.addBranch("Krakow", 2);
        Branch branch = store.branches.get("Krakow");
        Laptop laptop = laptop("Air", "Apple", 1000, 1);

        facade.addLaptop(branch, laptop);
        assertEquals(List.of(laptop), facade.laptops(branch));

        facade.removeLaptop(branch, laptop);
        assertTrue(facade.laptops(branch).isEmpty());
    }

    @Test
    void addLaptopPropagatesFullBranch() throws BranchFullException, DuplicateLaptopException {
        facade.addBranch("Krakow", 1);
        Branch branch = store.branches.get("Krakow");
        facade.addLaptop(branch, laptop("Air", "Apple", 1000, 1));

        assertThrows(BranchFullException.class, () -> facade.addLaptop(branch, laptop("Pro", "Dell", 2000, 2)));
    }

    @Test
    void changeConditionAndPrice() throws InvalidPriceException {
        facade.addBranch("Krakow", 1);
        Laptop laptop = laptop("Air", "Apple", 1000, 1);

        facade.changeCondition(store.branches.get("Krakow"), laptop, LaptopCondition.IN_REPAIR);
        facade.changePrice(laptop, 1200);

        assertEquals(LaptopCondition.IN_REPAIR, laptop.laptopCondition);
        assertEquals(1200, laptop.price);
    }

    @Test
    void changePriceRejectsNegativeValue() {
        Laptop laptop = laptop("Air", "Apple", 1000, 1);

        assertThrows(InvalidPriceException.class, () -> facade.changePrice(laptop, -1));
        assertEquals(1000, laptop.price);
    }

    @Test
    void sortBranchesByLoadOrdersByFillRatio() throws BranchFullException, DuplicateLaptopException {
        facade.addBranch("Empty", 2);
        facade.addBranch("Full", 1);
        facade.addLaptop(store.branches.get("Full"), laptop("Air", "Apple", 1000, 1));

        assertEquals("Empty", facade.sortBranchesByLoad(false).get(0).branchName);
        assertEquals("Full", facade.sortBranchesByLoad(true).get(0).branchName);
    }

    @Test
    void filterUsesTextAndCondition() throws BranchFullException, DuplicateLaptopException {
        facade.addBranch("Krakow", 2);
        Branch branch = store.branches.get("Krakow");
        Laptop dell = laptop("XPS", "Dell", 3000, 1);
        Laptop apple = laptop("Air", "Apple", 4000, 2);
        apple.laptopCondition = LaptopCondition.DAMAGED;
        facade.addLaptop(branch, dell);
        facade.addLaptop(branch, apple);

        assertEquals(List.of(dell), facade.filter(branch, "DELL", null));
        assertEquals(List.of(apple), facade.filter(branch, "", LaptopCondition.DAMAGED));
    }

    @Test
    void reserveAndCancelReservation() {
        Laptop laptop = laptop("Air", "Apple", 1000, 1);

        facade.reserveLaptop(laptop);
        assertTrue(laptop.reserved);

        facade.cancelReservation(laptop);
        assertFalse(laptop.reserved);
    }

    private Laptop laptop(String model, String brand, double price, int serial) {
        return new Laptop(LaptopCondition.WORKING, model, brand, 2020, price, 8, 256, "i5", 90, serial);
    }
}
