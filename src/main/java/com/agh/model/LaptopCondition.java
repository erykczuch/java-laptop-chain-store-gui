package com.agh.model;

public enum LaptopCondition {
    WORKING{
        @Override
        public String toString() {
            return "Working";
        }
    },
    DAMAGED{
        @Override
        public String toString() {
            return "Damaged";
        }
    },
    IN_REPAIR{
        @Override
        public String toString() {
            return "In repair";
        }
    },
    IN_TESTING{
        @Override
        public String toString() {
            return "In testing";
        }
    }
}