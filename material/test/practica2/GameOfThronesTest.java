package material.test.practica2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.practica2.GameOfThrones;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameOfThronesTest {
    private GameOfThrones got;
    private String path;

    @BeforeEach
    void setUp() throws IOException {
        got = new GameOfThrones();
        path = "docs/Caso de prueba GoT_Families.txt";
        got.loadFile(path);
    }


    @Test
    void testFindHeir() {
        String expected = "Eddard Stark";
        String output = got.findHeir("Stark");
        assertEquals(expected, output);
    }

    @Test
    void testChangeFamily() {
        got.changeFamily("Daenerys Targaryen", "Eddard Stark");
        assertTrue(got.areFamily("Daenerys Targaryen", "Arya Stark"));
    }

    @Test
    void testAreFamily() {
        assertTrue(got.areFamily("Arya Stark", "Robb Stark"));
        assertFalse(got.areFamily("Catelyn Tully", "Aerys Targaryen"));
    }

}