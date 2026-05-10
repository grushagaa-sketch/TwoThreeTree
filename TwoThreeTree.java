package семестровая_работа_2;

import java.util.*;

public class TwoThreeTree {
    Node23 root;
    int opCount;

    public TwoThreeTree() {
        root = null;
        opCount = 0;
    }

    public void resetOps() { opCount = 0; }
    public int getOps() { return opCount; }

    private void addOp() { opCount++; }

    // ==================== ПОИСК ====================
    public boolean search(int key) {
        if (root == null) return false;

        Node23 cur = root;

        while (true) {
            addOp(); // вход в узел

            if (cur.isLeaf) {
                for (int i = 0; i < cur.keyCount; i++) {
                    addOp(); // сравнение с ключом
                    if (cur.keys[i] == key) {
                        return true;
                    }
                }
                return false;
            }

            // внутренний узел
            addOp(); // сравнение с первым ключом
            if (key < cur.keys[0]) {
                cur = cur.children[0];
            } else if (cur.keyCount == 1) {
                cur = cur.children[1];
            } else {
                addOp(); // сравнение со вторым ключом
                if (key < cur.keys[1]) {
                    cur = cur.children[1];
                } else {
                    cur = cur.children[2];
                }
            }
        }
    }

    // ==================== ВСТАВКА ====================
    public void insert(int key) {
        addOp(); // вызов insert

        if (root == null) {
            root = new Node23();
            root.keys[0] = key;
            root.keyCount = 1;
            return;
        }

        Node23 cur = root;
        Stack<Node23> stack = new Stack<>();

        // спуск к листу
        while (!cur.isLeaf) {
            addOp(); // операция выбора ветки
            stack.push(cur);

            addOp(); // сравнение с первым ключом
            if (key < cur.keys[0]) {
                cur = cur.children[0];
            } else if (cur.keyCount == 1) {
                cur = cur.children[1];
            } else {
                addOp(); // сравнение со вторым ключом
                if (key < cur.keys[1]) {
                    cur = cur.children[1];
                } else {
                    cur = cur.children[2];
                }
            }
        }

        // проверяем, есть ли ключ уже в листе
        for (int i = 0; i < cur.keyCount; i++) {
            addOp(); // сравнение при проверке
            if (cur.keys[i] == key) {
                return; // ключ уже есть
            }
        }

        // вставка в лист
        if (cur.keyCount == 1) {
            addOp(); // операция вставки
            if (key < cur.keys[0]) {
                cur.keys[1] = cur.keys[0];
                cur.keys[0] = key;
            } else {
                cur.keys[1] = key;
            }
            cur.keyCount = 2;
            return;
        }

        // SPLIT - лист переполнен (3 ключа)
        addOp(); // операция split

        int[] three = {cur.keys[0], cur.keys[1], key};
        Arrays.sort(three);

        Node23 left = new Node23();
        Node23 right = new Node23();
        left.isLeaf = true;
        right.isLeaf = true;
        left.keys[0] = three[0];
        left.keyCount = 1;
        right.keys[0] = three[2];
        right.keyCount = 1;
        int mid = three[1];

        // если это был корень
        if (stack.isEmpty()) {
            addOp(); // создание нового корня
            Node23 newRoot = new Node23();
            newRoot.isLeaf = false;
            newRoot.keys[0] = mid;
            newRoot.keyCount = 1;
            newRoot.children[0] = left;
            newRoot.children[1] = right;
            root = newRoot;
            return;
        }

        // поднимаем средний ключ в родителя
        Node23 parent = stack.pop();
        insertIntoParent(parent, mid, left, right, stack);
    }

    private void insertIntoParent(Node23 parent, int mid, Node23 left, Node23 right, Stack<Node23> stack) {
        addOp(); // вход в insertIntoParent

        if (parent.keyCount == 1) {
            addOp(); // вставка в родителя
            if (mid < parent.keys[0]) {
                parent.keys[1] = parent.keys[0];
                parent.keys[0] = mid;
                parent.children[2] = parent.children[1];
                parent.children[0] = left;
                parent.children[1] = right;
            } else {
                parent.keys[1] = mid;
                parent.children[1] = left;
                parent.children[2] = right;
            }
            parent.keyCount = 2;
        } else {
            // родитель тоже переполнен - нужен split на уровне родителя
            addOp(); // split родителя

            int[] pKeys = {parent.keys[0], parent.keys[1], mid};
            Arrays.sort(pKeys);

            Node23 newLeft = new Node23();
            Node23 newRight = new Node23();
            newLeft.isLeaf = false;
            newRight.isLeaf = false;
            newLeft.keys[0] = pKeys[0];
            newLeft.keyCount = 1;
            newRight.keys[0] = pKeys[2];
            newRight.keyCount = 1;
            int newMid = pKeys[1];

            // распределяем детей
            if (mid < parent.keys[0]) {
                newLeft.children[0] = left;
                newLeft.children[1] = right;
                newRight.children[0] = parent.children[1];
                newRight.children[1] = parent.children[2];
            } else if (mid < parent.keys[1]) {
                newLeft.children[0] = parent.children[0];
                newLeft.children[1] = left;
                newRight.children[0] = right;
                newRight.children[1] = parent.children[2];
            } else {
                newLeft.children[0] = parent.children[0];
                newLeft.children[1] = parent.children[1];
                newRight.children[0] = left;
                newRight.children[1] = right;
            }

            if (stack.isEmpty()) {
                addOp(); // создание нового корня
                Node23 newRoot = new Node23();
                newRoot.isLeaf = false;
                newRoot.keys[0] = newMid;
                newRoot.keyCount = 1;
                newRoot.children[0] = newLeft;
                newRoot.children[1] = newRight;
                root = newRoot;
            } else {
                Node23 grandParent = stack.pop();
                insertIntoParent(grandParent, newMid, newLeft, newRight, stack);
            }
        }
    }

    // ==================== УДАЛЕНИЕ ====================
    public void delete(int key) {
        addOp(); // вызов delete

        if (root == null) return;

        Node23 cur = root;
        Stack<Node23> stack = new Stack<>();

        // спуск к листу
        while (!cur.isLeaf) {
            addOp(); // операция выбора ветки
            stack.push(cur);

            addOp(); // сравнение с первым ключом
            if (key < cur.keys[0]) {
                cur = cur.children[0];
            } else if (cur.keyCount == 1) {
                cur = cur.children[1];
            } else {
                addOp(); // сравнение со вторым ключом
                if (key < cur.keys[1]) {
                    cur = cur.children[1];
                } else {
                    cur = cur.children[2];
                }
            }
        }

        // ищем и удаляем ключ в листе
        for (int i = 0; i < cur.keyCount; i++) {
            addOp(); // сравнение при поиске
            if (cur.keys[i] == key) {
                addOp(); // операция удаления
                if (cur.keyCount == 2) {
                    if (i == 0) {
                        cur.keys[0] = cur.keys[1];
                    }
                    cur.keyCount = 1;
                } else {
                    cur.keyCount = 0;
                }
                return;
            }
        }
    }
}
