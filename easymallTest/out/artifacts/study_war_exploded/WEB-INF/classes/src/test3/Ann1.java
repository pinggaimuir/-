package test3;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

/**
 * Created by tarena on 2016/9/10.
 */
/*@Retention(RetentionPolicy.SOURCE)//给编译器看的 ，编译器看玩直接丢弃*/
/*@Retention(RetentionPolicy.CLASS)//字节码级别，编译器编译完后会将该类的注解保留在class文件中，当运行该程序时，JVM将不会保留*/
   /* @Retention(RetentionPolicy.RUNTIME)//编译器编译后将会保留在class文件中 jvm保留吗程序训醒时也会一起运行，可以利用反射来获取注解*/
    @Target(value={ElementType.TYPE,ElementType.METHOD})
    @Documented
    @Inherited
public @interface Ann1 {
  String name() default "";
   int  value()default 1;
    String[] like() default {};
    AnnotatedElement ae=null;
}
