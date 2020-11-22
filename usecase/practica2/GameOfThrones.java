package usecase.practica2;

import material.Position;
import material.tree.narytree.LinkedTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameOfThrones {

    public static class FamilyMember {
        private String id;
        private String name;
        private String surname;
        // 0 if male 1 if female
        private boolean genre;
        private int age;
        private LinkedTree<FamilyMember> family;
        private Position<FamilyMember> position;

        public FamilyMember(String id, String name, String surname, boolean genre, int age) {
            this.id = id;
            this.name = name;
            this.surname = surname;
            this.genre = genre;
            this.age = age;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public boolean isFemale() {
            return genre;
        }

        public void setGenre(boolean genre) {
            this.genre = genre;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public LinkedTree<FamilyMember> getFamily() {
            return family;
        }

        public void setFamily(LinkedTree<FamilyMember> family) {
            this.family = family;
        }

        public Position<FamilyMember> getPosition() {
            return position;
        }

        public void setPosition(Position<FamilyMember> position) {
            this.position = position;
        }
    }


    //    private HashSet<FamilyMember> members;
    private HashMap<String, FamilyMember> members;
    private HashMap<String, LinkedTree<FamilyMember>> heads;
    private HashMap<String, String> names;

    public HashMap<String, LinkedTree<FamilyMember>> getHeads() {
        return heads;
    }

    public void loadFile(String pathToFile) throws IOException {
        File file = new File(pathToFile);

        String patternDeclaration = "^([\\d\\w]+)\\s?=\\s?(.+)\\s(.+) (\\([mMfF]\\)) (\\d+)$";
        String patternDeliminator = "^-+$";
        String patternRelationship = "^([\\d\\w]+)\\s?->\\s?([\\d\\w]+)";

        Pattern regex = Pattern.compile(patternDeclaration);

        Matcher matcher = null;

        // false if reading elements, true if reading relationships
        boolean readingStatus = false;

        BufferedReader br = new BufferedReader(new FileReader(file));
        String read;
        int m = -1;
        this.members = new HashMap<>();
        this.names = new HashMap<>();

        while ((read = br.readLine()) != null) {
            read = read.trim();
            matcher = regex.matcher(read);

            if (!readingStatus) {
                if (matcher.find()) {
                    readCharacter(matcher);
                } else {
                    matcher = Pattern.compile(patternDeliminator).matcher(read);
                    if (matcher.find()) {
                        m = Integer.parseInt(br.readLine());
                        heads = new HashMap<>(m);
                        for (int i = 0; i < m; i++) {
                            String headId = br.readLine().trim();
                            FamilyMember e = members.get(headId);
                            LinkedTree<FamilyMember> newFamily = new LinkedTree<>();
                            e.setPosition(newFamily.addRoot(e));
                            heads.put(e.getSurname(), newFamily);
                            e.setFamily(newFamily);
                        }
                        regex = Pattern.compile(patternRelationship);
                        readingStatus = true;
                    } else throw new IOException();

                }
            } else {
                if (matcher.find()) {
                    buildRelationship(matcher.group(1), matcher.group(2));
                } else throw new IOException("Connection not found.");
            }
        }

    }

    public LinkedTree<FamilyMember> getFamily(String surname) {
        return this.heads.get(surname);
    }

    public String findHeir(String surname) {
        LinkedTree<FamilyMember> family = this.heads.get(surname);
        Iterator<Position<FamilyMember>> it = family.iterator();
        Position<FamilyMember> prev = family.root();
        if (it.hasNext())
            it.next();
        else return null;

        FamilyMember candidate = null;
        while (it.hasNext()) {
            Position<FamilyMember> aux = it.next();

            if (family.parent(aux) != prev)
                return candidate.getName() + " " + candidate.getSurname();


            if ((!aux.getElement().isFemale() && (candidate == null || candidate.isFemale() || candidate.getAge() < aux.getElement().getAge()))
                    || (aux.getElement().isFemale() && (candidate == null || (candidate.isFemale() && candidate.getAge() < aux.getElement().getAge()))))
                candidate = aux.getElement();
        }
        return candidate.getName() + " " + candidate.getSurname();
    }

    public void changeFamily(String memberName, String newParent) {
        FamilyMember m = this.members.get(this.names.get(memberName.replace(' ', ':')));
        FamilyMember d = this.members.get(this.names.get(newParent.replace(' ', ':')));

        if (m.getFamily() != d.getFamily()) {
            m.getFamily().remove(m.getPosition());
            d.getFamily().add(m, d.getPosition());

            m.setFamily(d.getFamily());
        } else {
            m.getFamily().moveSubtree(m.getPosition(), d.getPosition());
        }
    }

    public boolean areFamily(String name1, String name2) throws RuntimeException {
        try {
            return (this.members.get(this.names.get(name1.trim().replace(' ', ':'))).getFamily() ==
                    this.members.get(this.names.get(name2.trim().replace(' ', ':'))).getFamily());
        } catch (RuntimeException e) {
            throw new RuntimeException("Please, input existing members");
        }

    }

    private void readCharacter(Matcher matcher) {
        String id = matcher.group(1);
        String name = matcher.group(2);
        String surname = matcher.group(3);
        boolean genre = (matcher.group(4).equals("(F)") || matcher.group(4).equals("(f)") || matcher.group(4).equals("F") || matcher.group(4).equals("f"));
        int age = Integer.parseInt(matcher.group(5));
        this.members.put(id, new FamilyMember(id, name, surname, genre, age));
        this.names.put(name + ':' + surname, id);
    }

    private void buildRelationship(String parent, String offspring) {
        FamilyMember memberA = members.get(parent);
        FamilyMember memberB = members.get(offspring);
        memberB.setPosition(memberA.getFamily().add(memberB, memberA.getPosition()));
        memberB.setFamily(memberA.getFamily());

    }
}

