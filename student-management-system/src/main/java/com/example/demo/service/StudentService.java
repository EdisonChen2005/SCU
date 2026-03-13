package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Student;

// 完全手写Service接口，不依赖MyBatis-Plus的IService
public interface StudentService {
    // 新增学生
    boolean addStudent(Student student);

    // 删除学生
    boolean deleteStudentById(Integer id);

    // 修改学生
    boolean updateStudent(Student student);

    // 根据ID查学生
    Student getStudentById(Integer id);

    // 分页查询学生
    IPage<Student> pageQueryStudent(Page<Student> page, LambdaQueryWrapper<Student> wrapper);
}