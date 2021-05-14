package com.example.god.model.clazz;

import java.lang.reflect.Modifier;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author hengyunabc 2018-10-18
 *
 */
public class ClassUtils {

    public static String getCodeSource(final CodeSource cs) {
        if (null == cs || null == cs.getLocation() || null == cs.getLocation().getFile()) {
            return "";
        }

        return cs.getLocation().getFile();
    }

    public static boolean isLambdaClass(Class<?> clazz) {
        return clazz.getName().contains("$$Lambda$");
    }

//    public static Element renderClassInfo(ClassDetailVO clazz) {
//        return renderClassInfo(clazz, false, null);
//    }
//
//    public static Element renderClassInfo(ClassDetailVO clazz, boolean isPrintField, Integer expand) {
//        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
//
//        table.row(label("class-info").style(Decoration.bold.bold()), label(clazz.getClassInfo()))
//                .row(label("code-source").style(Decoration.bold.bold()), label(clazz.getCodeSource()))
//                .row(label("name").style(Decoration.bold.bold()), label(clazz.getName()))
//                .row(label("isInterface").style(Decoration.bold.bold()), label("" + clazz.isInterface()))
//                .row(label("isAnnotation").style(Decoration.bold.bold()), label("" + clazz.isAnnotation()))
//                .row(label("isEnum").style(Decoration.bold.bold()), label("" + clazz.isEnum()))
//                .row(label("isAnonymousClass").style(Decoration.bold.bold()), label("" + clazz.isAnonymousClass()))
//                .row(label("isArray").style(Decoration.bold.bold()), label("" + clazz.isArray()))
//                .row(label("isLocalClass").style(Decoration.bold.bold()), label("" + clazz.isLocalClass()))
//                .row(label("isMemberClass").style(Decoration.bold.bold()), label("" + clazz.isMemberClass()))
//                .row(label("isPrimitive").style(Decoration.bold.bold()), label("" + clazz.isPrimitive()))
//                .row(label("isSynthetic").style(Decoration.bold.bold()), label("" + clazz.isSynthetic()))
//                .row(label("simple-name").style(Decoration.bold.bold()), label(clazz.getSimpleName()))
//                .row(label("modifier").style(Decoration.bold.bold()), label(clazz.getModifier()))
//                .row(label("annotation").style(Decoration.bold.bold()), label(StringUtils.join(clazz.getAnnotations(), ",")))
//                .row(label("interfaces").style(Decoration.bold.bold()), label(StringUtils.join(clazz.getInterfaces(), ",")))
//                .row(label("super-class").style(Decoration.bold.bold()), TypeRenderUtils.drawSuperClass(clazz))
//                .row(label("class-loader").style(Decoration.bold.bold()), TypeRenderUtils.drawClassLoader(clazz))
//                .row(label("classLoaderHash").style(Decoration.bold.bold()), label(clazz.getClassLoaderHash()));
//
//        if (isPrintField) {
//            table.row(label("fields").style(Decoration.bold.bold()), TypeRenderUtils.drawField(clazz, expand));
//        }
//        return table;
//    }

    public static ClassDetailVO createClassInfo(Class clazz, boolean withFields) {
        CodeSource cs = clazz.getProtectionDomain().getCodeSource();
        ClassDetailVO classInfo = new ClassDetailVO();
        classInfo.setName(classname(clazz));
        classInfo.setClassInfo(classname(clazz));
        classInfo.setCodeSource(ClassUtils.getCodeSource(cs));
        classInfo.setInterface(clazz.isInterface());
        classInfo.setAnnotation(clazz.isAnnotation());
        classInfo.setEnum(clazz.isEnum());
        classInfo.setAnonymousClass(clazz.isAnonymousClass());
        classInfo.setArray(clazz.isArray());
        classInfo.setLocalClass(clazz.isLocalClass());
        classInfo.setMemberClass(clazz.isMemberClass());
        classInfo.setPrimitive(clazz.isPrimitive());
        classInfo.setSynthetic(clazz.isSynthetic());
        classInfo.setSimpleName(clazz.getSimpleName());
        classInfo.setModifier(modifier(clazz.getModifiers(), ','));
        classInfo.setAnnotations(TypeRenderUtils.getAnnotations(clazz));
        classInfo.setInterfaces(TypeRenderUtils.getInterfaces(clazz));
        classInfo.setSuperClass(TypeRenderUtils.getSuperClass(clazz));
        classInfo.setClassloader(TypeRenderUtils.getClassloader(clazz));
        classInfo.setClassLoaderHash(classLoaderHash(clazz));
        if (withFields) {
            classInfo.setFields(TypeRenderUtils.getFields(clazz));
        }
        return classInfo;
    }

