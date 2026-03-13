package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@TableName("student")
public class Student {
    @TableId(type = IdType.ASSIGN_ID) // 声明自增主键
    private Integer id;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 20, message = "姓名长度必须在1-20之间")
    private String name;

    // 年龄：必须≥0，≤150
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;

    // 分组校验：不同接口校验不同字段（新增只校验name/age，修改校验id/name/age）
    public interface Add {}
    public interface Update {}
    public interface Delete {}
    public interface Get {}

    // 空构造函数
    public Student() {
    }

    // 手动生成的 Getter 和 Setter，确保编译器绝对能识别
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
}