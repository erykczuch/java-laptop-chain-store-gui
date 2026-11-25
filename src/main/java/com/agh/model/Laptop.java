package com.agh.model;


import java.util.Comparator;

public class Laptop implements Comparable<Laptop>{
    public LaptopCondition laptopCondition;
    public String model;
    public String brand;
    public int year;
    public double price;
    public int ramGB;
    public int diskSpaceGB;
    public String processor;
    public int batteryHealth;
    public int serialNumber;
    public boolean reserved = false;

    public Laptop (LaptopCondition laptopCondition, String model, String brand, int year, double price,
                   int ramGB, int diskSpaceGB, String processor, int batteryHealth, int serialNumber)
    {
        this.laptopCondition = laptopCondition;
        this.model = model;
        this.brand = brand;
        this.year = year;
        this.price = price;
        this.ramGB = ramGB;
        this.diskSpaceGB = diskSpaceGB;
        this.processor = processor;
        this.batteryHealth = batteryHealth;
        this.serialNumber = serialNumber;

    }

    public void print() {
        System.out.println("              LAPTOP INFO");
        System.out.println("Brand:          " + brand);
        System.out.println("Model:          " + model);
        System.out.println("Condition:      " + laptopCondition + "\n");
        System.out.println("Year:           " + year);
        System.out.println("Price:          " + price);
        System.out.println("Processor:      " + processor);
        System.out.println("RAM:            " + ramGB + " GB");
        System.out.println("Storage:        " + diskSpaceGB + " GB");
        System.out.println("Battery health: " + batteryHealth + "%\n");
        System.out.println("Serial number:  " + serialNumber);
    }

    @Override
    public int compareTo(Laptop other) {
        int byModel = this.model.compareTo(other.model);
        if (byModel != 0){
            return byModel;
        }
        int byBrand = this.brand.compareTo(other.brand);
        if (byBrand != 0){
            return byBrand;
        }
        return Integer.compare(this.year, other.year);
    }
    public static Comparator<Laptop> compareByBrand() {
        return Comparator.comparing(laptop -> laptop.brand);
    }
    public static Comparator<Laptop> compareByYear() {
        return Comparator.comparing(laptop -> laptop.year);
    }
    public static Comparator<Laptop> compareByPrice() {
        return Comparator.comparingDouble(laptop -> laptop.price);
    }
    public static Comparator<Laptop> compareByModel() {
        return Comparator.comparing(laptop -> laptop.model);
    }
    public int compareByModel(String model) {
        return this.model.compareTo(model);
    }

}
