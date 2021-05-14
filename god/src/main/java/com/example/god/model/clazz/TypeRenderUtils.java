package com.example.god.model.clazz;

import org.thymeleaf.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author beiwei30 on 24/11/2016.
 */
public class TypeRenderUtils {

    public static String drawInterface(Class<?> clazz) {
        return StringUtils.concat(",", clazz.getInterfaces());
    }

    public static String drawParameters(Method method) {
        return StringUtils.concat("\n", method.getParameterTypes());
    }

    public static String drawParameters(Constructor constructor) {
        return StringUtils.concat("\n", constructor.getParameterTypes());
    }

    public static String drawParameters(String[] parameterTypes) {
        return StringUtils.concat("\n", parameterTypes);
    }

    public static String drawReturn(Method method) {
        return ClassUtils.classname(method.getReturnType());
    }

    public static String drawExceptions(Method method) {
        return StringUtils.concat("\n", method.getExceptionTypes());
    }

    public static String drawExceptions(Constructor constructor) {
        return StringUtils.concat("\n", constructor.getExceptionTypes());
    }

    public static String drawExceptions(String[] exceptionTypes) {
        return StringUtils.concat("\n", exceptionTypes);
    }



    public static String[] getAnnotations(Class<?> clazz) {
        return getAnnotations(clazz.getDeclaredAnnotations());
    }

    public static String[] getAnnotations(Annotation[] annotations) {
        List<String> list = new ArrayList<String>();
        if (annotations != null && annotations.length > 0) {
            for (Annotation annotation : annotations) {
                list.add(ClassUtils.classname(annotation.annotationType()));
            }
        }
        return list.toArray(new String[0]);
    }

    public static String[] getInterfaces(Class clazz) {
        Class[] interfaces = clazz.getInterfaces();
        return ClassUtils.getClassNameList(interfaces);
    }

    public static String[] getSuperClass(Class clazz) {
        List<String> list = new ArrayList<String>();
        Class<?> superClass = clazz.getSuperclass();
        if (null != superClass) {
            list.add(ClassUtils.classname(superClass));
            while (true) {
                superClass = superClass.getSuperclass();
                if (null == superClass) {
                    break;
                }
                list.add(ClassUtils.classname(superClass));
            }
        }
        return list.toArray(new String[0]);
    }

    public static String[] getClassloader(Class clazz) {
        List<String> list = new ArrayList<String>();
        ClassLoader loader = clazz.getClassLoader();
        if (null != loader) {
            list.add(loader.toString());
            while (true) {
                loader = loader.getParent();
                if (null == loader) {
                    break;
                }
                list.add(loader.toString());
            }
        }
        return list.toArray(new String[0]);
    }

    public static FieldVO[] getFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return new FieldVO[0];
        }

        List<FieldVO> list = new ArrayList<FieldVO>(fields.length);
        for (Field field : fields) {
            FieldVO fieldVO = new FieldVO();
            fieldVO.setName(field.getName());
            fieldVO.setType(ClassUtils.classname(field.getType()));
            fieldVO.setModifier(ClassUtils.modifier(field.getModifiers(), ','));
            fieldVO.setAnnotations(getAnnotations(field.getAnnotations()));
            if (Modifier.isStatic(field.getModifiers())) {
                fieldVO.setStatic(true);
                fieldVO.setValue(getFieldValue(field));
            } else {
                fieldVO.setStatic(false);
            }
            list.add(fieldVO);
        }
        return list.toArray(new FieldVO[0]);
    }

    private static Object getFieldValue(Field field) {
        final boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            Object value = field.get(null);
            return value;
        } catch (IllegalAccessException e) {
            // no op
        } finally {
            field.setAccessible(isAccessible);
        }
        return null;
    }

}
