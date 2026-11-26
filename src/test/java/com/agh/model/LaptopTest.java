package com.agh.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LaptopTest {

    @Test
    void compareToOrdersByModelThenBrandThenYear() {
        Laptop air = BranchTest.laptop("Air", "Apple", 2020, 1000, 1);
        Laptop pro = BranchTest.laptop("Pro", "Apple", 2020, 2000, 2);
        Laptop older = BranchTest.laptop("Air", "Apple", 2019, 900, 3);
        Laptop otherBrand = BranchTest.laptop("Air", "Dell", 2020, 1000, 4);

        assertTrue(air.compareTo(pro) < 0);
        assertTrue(air.compareTo(older) > 0);
        assertTrue(air.compareTo(otherBrand) < 0);
        assertEquals(0, air.compareTo(BranchTest.laptop("Air", "Apple", 2020, 1, 5)));
    }

    @Test
    void comparatorsOrderByTheChosenField() {
        Laptop cheap = BranchTest.laptop("Pro", "Dell", 2024, 1000, 1);
        Laptop expensive = BranchTest.laptop("Air", "Apple", 2020, 3000, 2);
        List<Laptop> laptops = List.of(cheap, expensive);

        assertEquals("Air", laptops.stream().min(Laptop.compareByModel()).orElseThrow().model);
        assertEquals("Apple", laptops.stream().min(Laptop.compareByBrand()).orElseThrow().brand);
        assertEquals(2020, laptops.stream().min(Laptop.compareByYear()).orElseThrow().year);
        assertEquals(1000, laptops.stream().min(Laptop.compareByPrice()).orElseThrow().price);
        assertTrue(cheap.compareByModel("Pro") == 0);
        cheap.print();
    }
}
