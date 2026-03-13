package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Student;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 告诉 Spring 这是一个 Mapper 接口
public interface StudentMapper extends BaseMapper<Student> {
    // 继承 BaseMapper 后，insert/update/select 自动就有了
}