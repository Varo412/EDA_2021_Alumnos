package material.test.practica3;

import material.Position;
import material.tree.binarytree.ArrayBinaryTree;
import material.tree.binarytree.BinaryTree;
import material.tree.binarytree.BinaryTreeUtils;
import org.junit.jupiter.api.Assertions;
import usecase.practica3.Diameter;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class BinaryTreeTest {

    private BinaryTree<Character> tree;
    private Position<Character>[] pos;
    private BinaryTreeUtils<Character> btu;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        tree = new ArrayBinaryTree<>();
        btu = new BinaryTreeUtils<>(this.tree);
        pos = new Position[12];
        pos[0] = tree.addRoot('a');
        pos[1] = tree.insertLeft(pos[0], 'b');
        pos[2] = tree.insertRight(pos[0], 'c');
        pos[3] = tree.insertLeft(pos[1], 'd');
        pos[4] = tree.insertRight(pos[1], 'e');
        pos[5] = tree.insertLeft(pos[4], 'g');
        pos[6] = tree.insertRight(pos[2], 'f');
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Not required
    }

    @org.junit.jupiter.api.Test
    void insert() {
        int s = tree.size();
        assertEquals(tree.insertLeft(pos[6], 'w'), tree.left(pos[6]));
        assertEquals(tree.insertRight(pos[6], 'v'), tree.right(pos[6]));
        assertEquals(tree.size(), s + 2);
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> tree.insertRight(pos[1], 't'));
        assertEquals("Node already has a right child", runtimeException.getMessage());
    }

    @org.junit.jupiter.api.Test
    void subtree() {
        Iterator<Position<Character>> ite = this.tree.subTree(pos[1]).iterator();
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals("dbge", result);
    }

    @org.junit.jupiter.api.Test
    void mirror() {
        Iterator<Position<Character>> ite = this.btu.mirror().iterator();
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals("fcaegbd", result);
    }

    @org.junit.jupiter.api.Test
    void contains() {
        assertTrue(this.btu.contains('e'));
        assertFalse(this.btu.contains('ç'));
    }

    @org.junit.jupiter.api.Test
    void levelIterative() {
        assertEquals(3, btu.level(pos[6]));
    }

    @org.junit.jupiter.api.Test
    void level() {
        assertEquals(3, btu.level(pos[6]));
    }

    @org.junit.jupiter.api.Test
    void remove() {
        int s = tree.size();
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> tree.remove(pos[0]));
        assertEquals("Cannot delete nodes with more than one child", runtimeException.getMessage());
        assertEquals(tree.size(), s);
        assertEquals('e', tree.remove(pos[4]));
        assertEquals(tree.size(), s - 2);
        s -= 2;
        assertEquals('d', tree.remove(pos[3]));
        assertEquals(tree.size(), s - 1);
    }

    @org.junit.jupiter.api.Test
    void replace() {
        assertEquals(tree.root().getElement(), tree.replace(pos[0], 'r'));
        assertEquals(tree.root().getElement(), 'r');
    }

    @org.junit.jupiter.api.Test
    void resize() {
        int s = tree.size();
        Position<Character> p = pos[2];
        for (int i = 0; i < 40; i++) {
            p = tree.insertLeft(p, (char) (i + 35));
        }
        assertEquals(s + 40, tree.size());

    }

    @org.junit.jupiter.api.Test
    void completion() {
        assertFalse(tree.isComplete());
    }

    @org.junit.jupiter.api.Test
    void moveSubtree() {
        int s = tree.size();
        tree.moveSubtree(pos[1], pos[6]);
        assertEquals(s, tree.size());

        BinaryTree<Character> t = this.tree.subTree(pos[6]);
        Iterator<Position<Character>> ite = t.iterator();
        String result = "";
        while (ite.hasNext()) {
            result += ite.next().getElement();
        }
        assertEquals("dbgef", result);
    }

    @org.junit.jupiter.api.Test
    void attach() {
        int s = tree.size();
        BinaryTree<Character> n1 = tree.subTree(tree.root());
        tree.attachLeft(pos[5], n1);
        assertEquals(tree.size(), s * 2);
        s *= 2;
        Iterator<Position<Character>> ite = tree.iterator();
        int i = 0;
        while (ite.hasNext()) {
            i++;
            ite.next();
        }
        assertEquals(s, i);
    }

    @org.junit.jupiter.api.Test
    void diameter() {
        BinaryTree<Integer> newtree = new ArrayBinaryTree<>();
        Position<Integer> pos[] = new Position[12];
        pos[0] = newtree.addRoot(1);
        pos[1] = newtree.insertLeft(pos[0], 2);
        pos[2] = newtree.insertRight(pos[0], 3);
        pos[3] = newtree.insertRight(pos[1], 4);
        pos[4] = newtree.insertRight(pos[2], 6);
        pos[5] = newtree.insertLeft(pos[2], 5);
        pos[6] = newtree.insertLeft(pos[5], 7);
        pos[7] = newtree.insertRight(pos[5], 8);

        assertEquals(6, Diameter.evalDiameter(newtree, pos[3], pos[7]));
    }

}
