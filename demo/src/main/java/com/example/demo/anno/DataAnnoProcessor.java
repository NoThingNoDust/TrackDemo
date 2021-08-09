package com.example.demo.anno;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.example.demo.anno.TestData")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DataAnnoProcessor extends AbstractProcessor {
    private JavacTrees javacTrees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        javacTrees = JavacTrees.instance(processingEnv);
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(TestData.class);
        for (Element element : set) {
            JCTree tree = javacTrees.getTree(element);
            tree.accept(new TreeTranslator(){
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    for (JCTree def : jcClassDecl.defs) {
                        if (def.getKind().equals(Tree.Kind.VARIABLE)) {
                            jcClassDecl.defs.prepend(genGetterMethod((JCTree.JCVariableDecl) def));
                            jcClassDecl.defs.prepend(genSetterMethod((JCTree.JCVariableDecl) def));
                        }
                    }
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
        return true;
    }

    private JCTree.JCMethodDecl genGetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
        JCTree.JCReturn returnStatement = treeMaker.Return(
                treeMaker.Select(
                        treeMaker.Ident(
                                names.fromString("this")),
                        jcVariableDecl.getName()));
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(returnStatement);
        // public 方法访问级别修饰符
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名(getXxx), 根据字段名生成首字母大写的 get 方法
        Name methodName = getMethodName(jcVariableDecl.getName());
        // 返回值类型，get 方法的返回值类型与字段类型一样
        JCTree.JCExpression returnMethodType = jcVariableDecl.vartype;
        // 生成方法体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameterList = List.nil();
        // 异常抛出列表
        List<JCTree.JCExpression> thrownCauseList = List.nil();

        return treeMaker.MethodDef(
                modifiers,              // 方法访问级别修饰符
                methodName,             // get 方法名
                returnMethodType,       // 返回值类型
                methodGenericParamList, // 泛型参数列表
                parameterList,          // 参数值列表
                thrownCauseList,        // 异常抛出列表
                body,                   // 方法体
                null        // 默认
        );
    }

    private JCTree.JCMethodDecl genSetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
        JCTree.JCExpressionStatement statement = treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("this")),
                                jcVariableDecl.getName()
                        ),
                        treeMaker.Ident(jcVariableDecl.getName())
                )
        );
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<JCTree.JCStatement>().append(statement);

        JCTree.JCVariableDecl param = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),               //访问修饰符
                jcVariableDecl.getName(),                           //变量名
                jcVariableDecl.vartype,                             //变量类型
                null                                    //变量初始值
        );


        // public 方法访问级别修饰符
        JCTree.JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名(setXxx), 根据字段名生成首字母大写的 get 方法
        Name methodName = setMethodName(jcVariableDecl.getName());
        // 返回值类型，get 方法的返回值类型与字段类型一样
        JCTree.JCExpression returnMethodType = treeMaker.Type(new Type.JCVoidType());
        // 生成方法体
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        // 泛型参数列表
        List<JCTree.JCTypeParameter> methodGenericParamList = List.nil();
        // 参数值列表
        List<JCTree.JCVariableDecl> parameterList = List.of(param);
        // 异常抛出列表
        List<JCTree.JCExpression> thrownCauseList = List.nil();

        return treeMaker.MethodDef(
                modifiers,              // 方法访问级别修饰符
                methodName,             // set 方法名
                returnMethodType,       // 返回值类型
                methodGenericParamList, // 泛型参数列表
                parameterList,          // 参数值列表
                thrownCauseList,        // 异常抛出列表
                body,                   // 方法体
                null        // 默认
        );
    }

    private Name setMethodName(Name name) {
        String fieldName = name.toString();
        return names.fromString("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, name.length()));
    }

    private Name getMethodName(Name name) {
        String fieldName = name.toString();
        return names.fromString("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, name.length()));
    }



}
