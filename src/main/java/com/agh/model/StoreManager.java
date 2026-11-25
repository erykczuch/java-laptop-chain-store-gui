package com.agh.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreManager {
    public Map<String, Branch> branches = new HashMap<>();

    public void addBranch(String name, int capacity){
        branches.put(name, new Branch(name, capacity));
    }
    public void removeBranch(String name){
        branches.remove(name);
    }
    List<Branch> findEmpty(){
        List<Branch> emptyBranches = new ArrayList<>();
        for (Branch b : branches.values()){
            if(b.laptopList.size() == 0)
                emptyBranches.add(b);
        }
        return emptyBranches;
    }
    public void summary(){
        for (Branch b : branches.values()){
            System.out.println("Branch name: " + b.branchName);
            System.out.println("Current capacity: " + ((double) b.laptopList.size() / b.maxCapacity) * 100 + "%");
        }
    }
    public void transferLaptop(Laptop laptop, String fromBranch, String toBranch){
        Branch from = branches.get(fromBranch);
        Branch to = branches.get(toBranch);
        boolean found = false;

        if(to.laptopList.size() >= to.maxCapacity)
            throw new IllegalArgumentException("Branch is full");
        for (Laptop l : from.laptopList){
            if(l.serialNumber == laptop.serialNumber){
                found = true;
                break;
            }
        }
        if(!found)
            throw new IllegalArgumentException("Laptop not found in the source branch");

        to.laptopList.add(laptop);
        from.laptopList.remove(laptop);
    }
}
