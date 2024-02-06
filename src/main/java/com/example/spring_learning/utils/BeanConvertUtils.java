package com.example.spring_learning.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 转换对象工具
 *
 * @author bugpool
 */
public class BeanConvertUtils extends BeanUtils {
    
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier) {
        return convertTo(source, targetSupplier, null);
    }
    
    /**
     * 转换对象
     *
     * @param source         源对象
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象
     */
    public static <S, T> T convertTo(S source, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        // 检查源对象和目标对象供应方是否为null，如果是，则返回null
        if (null == source || null == targetSupplier) {
            return null;
        }
        // 从供应方获取目标对象
        T target = targetSupplier.get();
        // 使用Spring的BeanUtils.copyProperties方法将源对象的属性复制到目标对象
        copyProperties(source, target);
        // 如果提供了回调，则在复制完成后执行回调
        if (callBack != null) {
            callBack.callBack(source, target);
        }
        // 返回填充了源对象属性的目标对象
        return target;
    }
    
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier) {
        return convertListTo(sources, targetSupplier, null);
    }
    
    /**
     * 转换对象
     *
     * @param sources        源对象list
     * @param targetSupplier 目标对象供应方
     * @param callBack       回调方法
     * @param <S>            源对象类型
     * @param <T>            目标对象类型
     * @return 目标对象list
     */
    public static <S, T> List<T> convertListTo(List<S> sources, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        if (null == sources || null == targetSupplier) {
            return null;
        }
        
        // List<T> list = new ArrayList<>(sources.size());
        // for (S source : sources) {
        //     T target = targetSupplier.get();
        //     copyProperties(source, target);
        //     if (callBack != null) {
        //         callBack.callBack(source, target);
        //     }
        //     list.add(target);
        // }a
        return sources.stream().map(source -> {
            T target = targetSupplier.get();
            copyProperties(source, target);
            if (callBack != null) {
                callBack.callBack(source, target);
            }
            return target;
        }).collect(Collectors.toList());
    }
    
    public static <S, T> List<T> convertListTo2(List<S> sources, Supplier<T> targetSupplier, ConvertCallBack<S, T> callBack) {
        if (null == sources || null == targetSupplier) {
            return null;
        }
        
        return sources.parallelStream().map(source -> {
            T target = targetSupplier.get();
            copyProperties(source, target);
            if (callBack != null) {
                callBack.callBack(source, target);
            }
            return target;
        }).collect(Collectors.toList());
    }
    
    /**
     * 回调接口
     *
     * @param <S> 源对象类型
     * @param <T> 目标对象类型
     */
    @FunctionalInterface
    public interface ConvertCallBack<S, T> {
        void callBack(S t, T s);
    }
}

