# 反射的相关应用

* [sdk动态加载](#sdk动态加载)
    * [ClassLoader](#classloader)
* [反射](#反射)
    * [对象的创建](#对象的创建)
    * [Class](#class)
    * [Constructor](#constructor)
    * [Method](#method)
    * [Field](#field)
* [反射获取资源文件](#反射获取资源文件)



## sdk动态加载
普通的jar包是没法动态加载的，因为是class文件，而android支持的是dex格式的。
android的sdk提供转换jar成dex的工具
需要dx命令
```
dx --dex --output=输出文件名.dex 输入文件名.jar
```


### ClassLoader
类加载器，动态的装载Class文件  
Android 中提供了DexClassLoader类  
DexClassLoader构造方法中有四个参数：  
第一个参数：dexPath 需要加载的APK或者Jar文件的路径  
第二个参数：optimizedDirectory 加载后的存放文件路径，不能为null  
第三个参数：libraryPath 本地lib下包含的目录列表，可能是null  
第四个参数：parent 该类装载器的父类装载器，一般用当前执行的装载器（getClassLoader）  


### 对象的创建
首先要先获取类，使用装载器的对象(mDexClassLoader)调用loadClass方法。
参数是类在包中的地址（导包对应的地址）

对象的创建有两种途径，一种是new创建对象，另一种是静态调用（单例）  

通过new调用创建对象：
通过调用类（mClass）的getConstructor方法获得构造函数类的对象（mConstructor）。
参数是创建类对象的参数类型Class...
然后通过构造函数对象（mConstructor）的newInstance方法创建类的对象（Object类型）。
参数是创建类对象的参数对应的值Object...

通过静态方法调用创建对象：
通过调用类（mClass）的getMethod方法获得方法类的对象（mMethod）。
参数有两个：
第一个是静态方法名称（字符串类型），第二个是创建类的对象对应的参数类型Class...
然后通过方法类的对象（mMethod）的invoke方法创建类的对象（Object类型）。
参数有两个：
第一个是调用类（mClass），第二个是创建类对象的参数对应的值Object...
```
private void initModel(){
    Model model = Model.getInstance("dawn", 1, true, 1.0f);
    Model model2 = new Model("dawn", 1, true, 1.0f);
    String jsonModel = new GsonBuilder().create().toJson(model);
    String jsonModel2 = new GsonBuilder().create().toJson(model2);
    Log.i("dawn", "json model = " + jsonModel);
    Log.i("dawn", "json model2 = " + jsonModel2);
}
private void initModelReflex(){
    try {
        Class<?> classModel = Class.forName("com.dawn.reflexdawn.Model");
        Method getInstance = classModel.getMethod("getInstance", new Class[]{
                Class.forName("java.lang.String"),Integer.TYPE, Boolean.TYPE, Float.TYPE});
        Object model = getInstance.invoke(classModel, new Object[]{"dawn", 1, true, 1.0f});
        String jsonModel = new GsonBuilder().create().toJson(model);
        Log.i("dawn", "reflex json model = " + jsonModel);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    }
    try {
        Class<?> classModel2 = Class.forName("com.dawn.reflexdawn.Model");
        Constructor<?> constructor = classModel2.getConstructor(new Class[]{
                Class.forName("java.lang.String"),Integer.TYPE, Boolean.TYPE, Float.TYPE});
        Object model2 = constructor.newInstance(new Object[]{"dawn", 1, true, 1.0f});
        String jsonModel2 = new GsonBuilder().create().toJson(model2);
        Log.i("dawn", "reflex json model2 = " + jsonModel2);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchMethodException e) {
        e.printStackTrace();
    } catch (IllegalAccessException e) {
        e.printStackTrace();
    } catch (InstantiationException e) {
        e.printStackTrace();
    } catch (InvocationTargetException e) {
        e.printStackTrace();
    }
}
```


## 反射

### Class
类，获取Class类型的方法有：
getClass();
Class.forName(className);
基本类型的Class类型是包装类的TYPE属性，例如：
```
Class classInteger = Integer.TYPE;
Class classBoolean = Boolean.TYPE;
Class classFloat = Float.TYPE;
```
字符串的Class类型是：
```
try {
    Class classString = Class.forName("java.lang.String");
} catch (ClassNotFoundException e) {
    e.printStackTrace();
}
```


### Constructor
构造函数  
* getConstructor(Class[] params) 根据构造函数的参数，返回一个具体的具有public属性的构造函数  
* getConstructors() 返回所有具有public属性的构造函数数组  
* getDeclaredConstructor(Class[] params) 根据构造函数的参数，返回一个具体的构造函数（不分public和非public属性）  
* getDeclaredConstructor() 返回该类中所有的构造函数数组（部分public和非public属性）  


### Method
方法  
* getMethod(String name, Class[] params) 根据方法名和参数，返回一个具体的具有public属性的方法
* getMethods() 返回所有具有public属性的方法的数组
* getDeclaredMethod(String name, Class[] params) 根据方法名和参数，放回一个具体的方法（部分public和非public属性）
* getDeclaredMethod() 返回该类中的所有方法数组（部分public和非public属性）

注：
getMethods():用于获取类的所有的public修饰域的成员方法，包括从父类继承的public方法和实现接口的public方法
getDeclaredMethods():用于获取在当前类中定义的所有的成员方法和实现接口方法，不包括从父类继承的方法。


### Field
成员属性
* getField(String name) 根据变量名，返回一个具体的具有public属性的成员变量。
* getFields() 返回具有public属性的成员变量的数组
* getDeclaredField(String name) 根据变量名，返回一个成员变量（部分public和非public属性）。
* getDeclaredField() 返回所有成员变量组成的数组（部分public和非public属性）。


## 反射获取资源文件
```
public static int getResId(String variableName, Class<?> c) {
    try {
        Field idField = c.getDeclaredField(variableName);
        return idField.getInt(idField);
    } catch (Exception e) {
        e.printStackTrace();
        return -1;
    }
}
```
```
int imgId = ReflexUtils.getResId("img.png", R.drawable.class);
```
