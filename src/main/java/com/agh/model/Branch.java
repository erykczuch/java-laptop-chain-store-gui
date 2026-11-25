package com.agh.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Branch {
    public String branchName;
    public List<Laptop> laptopList;
    public int maxCapacity;

    public Branch(String branchName, int maxCapacity){
        this.branchName = branchName;
        this.maxCapacity = maxCapacity;
        this.laptopList = new ArrayList<>();
    }

    public void addLaptop(Laptop laptop) {
        if(laptopList.size() >= maxCapacity)
            throw new IllegalArgumentException("Branch is full");

        for (Laptop l : laptopList) {
            if(laptop.compareTo(l) == 0)
                throw new IllegalArgumentException("This laptop has been already added");
        }

        laptopList.add(laptop);
    }
    public void removeLaptop(Laptop laptop) {
        laptopList.remove(laptop);
    }
    public void sellLaptop(Laptop laptop) {
        laptopList.remove(laptop);
    }
    public void changeCondition(Laptop laptop, LaptopCondition condition){
        laptop.laptopCondition = condition;
    }
    public void changeYear(Laptop laptop, int year){
        laptop.year = year;
    }
    public int countByCondition(LaptopCondition condition) {
        int count = 0;
        for (Laptop l : laptopList){
            if (l.laptopCondition == condition)
                count++;
        }
        return count;
    }
    public List<Laptop> sortByName(){
        List<Laptop> laptops = new ArrayList<>(laptopList);
        Collections.sort(laptops);
        return laptops;
    }
    public List<Laptop> sortByPrice() {
        List<Laptop> laptops = new ArrayList<>(laptopList);
        Collections.sort(laptops, Laptop.compareByPrice());
        return laptops;
    }
    public Laptop search(String model){
      Comparator<String> byModel = Comparator.naturalOrder();
        for (Laptop l : laptopList){
          if(byModel.compare(l.model, model) == 0){
              return l;
          }
      }
        throw new IllegalArgumentException("Laptop with this model was not found");
    }
    public List<Laptop> searchPartial(String text){
        List<Laptop> foundLaptops = new ArrayList<>();
        for (Laptop l : laptopList) {
            if((l.model).contains(text) || (l.brand).contains(text))
                foundLaptops.add(l);
        }
        return foundLaptops;
    }
    public void summary(){
        for (Laptop l : laptopList){
            l.print();
            System.out.println();
        }

    }
    public Laptop max(){
        return Collections.max(laptopList, Laptop.compareByPrice());
    }
    public double totalValue(){
        double totalPrice = 0;
        for (Laptop l : laptopList)
            totalPrice += l.price;
        return totalPrice;
    }
}
