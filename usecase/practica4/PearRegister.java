package usecase.practica4;

import material.maps.HashTableMapDH;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PearRegister {

    public static class PearStore {
        private String name;
        private int id;
        private int stock;
        private double score;

        public PearStore(String name, int id) {
            this.name = name;
            this.id = id;
            this.stock = 0;
            this.score = 0;
        }

        public PearStore(String name, int id, int stock, double score) {
            this.name = name;
            this.id = id;
            this.stock = stock;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    public static class Product {
        private String name;
        private int year;

        public Product(String name, int year) {
            this.name = name;
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }


    HashTableMapDH<Product, List<PearStore>> register;

    public void loadFile(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        Scanner scan = new Scanner(file);
        int n = scan.nextInt();

        this.register = new HashTableMapDH<>(n);

        for (int i = 0; i < n; i++) {
            String name = scan.next();
            int year = scan.nextInt();

            Product product = new Product(name, year);

            int t = scan.nextInt();
            ArrayList<PearStore> stores = new ArrayList<>(t);
            for (int j = 0; j < t; i++) {
                String storeName = scan.next();
                int id = scan.nextInt();
                int stock = scan.nextInt();
                long score = scan.nextLong();
                PearStore store = new PearStore(storeName, id, stock, score);
                stores.add(store);
            }

            register.put(product, stores);

        }

    }

    public void addProduct(Product producto, Iterable<PearStore> stores) {
        throw new RuntimeException("Not yet implemented");
    }

    public void addSalesInPearStore(Product producto, PearStore store, int units, double score) {
        throw new RuntimeException("Not yet implemented");
    }

    public double getScoreOfProduct(Product producto) {
        throw new RuntimeException("Not yet implemented");
    }

    public PearStore getGreatestSeller(Product producto) {
        throw new RuntimeException("Not yet implemented");
    }

    public int getUnits(Product producto) {
        throw new RuntimeException("Not yet implemented");
    }

    public boolean productExists(Product product) {
        throw new RuntimeException("Not yet implemented");
    }
}

