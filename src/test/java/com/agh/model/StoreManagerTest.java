package com.agh.model;

import com.agh.model.exception.BranchFullException;
import com.agh.model.exception.DuplicateLaptopException;
import com.agh.model.exception.LaptopNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreManagerTest {

    @Test
    void addAndRemoveBranch() {
        StoreManager store = new StoreManager();

        store.addBranch("Krakow", 4);
        assertEquals(4, store.branches.get("Krakow").maxCapacity);

        store.removeBranch("Krakow");
        assertFalse(store.branches.containsKey("Krakow"));
    }

    @Test
    void findEmptyReturnsBranchesWithoutLaptops() throws BranchFullException, DuplicateLaptopException {
        StoreManager store = new StoreManager();
        store.addBranch("Empty", 2);
        store.addBranch("Used", 2);
        store.branches.get("Used").addLaptop(BranchTest.laptop("Air", "Apple", 2020, 1000, 1));

        assertEquals(1, store.findEmpty().size());
        assertEquals("Empty", store.findEmpty().get(0).branchName);
        store.summary();
    }

    @Test
    void transferLaptopMovesItBetweenBranches() throws BranchFullException, DuplicateLaptopException, LaptopNotFoundException {
        StoreManager store = new StoreManager();
        store.addBranch("From", 2);
        store.addBranch("To", 2);
        Laptop laptop = BranchTest.laptop("Air", "Apple", 2020, 1000, 7);
        store.branches.get("From").addLaptop(laptop);

        store.transferLaptop(laptop, "From", "To");

        assertTrue(store.branches.get("From").laptopList.isEmpty());
        assertSame(laptop, store.branches.get("To").laptopList.get(0));
    }

    @Test
    void transferLaptopRejectsFullTarget() throws BranchFullException, DuplicateLaptopException {
        StoreManager store = new StoreManager();
        store.addBranch("From", 2);
        store.addBranch("To", 1);
        Laptop moving = BranchTest.laptop("Air", "Apple", 2020, 1000, 1);
        store.branches.get("From").addLaptop(moving);
        store.branches.get("To").addLaptop(BranchTest.laptop("Pro", "Apple", 2023, 2000, 2));

        assertThrows(BranchFullException.class, () -> store.transferLaptop(moving, "From", "To"));
    }

    @Test
    void transferLaptopThrowsWhenSerialIsMissing() {
        StoreManager store = new StoreManager();
        store.addBranch("From", 2);
        store.addBranch("To", 2);

        assertThrows(LaptopNotFoundException.class, () ->
                store.transferLaptop(BranchTest.laptop("Air", "Apple", 2020, 1000, 9), "From", "To"));
    }
}
