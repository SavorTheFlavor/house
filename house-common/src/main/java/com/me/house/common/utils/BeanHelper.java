package com.me.house.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Created by Administrator on 2018/8/5.
 */
public class BeanHelper {

    private static final String updateTimeKey  = "updateTime";

    private static final String createTimeKey  = "createTime";

    public static <T> void setDefaultProp(T target, Class<T> clazz){
        PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(clazz);
        for(PropertyDescriptor propertyDescriptor: descriptors) {
            String fieldName = propertyDescriptor.getName();
            Object value;
            try {
                value = PropertyUtils.getProperty(target, fieldName);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("can not set property  when get for " + target + " and clazz " + clazz + " field " + fieldName);
            }
            /*
                * Determines if the class or interface represented by this
                * {@code Class} object is either the same as, or is a superclass or
                * superinterface of, the class or interface represented by the specified
                * {@code Class} parameter. It returns {@code true} if so;
             */
            //判断该field是否是String类型并且是空值，如果是则为其设置默认值""
            if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
                try {
                    PropertyUtils.setProperty(target, fieldName, "");
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException("can not set property  when get for " + target + " and clazz " + clazz + " field " + fieldName);
                }
            } else if (Integer.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
                try {
                    PropertyUtils.setProperty(target, fieldName, -1);
                } catch (Exception e) {
                    throw new RuntimeException("can not set property when set for " + target + " and clazz " + clazz + " field " + fieldName);
                }
            }else if (Long.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
                try {
                    PropertyUtils.setProperty(target, fieldName, -1L);
                } catch (Exception e) {
                    throw new RuntimeException("can not set property when set for " + target + " and clazz " + clazz + " field " + fieldName);
                }
            }else if (Date.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && value == null) {
                try {
                    PropertyUtils.setProperty(target, fieldName, new Date());
                } catch (Exception e) {
                    throw new RuntimeException("can not set property when set for "+ target +" and clazz "+clazz + " field "+ fieldName);
                }
            }
        }
    }

    /**
     * set the target's updateTime property to System.currentTimeMillis()
     * @param target
     * @param <T>
     */
    public static <T> void onUpdate(T target){
        try {
            PropertyUtils.setProperty(target, updateTimeKey, System.currentTimeMillis());
            Class<T> clazz = (Class<T>)target.getClass();
            setDefaultProp(target, clazz);
        } catch (Exception e) {
        }
    }

    public static <T> void onInsert(T target){
        Class<T> clazz = (Class<T>)target.getClass();
        setDefaultProp(target, clazz);
    }
}
