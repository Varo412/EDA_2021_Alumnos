package material.test.practica4;


import usecase.practica4.*;
import usecase.practica4.PearRegister.PearStore;
import usecase.practica4.PearRegister.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PearRegisterTest {
    private static PearRegister register;

    @org.junit.jupiter.api.BeforeAll
    static void loadFile() throws IOException {
        register = new PearRegister();
        String path = "docs/p4 - PearSalesFile.txt";
        register.loadFile(path);
    }


    @org.junit.jupiter.api.Test
    void addProductAndProductExists() {
        Product producto = new Product("NewYMak", 2021);
        PearStore pearStore = new PearStore("CarabanchelPearStore", 123456);
        List<PearStore> pearStoreList = new ArrayList<>();
        pearStoreList.add(pearStore);
        register.addProduct(producto, pearStoreList);
        assertTrue(register.productExists(producto));
    }

    @org.junit.jupiter.api.Test
    void getScoreOfProduct() {
        Product producto = new Product("MakBukPro", 2019);
        assertEquals(4.1, Math.round(register.getScoreOfProduct(producto) * 10) / 10.0);
    }

    @org.junit.jupiter.api.Test
    void getGreatestSeller() {
        Product producto = new Product("MakBukPro", 2019);
        PearStore ps = register.getGreatestSeller(producto);
        PearStore toTest = new PearStore("PrincipePioPearStore", 123444);
        assertEquals(ps, toTest);
    }

    @org.junit.jupiter.api.Test
    void getUnits() {
        Product producto = new Product("MakBukPro", 2019);
        int units = register.getUnits(producto);
        assertEquals(2837, units);

        register.addProduct(new Product("MakBukPro", 2020), new ArrayList<>());
        int units2 = register.getUnits(new Product("MakBukPro", 2020));
        assertEquals(0, units2);
    }
}