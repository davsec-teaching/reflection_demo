import com.ecs160.MyAnnotation;
import com.ecs160.Student;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReflectionDemo {

    private static void listAllClassesAllMethods() {
        String packageName = "com.ecs160";
        // Convert package name to a directory path
        String packagePath = packageName.replace('.', '/');
        File directory = new File("target/classes/" + packagePath);

        if (directory.exists()) {
            List<Class<?>> classes = new ArrayList<>();

            // List all .class files in the directory
            for (String fileName : directory.list()) {
                if (fileName.endsWith(".class")) {
                    // Remove .class extension to get class name
                    String className = fileName.substring(0, fileName.length() - 6);
                    String fullClassName = packageName + "." + className;

                    // Load class using reflection
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(fullClassName);
                        classes.add(clazz);
                        System.out.println("Loaded class: " + clazz.getName());
                        for (Method method: clazz.getMethods()) {
                            System.out.println("has method: " + method.getName());
                        }
                    } catch (ClassNotFoundException e) {
                        System.out.println("Failed to load class " + clazz);
                    }

                }
            }
        } else {
            System.out.println("Directory does not exist!");
        }
    }

    private static void invokeAllMethods(Object obj) {
        Class clazz = obj.getClass();
        for (Method m: clazz.getDeclaredMethods()) { // Only the declared methods, not the inherited methods
            if (m.getName().startsWith("get")) {
                try {
                    Object returnObject = m.invoke(obj);
                    System.out.println("Getter method: " + m.getName());
                    System.out.println("Getter returned: " + returnObject);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.println("Something went wrong...");
                }
            }
        }

    }

    private static void initializePrivateField(Object obj, Object objVal) {
        Class clazz = obj.getClass();
        for (Field field: clazz.getDeclaredFields()) {
            System.out.println(field.getName());
            if (field.getName().equals("active")) {
                field.setAccessible(true); // ignores access modifiers
                try {
                    field.set(obj, objVal);
                } catch (Exception e) {
                    System.out.println("Failed to set the field");;
                }
            }
        }
    }
    public static void main(String[] args) throws IllegalAccessException {
        listAllClassesAllMethods();
        Student student = new Student(100, "Student1");
        invokeAllMethods(student);
        initializePrivateField(student, false);
        System.out.println("Student is active? " + student.getActive());
        // System.out.println("Trying to access the active field directly" + student.active); // compiler error
        readAnnotatedFields(student);

        /*
        Class clazz = student.getClass();
        for (Field f: clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(com.ecs160.Persistable)) {
                f.setAccessible(true);
                Object v = f.get(student);
                Map<String, String> redisRecord = new ArrayList<>();
                redisRecord.put(f.getName(), v);
                jedis.hset(redisRecord);
            }
        }
         */
    }

    private static void readAnnotatedFields(Student student) {
        Class clazz = student.getClass();

        for (Field field: clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(MyAnnotation.class)) {
                Object value = null;
                try {
                    field.setAccessible(true);
                    value = field.get(student);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Printing annotated field " + field.getName() + " with value: " + value);
            }
        }
    }
}
