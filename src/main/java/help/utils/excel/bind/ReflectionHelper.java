package help.utils.excel.bind;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import help.utils.excel.ExcelProcessingException;
import help.utils.excel.bind.annotation.ExcelTableElement;
import help.utils.excel.bind.annotation.ExcelTransient;

public class ReflectionHelper {
    public static List<Field> getAllAccessibleTableFieldsFromThisAndSuperClasses(Class<?> tableClass) {
        return getAllAccessibleFieldsFromThisAndSuperClasses(tableClass, true);
    }

    public static List<Field> getAllAccessibleFieldsFromThisAndSuperClasses(Class<?> tableClass) {
        return getAllAccessibleFieldsFromThisAndSuperClasses(tableClass, false);
    }

    private static List<Field> getAllAccessibleFieldsFromThisAndSuperClasses(Class<?> tableClass, boolean onlyTables) {
        List<Field> accessibleFields = new ArrayList<>();
        for (Class<?> clazz : getThisAndAllSuperClasses(tableClass)) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAnnotationPresent(ExcelTransient.class)) {
                    if (onlyTables && !isTableClassField(field)) {
                        continue;
                    }

                    boolean isLocalField = Objects.equals(field.getDeclaringClass(), clazz);
                    boolean isPublic = Modifier.isPublic(field.getModifiers());
                    boolean isProtected = Modifier.isProtected(field.getModifiers());
                    boolean isPackagePrivateAndAccessible =
                            !Modifier.isPrivate(field.getModifiers()) && !isPublic && !isProtected && field.getDeclaringClass().getPackage().getName().equals(clazz.getPackage().getName());
                    boolean isNotHiddenByChildClassField = accessibleFields.stream().noneMatch(f -> Objects.equals(field.getName(), f.getName()));

                    if ((isLocalField || isPublic || isProtected || isPackagePrivateAndAccessible) && isNotHiddenByChildClassField) {
                        accessibleFields.add(field);
                    }
                }
            }
        }
        return accessibleFields;
    }

    public static List<Class<?>> getThisAndAllSuperClasses(Class<?> clazz) {
        List<Class<?>> allSuperClasses = new ArrayList<>();
        allSuperClasses.add(clazz);
        while (clazz != null && clazz.getClasses() != null && !Objects.equals(clazz.getSuperclass(), Object.class)) {
            clazz = clazz.getSuperclass();
            allSuperClasses.add(clazz);
        }
        return allSuperClasses;
    }

    public static boolean isTableClassField(Field field) {
        return getThisAndAllSuperClasses(getFieldType(field)).stream().filter(Objects::nonNull).anyMatch(clazz -> clazz.isAnnotationPresent(ExcelTableElement.class));
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getFieldType(Field field) {
        if (List.class.isAssignableFrom(field.getType())) {
            return getGenericType(field);
        }
        return (Class<T>) field.getType();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericType(Field field) {
        try {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            Type[] typeArgs = parameterizedType.getActualTypeArguments();
            return (Class<T>) typeArgs[0];
        } catch (ClassCastException e) {
            throw new ExcelProcessingException("Can't get generic type of field " + field.getName(), e);
        }
    }

    public static <T> T getInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ExcelProcessingException(String.format("Failed to create instance of \"%s\" class.", clazz.getName()), e);
        }
    }

    //TODO-dchubkov: add getInt(), getLong(), etc... methods
    public static Object getFieldValue(Field field, Object classInstance) {
        if (field == null) {
            throw new ExcelProcessingException("Field should not be null");
        }

        if (classInstance == null) {
            throw new ExcelProcessingException(String.format("Unable to get value of the field \"%1$s\" with type \"%2$s\" from null class instance", field.getName(), field.getType()));
        }

        if (!field.isAccessible()) {
            //TODO-dchubkov: find appropriate getter method and use it for set value
            field.setAccessible(true);
        }

        try {
            return field.get(classInstance);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new ExcelProcessingException(String.format("Unable to get value of the field \"%1$s\" with type \"%2$s\" in class \"%3$s\"",
                    field.getName(), field.getType(), classInstance.getClass().getName()), e);
        }
    }

    public static void setFieldValue(Field field, Object classInstance, Object value) {
        if (field == null) {
            throw new ExcelProcessingException("Field should not be null");
        }

        if (field.getType().isPrimitive() && value == null) {
            return; // unable to set null values to fields of primitive types, leave this field with its default type value
        }

        if (classInstance == null) {
            throw new ExcelProcessingException(String.format("Unable to set value to the field \"%1$s\" with type \"%2$s\" in null class instance", field.getName(), field.getType()));
        }

        if (!field.isAccessible()) {
            //TODO-dchubkov: find appropriate setter method and use it for set value
            field.setAccessible(true);
        }

        try {
            field.set(classInstance, value);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new ExcelProcessingException(String.format("Unable to set value \"%1$s\" to the field \"%2$s\" with type \"%3$s\" in class \"%4$s\"",
                    value != null ? value.toString() : null, field.getName(), field.getType(), classInstance.getClass().getName()), e);
        }
    }

    public static List<?> getValueAsList(Field tableField, Object classInstance) {
        return getValueAsList(getFieldValue(tableField, classInstance));
    }

    public static List<?> getValueAsList(Object value) {
        if (value == null) {
            return null;
        }
        if (List.class.isAssignableFrom(value.getClass())) {
            return (List<?>) value;
        }
        return Collections.singletonList(value);
    }
}
