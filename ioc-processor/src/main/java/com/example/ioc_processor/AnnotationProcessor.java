package com.example.ioc_processor;


import com.example.ioc_annotation.BindView;
import com.google.auto.service.AutoService;


import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.annotation.processing.Filer;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.example.ioc_annotation.BindView")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {
    private Filer mFilterUtils;
    private Elements mElementsUtils;
    private Messager mMessager;
    private Map<String, ProxyInfo> mProxyInfoMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFilterUtils = processingEnvironment.getFiler();
        mElementsUtils = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process...");
        mProxyInfoMap.clear();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            String qualifiedName = typeElement.getQualifiedName().toString();
            ProxyInfo proxyInfo = mProxyInfoMap.get(qualifiedName);
            if (proxyInfo == null) {
                proxyInfo = new ProxyInfo(mElementsUtils, typeElement);
                mProxyInfoMap.put(qualifiedName, proxyInfo);


            }

            BindView annotation = variableElement.getAnnotation(BindView.class);
            if (annotation != null) {
                int id = annotation.value();
                proxyInfo.mInjectElements.put(id, variableElement);
            }
        }

        for (String key : mProxyInfoMap.keySet()) {
            ProxyInfo proxyInfo = mProxyInfoMap.get(key);
            try {
                JavaFileObject sourceFile = mFilterUtils.createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.getTypeElement());
                Writer writer = sourceFile.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();

            }

        }
        return false;
    }

    private static class ProxyInfo {
        private static final String SUFFIX = "_ViewBinding";
        private Map<Integer, VariableElement> mInjectElements = new HashMap<>();
        private TypeElement mTypeElement;
        private String mPackageName;
        private String mProxyClassName;



        private ProxyInfo(Elements elementUtil, TypeElement typeElement) {
            mTypeElement = typeElement;
            PackageElement packageElement = elementUtil.getPackageOf(typeElement);
            mPackageName = packageElement.getQualifiedName().toString();
            String className = getClassName(typeElement,mPackageName);
            mProxyClassName = className + SUFFIX;


        }

        private String getClassName(final  TypeElement typeElement,final  String packageName){
            int packageLength = packageName.length() +1;
            return typeElement.getQualifiedName().toString().substring(packageLength).replace(".","$");
        }

        private String getProxyClassFullName() {
            return mPackageName + "." + mProxyClassName;
        }

        private TypeElement getTypeElement() {
            return mTypeElement;
        }

        private String generateJavaCode(){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("//Generate code. Do not modify it ! \n")
                    .append("package ").append(mPackageName).append(";\n\n")
                    .append("import com.example.ioc.*;\n\n")
                    .append("public class ").append(mProxyClassName)
                    .append(" implements ")
                    .append("ViewInject")
                    .append("<")
                    .append(mTypeElement.getQualifiedName())
                    .append(">")
                    .append("{\n");
            generateMethod(stringBuilder);
            stringBuilder.append("\n}\n");
            return stringBuilder.toString();

        }

        private void generateMethod(StringBuilder stringBuilder){
            if (stringBuilder == null) {
                return;
            }
            stringBuilder.append("@Override\n")
                    .append("public void bind(").append(mTypeElement.getQualifiedName()).append(" host, Object object )").append("{\n");

            for (Integer id : mInjectElements.keySet()) {
                VariableElement variableElement = mInjectElements.get(id);
                String name = variableElement.getSimpleName().toString();
                String type = variableElement.asType().toString();
                stringBuilder.append("if(object instanceof android.app.Activity)").append("{\n")
                        .append("host.").append(name).append(" = ")
                        .append("(").append(type).append(")((android.app.Activity)object).findViewById(").append(id).append(");")
                        .append("\n}\n")
                        .append("else").append("{\n")
                        .append("host.").append(name).append(" = ")
                        .append("(").append(type).append(")((android.view.View)object).findViewById(").append(id).append(");")
                        .append("\n}\n");
            }
            stringBuilder.append("\n}\n");
        }

    }
}
