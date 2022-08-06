package xyz.idaoteng.myblog.common.dto;

import java.io.Serializable;

/*
  DTO：数据传输（时的）对象。不同的Web应用交互时发送的数据对象<VO是DTO的细化>，因此它应该只由数据属性构成。
  DTO是数据对象的一种，另有：
  PO：持久化（时的）对象；
  VO：视图（渲染时的）对象<渲染HTML页面需时要使用到的数据对象。在RESTful Web应用中可能完全不需要，因为RESTful Web应用并不关心消费者是前端应用还是非前端应用>
  数据对象是POJO的一种，但它比POJO还要更加纯粹，因为它只有访问器，没有处理业务的方法
  POJO衍生的概念还有Bean、EJB(Enterprise Java Bean)、Spring Bean
  上述所有名词的O都是Object的缩写，它有时是指对象的类型，有时是指对象本身（当我给它带了括号时）
  java中数据对象和属性的概念：
    属性：
        字段（或称：属性名、域、成员变量。下同） + getter/setter方法
    属性的命名：
        字段名 = getter/setter方法名 - “()” - “get”/“set” 在将首字母改成小写
        get/set方法名必须符合小驼峰命名法
    数据属性：
        基本数据类型字段 + getter/setter方法（使用@Data注解时：boolean 为is方法， Boolean仍然是get方法 ）
        string类型字段 + getter/setter方法
        基本数据类型数组/集合字段 + getter/setter方法
        string类型数组/集合字段 + getter/setter方法
        只由数据属性构成的类型字段 + getter/setter方法
    数据对象：
        只含有数据属性的类型所new出来的对象
*/
public interface DataTransferObject extends Serializable {
}
