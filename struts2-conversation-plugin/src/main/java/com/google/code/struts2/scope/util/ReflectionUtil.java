package com.google.code.struts2.scope.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.util.ClassLoaderUtils;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author rees.byars
 */
public class ReflectionUtil {

	public static Set<Field> getFields(Class<?> clazz) {
		Set<Field> fields = new HashSet<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			if (!superClass.equals(Object.class) && !superClass.equals(ActionSupport.class)) {
				fields.addAll(getFields(superClass));
			}
		}
		return fields;
	}

	public static Set<Method> getMethods(Class<?> clazz) {
		Set<Method> methods = new HashSet<Method>();
		methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			if (!superClass.equals(Object.class) && !superClass.equals(ActionSupport.class)) {
				methods.addAll(getMethods(superClass));
			}
		}
		return methods;
	}

	public static Set<Class<?>> getClasses(Class<?> clazz) {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.addAll(Arrays.asList(clazz.getDeclaredClasses()));
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			classes.addAll(getClasses(superClass));
		}
		return classes;
	}

	public static <T extends Annotation> Set<T> getAnnotationInstances(Class<?> clazz, Class<T> annotationClass) {
		Set<T> annotationInstances = new HashSet<T>();
		for (Class<?> clazzClass : clazz.getInterfaces()) {
			if (clazzClass.isAnnotationPresent(annotationClass)) {
				annotationInstances.add(clazzClass.getAnnotation(annotationClass));
			}
		}
		if (clazz.isAnnotationPresent(annotationClass)) {
			annotationInstances.add(clazz.getAnnotation(annotationClass));
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null) {
			annotationInstances.addAll(getAnnotationInstances(superClass, annotationClass));
		}
		return annotationInstances;
	}

	public static void makeAccessible(Field field) {
		if ((Modifier.isPublic(field.getModifiers()))
				&& (Modifier.isPublic(field.getDeclaringClass().getModifiers()))) {
			return;
		}
		field.setAccessible(true);
	}
	
	public static boolean classExists(String className) {
		boolean exists = false;
		try {
			ClassLoaderUtils.loadClass(className, ReflectionUtil.class);
			exists = true;
		} catch (ClassNotFoundException e) {}
		return exists;
	}

}