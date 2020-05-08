package com.easypay.sdk.util;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class BeanUtils {

	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
		if ((map == null) || (map.isEmpty())) {
			return null;
		}
		Map<String, Object> sortMap = new TreeMap<String, Object>(new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
		});
		sortMap.putAll(map);
		return sortMap;
	}

	public static String getMapToString(Map<String, Object> map) {
		if ((map == null) || (map.isEmpty())) {
			return "";
		}
		Map<String, Object> sortMap = sortMapByKey(map);
		StringBuffer sb = new StringBuffer();
		if (sortMap == null) {
			return "";
		}
		for (String key : sortMap.keySet()) {
			if (key != null) {
				sb.append(key + "=" + sortMap.get(key) + "&");
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	/**
	 * 
	 * @Description: 对象转map
	 * @param obj
	 * @return
	 * @author XY  
	 * @date 2019年11月15日
	 */
    public static Map<String, Object> obj2Map(Object obj) {
        Map<String, Object> reqMap = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            String fName = f.getName();
            try {
            	// 去掉序列化时的serialVersionUID
            	if (fName.equalsIgnoreCase("serialVersionUID")) {
					continue;
				}
            	
                Object fValue = f.get(obj);
                if (fValue == null || "".equals(fValue)) {
                    reqMap.put(fName, "");
                } else {
                    reqMap.put(fName, fValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return reqMap;
    }
    
    /**
     * map转对象
     */
    public static <T> T map2Obj(Map<String, Object> map, Class<?> clazz) throws Exception {
		Object obj = clazz.newInstance();
		if (map != null && !map.isEmpty() && map.size() > 0) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String propertyName = entry.getKey(); 	// 属性名
				Object value = entry.getValue();		// 属性值
				String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
				Field field = getClassField(clazz, propertyName);	// 获取和map的key匹配的属性名称
				if (field == null){
					continue;
				}
				
				Class<?> fieldTypeClass = field.getType();
				value = convertValType(value, fieldTypeClass);  // 匹配数据类型
				try {
					clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		return (T) obj;
	}

	/**
	 * 根据给定对象类匹配对象中的特定字段
	 */
	private static Field getClassField(Class<?> clazz, String fieldName) {
		if (Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}
		
		Class<?> superClass = clazz.getSuperclass();	//如果该类还有父类，将父类对象中的字段也取出
		if (superClass != null) {						//递归获取
			return getClassField(superClass, fieldName);
		}
		return null;
	}

	/**
	 * 将map的value值转为实体类中字段类型匹配的方法
	 */
	private static Object convertValType(Object value, Class<?> fieldTypeClass) {
		Object retVal = null;
		
		if (Long.class.getName().equals(fieldTypeClass.getName())
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if (Integer.class.getName().equals(fieldTypeClass.getName())
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if (Float.class.getName().equals(fieldTypeClass.getName())
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if (Double.class.getName().equals(fieldTypeClass.getName())
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else {
			retVal = value;
		}
		return retVal;
	}
}
