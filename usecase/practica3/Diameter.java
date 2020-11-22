package usecase.practica3;

import material.Position;
import material.tree.binarytree.BinaryTree;

import java.util.HashMap;

public class Diameter {

    public static int evalDiameter(BinaryTree<Integer> tree, Position<Integer> v1, Position<Integer> v2) {
        HashMap<Position<Integer>, Integer> m1 = new HashMap<>(tree.size());

        int c = 0;
        Position<Integer> aux = v1;


        while (aux != null) {
            if (aux == v2) return ++c;
            m1.put(aux, ++c);
            if (aux != tree.root())
                aux = tree.parent(aux);
            else break;
        }

        aux = v2;
        c = 0;
        while (aux != null) {
            if (m1.containsKey(aux)) return c + m1.get(aux);
            c++;
            aux = tree.parent(aux);
        }
        throw new RuntimeException("Relationship not found");
    }
}