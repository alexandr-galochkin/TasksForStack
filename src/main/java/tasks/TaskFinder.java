package tasks;

import java.util.*;

class TaskFinder {
    static class Node {
        Node(int id, String name, Integer priority, List<Node> children) {
            this.id = id;
            this.name = name;
            this.priority = priority;
            this.children = children;
        }

        boolean isGroup() {
            return children != null;
        }

        int id;
        String name;
        Integer priority;
        List<Node> children;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node node = (Node) o;
            return id == node.id
                    && name.equals(node.name)
                    && Objects.equals(priority, node.priority)
                    && Objects.equals(children, node.children);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, priority, children);
        }
    }

    static Node task(int id, String name, int priority) {
        return new Node(id, name, priority, null);
    }

    static  Node group(int id, String name, Node... children) {
        return new Node(id, name, null, Arrays.asList(children));
    }


    static Node tasks =
            group(0, "Все задачи",
                    group(1, "Разработка",
                            task(2, "Планирование разработок", 1),
                            task(3, "Подготовка релиза", 4),
                            task(4, "Оптимизация", 2)),
                    group(5, "Тестирование",
                            group(6, "Ручное тестирование",
                                    task(7, "Составление тест-планов", 3),
                                    task(8, "Выполнение тестов", 6)),
                            group(9, "Автоматическое тестирование",
                                    task(10, "Составление тест-планов", 3),
                                    task(11, "Написание тестов", 3))),
                    group(12, "Аналитика"));


    static Optional<Node> findTaskHavingMaxPriorityInGroup(Node tasks, int groupId) throws Exception {
        Deque<Node> groups = new LinkedList<>();
        Node mainGroup = null;
        if (!tasks.isGroup()){
            throw new Exception();
        }
        groups.add(tasks);

        //Search our group
        while (!groups.isEmpty()){
            Node currentGroup = groups.pop();
                if (currentGroup.id == groupId){
                    mainGroup = currentGroup;
                    break;
                }
                for (Node currentChild: currentGroup.children) {
                    if (currentChild.isGroup()){
                        groups.addLast(currentChild);
                    }
                }
        }
        if (mainGroup == null){
            throw new Exception();
        }

        groups = new LinkedList<>();
        groups.add(mainGroup);
        int maxPriority = -1;
        Node taskWithMaxPriority = null;

        //search for the task with max priority among all the children in the mainGroup
        while (!groups.isEmpty()){
            Node currentGroup = groups.pop();
            for (Node currentChild: currentGroup.children) {
                if (currentChild.isGroup()){
                    groups.addLast(currentChild);
                } else {
                    if (currentChild.priority > maxPriority){
                        taskWithMaxPriority = currentChild;
                        maxPriority = currentChild.priority;
                    }
                }
            }
        }
        return Optional.ofNullable(taskWithMaxPriority);
    }


    static void testFindTaskHavingMaxPriorityInGroup() {
        TestRunner runner = new TestRunner("findTaskHavingMaxPriorityInGroup");

        runner.expectException(() -> findTaskHavingMaxPriorityInGroup(tasks, 13));
        runner.expectException(() -> findTaskHavingMaxPriorityInGroup(tasks, 2));

        runner.expectFalse(() -> findTaskHavingMaxPriorityInGroup(tasks, 12).isPresent());

        runner.expectTrue(() -> findTaskHavingMaxPriorityInGroup(tasks, 0).get()
                .equals(task(8, "Выполнение тестов", 6)));
        runner.expectTrue(() -> findTaskHavingMaxPriorityInGroup(tasks, 1).get()
                .equals(task(3, "Подготовка релиза", 4)));

        runner.expectTrue(() -> findTaskHavingMaxPriorityInGroup(tasks, 9).get().priority == 3);
    }
}
