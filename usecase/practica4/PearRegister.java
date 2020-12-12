package usecase.practica4;

import material.maps.HashTableMapSC;

import java.io.*;
import java.util.*;


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

        @Override
        public boolean equals(Object obj) {
            return this.id == ((PearStore) obj).getId();
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

        @Override
        public boolean equals(Object obj) {
            Product casted = ((Product) obj);
            return this.name.equals(casted.getName()) && this.year == casted.getYear();
        }
    }


    HashTableMapSC<Product, ArrayList<PearStore>> register;

    public void loadFile(String pathToFile) throws IOException {
        File file = new File(pathToFile);
        Scanner scan = new Scanner(file);
        int n = scan.nextInt();

        this.register = new HashTableMapSC<>(n);

        for (int i = 0; i < n; i++) {
            String name = scan.next();
            int year = scan.nextInt();

            Product product = new Product(name, year);

            int t = scan.nextInt();
            ArrayList<PearStore> stores = new ArrayList<>(t);
            for (int j = 0; j < t; j++) {
                String storeName = scan.next();
                int id = scan.nextInt();
                int stock = scan.nextInt();
                float score = scan.nextFloat();
                PearStore store = new PearStore(storeName, id, stock, score);
                stores.add(store);
            }

            register.put(product, stores);
        }

    }

    public void addProduct(Product producto, Iterable<PearStore> stores) {
        register.put(producto, (ArrayList<PearStore>) stores);
    }

    public void addSalesInPearStore(Product producto, PearStore store, int units, double score) {
        register.get(findProduct(producto)).stream().filter(sto -> sto.equals(store)).findFirst().ifPresent(pearStore -> {
            pearStore.setStock(units);
            pearStore.setScore(score);
        });
    }

    public double getScoreOfProduct(Product producto) {
        return register.get(findProduct(producto)).stream().mapToDouble(PearStore::getScore).average().orElse(0);
    }

    public PearStore getGreatestSeller(Product producto) {
        return register.get(findProduct(producto)).stream().max(Comparator.comparing(PearStore::getStock)).orElse(null);
    }

    public int getUnits(Product producto) {
        return register.get(findProduct(producto)).stream().map(s -> s.getStock()).reduce(Integer::sum).orElse(0);
    }

    public boolean productExists(Product product) {
        return Objects.nonNull(register.get(product));
    }

    private Product findProduct(Product ref) {
        return ((ArrayList<Product>) register.keys()).stream().filter(e -> e.equals(ref)).findFirst().orElse(null);
    }
}

