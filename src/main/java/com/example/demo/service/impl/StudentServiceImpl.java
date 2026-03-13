package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Student;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    // 手动校验器（兜底）
    private final Validator validator;
    public StudentServiceImpl() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public boolean addStudent(Student student) {
        // 手动校验（兜底）
        Set<javax.validation.ConstraintViolation<Student>> violations = validator.validate(student, Student.Add.class);
        if (!violations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            violations.forEach(v -> errorMsg.append(v.getMessage()).append(";"));
            throw new RuntimeException(errorMsg.toString());
        }
        return studentMapper.insert(student) > 0;
    }

    @Override
    public boolean deleteStudentById(Integer id) {
        return studentMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateStudent(Student student) {
        // 手动校验（兜底）
        Set<javax.validation.ConstraintViolation<Student>> violations = validator.validate(student, Student.Update.class);
        if (!violations.isEmpty()) {
            StringBuilder errorMsg = new StringBuilder();
            violations.forEach(v -> errorMsg.append(v.getMessage()).append(";"));
            throw new RuntimeException(errorMsg.toString());
        }
        return studentMapper.updateById(student) > 0;
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentMapper.selectById(id);
    }

    // 实现分页+条件查询
    @Override
    public IPage<Student> pageQueryStudent(Page<Student> page, LambdaQueryWrapper<Student> wrapper) {
        return studentMapper.selectPage(page, wrapper);
    }
}