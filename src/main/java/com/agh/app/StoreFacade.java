package com.agh.app;

import com.agh.model.Branch;
import com.agh.model.Laptop;
import com.agh.model.LaptopCondition;
import com.agh.model.StoreManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoreFacade {
    StoreManager store = new StoreManager();

    public StoreFacade(StoreManager store){
        this.store = store;
    }
    public Map<String, Branch> branches(){
        return store.branches;
    }
    public List<Laptop> laptops(Branch branch){
        return branch.laptopList;
    }
    public void addBranch(String name, int capacity){
        store.addBranch(name, capacity);
    }
    public void removeBranch(String branch){
        store.removeBranch(branch);
    }
    public void addLaptop(Branch branch, Laptop laptop){
        branch.addLaptop(laptop);
    }
    public void removeLaptop(Branch branch, Laptop laptop){
        branch.removeLaptop(laptop);
    }
    public void changeCondition(Branch branch, Laptop laptop, LaptopCondition condition){
        branch.changeCondition(laptop, condition);
    }
    public void changePrice(Laptop laptop, double price){
        laptop.price = price;
    }
    public List<Branch> sortBranchesByLoad(boolean descending){
        List<Branch> sortedBranches = new ArrayList<>(store.branches.values());;
        boolean sorted = false;

        while(!sorted){
            sorted = true;
            for( int i = 0; i < sortedBranches.size() - 1; i++){
                if(descending){
                    if(sortedBranches.get(i).laptopList.size()/ (double) sortedBranches.get(i).maxCapacity <
                            sortedBranches.get(i+1).laptopList.size()/ (double) sortedBranches.get(i+1).maxCapacity){
                        Branch tempBranch = sortedBranches.get(i);
                        sortedBranches.set(i, sortedBranches.get(i+1));
                        sortedBranches.set(i+1, tempBranch);
                        sorted = false;
                    }
                }
                else{
                    if(sortedBranches.get(i).laptopList.size()/ (double) sortedBranches.get(i).maxCapacity >
                            sortedBranches.get(i+1).laptopList.size()/ (double) sortedBranches.get(i+1).maxCapacity){
                        Branch tempBranch = sortedBranches.get(i);
                        sortedBranches.set(i, sortedBranches.get(i+1));
                        sortedBranches.set(i+1, tempBranch);
                        sorted = false;
                    }
                }
            }
        }
        return sortedBranches;
    }
    public List<Laptop> filter(Branch branch, String text, LaptopCondition condition){
        List<Laptop> filteredLaptops = branch.searchPartial(text);

        for( int i = 0; i < filteredLaptops.size(); i++){
            if (condition != null && filteredLaptops.get(i).laptopCondition != condition) {
                filteredLaptops.remove(i);
                i--;
            }
        }
        return filteredLaptops;
    }
    public void reserveLaptop(Laptop laptop){
        laptop.reserved = true;
    }
}