    public static ClassVO createSimpleClassInfo(Class clazz) {
        ClassVO classInfo = new ClassVO();
        fillSimpleClassVO(clazz, classInfo);
        return classInfo;
    }

    public static void fillSimpleClassVO(Class clazz, ClassVO classInfo) {
        classInfo.setName(classname(clazz));
        classInfo.setClassloader(getClassloader(clazz));
        classInfo.setClassLoaderHash(classLoaderHash(clazz));
    }

    public static String[] getClassloader(Class clazz) {
        List<String> list = new ArrayList<>();
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

    public static String classLoaderHash(Class<?> clazz) {
        if (clazz == null || clazz.getClassLoader() == null) {
            return "null";
        }
        return Integer.toHexString(clazz.getClassLoader().hashCode());
    }

    /**
     * 翻译Modifier值
     *
     * @param mod modifier
     * @return 翻译值
     */
    public static String modifier(int mod, char splitter) {
        StringBuilder sb = new StringBuilder();
        if (Modifier.isAbstract(mod)) {
            sb.append("abstract").append(splitter);
        }
        if (Modifier.isFinal(mod)) {
            sb.append("final").append(splitter);
        }
        if (Modifier.isInterface(mod)) {
            sb.append("interface").append(splitter);
        }
        if (Modifier.isNative(mod)) {
            sb.append("native").append(splitter);
        }
        if (Modifier.isPrivate(mod)) {
            sb.append("private").append(splitter);
        }
        if (Modifier.isProtected(mod)) {
            sb.append("protected").append(splitter);
        }
        if (Modifier.isPublic(mod)) {
            sb.append("public").append(splitter);
        }
        if (Modifier.isStatic(mod)) {
            sb.append("static").append(splitter);
        }
        if (Modifier.isStrict(mod)) {
            sb.append("strict").append(splitter);
        }
        if (Modifier.isSynchronized(mod)) {
            sb.append("synchronized").append(splitter);
        }
        if (Modifier.isTransient(mod)) {
            sb.append("transient").append(splitter);
        }
        if (Modifier.isVolatile(mod)) {
            sb.append("volatile").append(splitter);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }


//    public static MethodVO createMethodInfo(Method method, Class clazz, boolean detail) {
//        MethodVO methodVO = new MethodVO();
//        methodVO.setDeclaringClass(clazz.getName());
//        methodVO.setMethodName(method.getName());
//        methodVO.setDescriptor(Type.getMethodDescriptor(method));
//        methodVO.setConstructor(false);
//        if (detail) {
//            methodVO.setModifier(StringUtils.modifier(method.getModifiers(), ','));
//            methodVO.setAnnotations(TypeRenderUtils.getAnnotations(method.getDeclaredAnnotations()));
//            methodVO.setParameters(getClassNameList(method.getParameterTypes()));
//            methodVO.setReturnType(StringUtils.classname(method.getReturnType()));
//            methodVO.setExceptions(getClassNameList(method.getExceptionTypes()));
//            methodVO.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
//        }
//        return methodVO;
//    }

//    public static MethodVO createMethodInfo(Constructor constructor, Class clazz, boolean detail) {
//        MethodVO methodVO = new MethodVO();
//        methodVO.setDeclaringClass(clazz.getName());
//        methodVO.setDescriptor(Type.getConstructorDescriptor(constructor));
//        methodVO.setMethodName("<init>");
//        methodVO.setConstructor(true);
//        if (detail) {
//            methodVO.setModifier(StringUtils.modifier(constructor.getModifiers(), ','));
//            methodVO.setAnnotations(TypeRenderUtils.getAnnotations(constructor.getDeclaredAnnotations()));
//            methodVO.setParameters(getClassNameList(constructor.getParameterTypes()));
//            methodVO.setExceptions(getClassNameList(constructor.getExceptionTypes()));
//            methodVO.setClassLoaderHash(StringUtils.classLoaderHash(clazz));
//        }
//        return methodVO;
//    }

//    public static Element renderMethod(MethodVO method) {
//        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
//        table.row(label("declaring-class").style(bold.bold()), label(method.getDeclaringClass()))
//                .row(label("method-name").style(bold.bold()), label(method.getMethodName()).style(bold.bold()))
//                .row(label("modifier").style(bold.bold()), label(method.getModifier()))
//                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(method.getAnnotations())))
//                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(method.getParameters())))
//                .row(label("return").style(bold.bold()), label(method.getReturnType()))
//                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(method.getExceptions())))
//                .row(label("classLoaderHash").style(bold.bold()), label(method.getClassLoaderHash()));
//        return table;
//    }

//    public static Element renderConstructor(MethodVO constructor) {
//        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
//        table.row(label("declaring-class").style(bold.bold()), label(constructor.getDeclaringClass()))
//                .row(label("constructor-name").style(bold.bold()), label("<init>").style(bold.bold()))
//                .row(label("modifier").style(bold.bold()), label(constructor.getModifier()))
//                .row(label("annotation").style(bold.bold()), label(TypeRenderUtils.drawAnnotation(constructor.getAnnotations())))
//                .row(label("parameters").style(bold.bold()), label(TypeRenderUtils.drawParameters(constructor.getParameters())))
//                .row(label("exceptions").style(bold.bold()), label(TypeRenderUtils.drawExceptions(constructor.getExceptions())))
//                .row(label("classLoaderHash").style(bold.bold()), label(constructor.getClassLoaderHash()));
//        return table;
//    }

    /**
     * 翻译类名称
     *
     * @param clazz Java类
     * @return 翻译值
     */
    public static String classname(Class<?> clazz) {
        if (clazz.isArray()) {
            StringBuilder sb = new StringBuilder(clazz.getName());
            sb.delete(0, 2);
            if (sb.length() > 0 && sb.charAt(sb.length() - 1) == ';') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("[]");
            return sb.toString();
        } else {
            return clazz.getName();
        }
    }

    public static String[] getClassNameList(Class[] classes) {
        List<String> list = new ArrayList<String>();
        for (Class anInterface : classes) {
            list.add(classname(anInterface));
        }
        return list.toArray(new String[0]);
    }

    public static List<ClassVO> createClassVOList(Set<Class<?>> matchedClasses) {
        List<ClassVO> classVOs = new ArrayList<ClassVO>(matchedClasses.size());
        for (Class<?> aClass : matchedClasses) {
            ClassVO classVO = createSimpleClassInfo(aClass);
            classVOs.add(classVO);
        }
        return classVOs;
    }

    public static ClassLoaderVO createClassLoaderVO(ClassLoader classLoader) {
        ClassLoaderVO classLoaderVO = new ClassLoaderVO();
        classLoaderVO.setHash(classLoaderHash(classLoader));
        classLoaderVO.setName(classLoader==null?"BootstrapClassLoader":classLoader.toString());
        ClassLoader parent = classLoader == null ? null : classLoader.getParent();
        classLoaderVO.setParent(parent==null?null:parent.toString());
        return classLoaderVO;
    }

    public static List<ClassLoaderVO> createClassLoaderVOList(Collection<ClassLoader> classLoaders) {
        List<ClassLoaderVO> classLoaderVOList = new ArrayList<ClassLoaderVO>();
        for (ClassLoader classLoader : classLoaders) {
            classLoaderVOList.add(createClassLoaderVO(classLoader));
        }
        return classLoaderVOList;
    }


    public static String classLoaderHash(ClassLoader classLoader) {
        if (classLoader == null ) {
            return "null";
        }
        return Integer.toHexString(classLoader.hashCode());
    }

//    public static Element renderMatchedClasses(Collection<ClassVO> matchedClasses) {
//        TableElement table = new TableElement().leftCellPadding(1).rightCellPadding(1);
//        table.row(new LabelElement("NAME").style(Decoration.bold.bold()),
//                new LabelElement("HASHCODE").style(Decoration.bold.bold()),
//                new LabelElement("CLASSLOADER").style(Decoration.bold.bold()));
//
//        for (ClassVO c : matchedClasses) {
//            table.row(label(c.getName()),
//                    label(c.getClassLoaderHash()).style(Decoration.bold.fg(Color.red)),
//                    TypeRenderUtils.drawClassLoader(c));
//        }
//        return table;
//    }

}
