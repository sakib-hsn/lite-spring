package com.sakib.io.litespring;

import com.sakib.io.litespring.annotation.Autowired;
import com.sakib.io.litespring.annotation.Component;
import com.sakib.io.litespring.annotation.RequestMapping;
import com.sakib.io.litespring.annotation.RestController;
import com.sakib.io.service.ProductService;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContext {
    private final Map<String, Object> beanFactory = new HashMap<>();
    private final int TOMCAT_PORT = 8080;
    private final TomCatConfig tomCatConfig;


    private static ApplicationContext applicationContext;

    private ApplicationContext() {
        tomCatConfig = new TomCatConfig(TOMCAT_PORT);
    }

    public static synchronized ApplicationContext getInstance() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }
        return applicationContext;
    }

    protected void createSpringContainer(List<Class<?>> classes) {
        try {
            beanCreates(classes);
            injectDependencies(classes);
            DispatcherServlet dispatcherServlet = new DispatcherServlet(findControllerMethods(classes));
            tomCatConfig.registerServlet(
                    dispatcherServlet, dispatcherServlet.getClass().getSimpleName(), "/");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected List<ControllerMethod> findControllerMethods(List<Class<?>> classes) {
        List<ControllerMethod> controllerMethods = new ArrayList<>();

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RestController.class)) {
                RestController restController = clazz.getAnnotation(RestController.class);
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String mappedUrl = restController.url() + requestMapping.url();
                        ControllerMethod controllerMethod = ControllerMethod.builder()
                                .clazz(clazz)
                                .method(method)
                                .methodType(requestMapping.type())
                                .url(mappedUrl)
                                .instance(beanFactory.get(clazz.getSimpleName()))
                                .build();
                        System.out.println("controllerMethod = " + controllerMethod);
                        controllerMethods.add(controllerMethod);
                    }
                }
            }
        }
        return controllerMethods;
    }


    protected void beanCreates(List<Class<?>> classes) throws Exception {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) ||
                    clazz.isAnnotationPresent(RestController.class)) {
                if (!beanFactory.containsKey(clazz.getSimpleName())) {
                    recursiveBeanCreate(clazz);
                }
            }
        }
    }

    private void recursiveBeanCreate(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("clazz.getSimpleName() = " + clazz.getSimpleName());
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];

        int paramCount = constructor.getParameterCount();
        Object[] paramObject = (paramCount > 0) ? new Object[paramCount] : new Object[0];

        if (paramCount > 0) {
            Parameter[] parameters = constructor.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                System.out.println("beanFactory.containsKey(parameter.getType().getSimpleName()) = " + beanFactory.containsKey(parameters[i].getType().getSimpleName()));
                if (!beanFactory.containsKey(parameters[i].getType().getSimpleName())) {
                    System.out.println("parameters[i].getType() = " + parameters[i].getType());
                    recursiveBeanCreate(parameters[i].getType());
                }
                paramObject[i] = getBean(parameters[i].getType().getSimpleName());
            }
        }

        Object instance = constructor.newInstance(paramObject);
        beanFactory.put(clazz.getSimpleName(), instance);
    }

    protected void injectDependencies(List<Class<?>> classes) throws IllegalAccessException {
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(Component.class) ||
                    clazz.isAnnotationPresent(RestController.class)) {

                Object bean = beanFactory.get(clazz.getSimpleName());

                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        Object instance = beanFactory.get(field.getType().getSimpleName());
                        field.setAccessible(true);
                        field.set(bean, instance);
                    }
                }
            }
        }
    }

    public Object getBean(Class<?> clazz) {
        return beanFactory.get(clazz.getSimpleName());
    }

    public Object getBean(String name) {
        return beanFactory.get(name);
    }
}
