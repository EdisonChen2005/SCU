package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // ========== 1. 新增学生（POST：RESTful规范） ==========
    @PostMapping("/add")
    public Result<String> add(@Validated(Student.Add.class) @RequestBody Student student) {
        // @Validated(Add.class)：只校验新增相关字段（name/age）
        // @RequestBody：接收JSON参数（前端传{"name":"张三","age":20}）
        boolean success = studentService.addStudent(student);
        return success ? Result.success("新增学生成功") : Result.fail("新增学生失败");
    }

    // ========== 2. 删除学生（DELETE：RESTful规范） ==========
    @DeleteMapping("/delete/{id}")
    public Result<Void> delete(@NotNull(message = "学生ID不能为空") @PathVariable Integer id) {
        // 先校验学生是否存在
        Student existStudent = studentService.getStudentById(id);
        if (existStudent == null) {
            return Result.fail("删除失败：学生不存在");
        }
        boolean success = studentService.deleteStudentById(id);
        return success ? Result.success() : Result.fail("删除学生失败");
    }

    // ========== 3. 修改学生（PUT：RESTful规范） ==========
    @PutMapping("/update")
    public Result<String> update(@Validated(Student.Update.class) @RequestBody Student student) {
        // @Validated(Update.class)：校验修改相关字段（id/name/age）
        // 先校验学生是否存在
        Student existStudent = studentService.getStudentById(student.getId());
        if (existStudent == null) {
            return Result.fail("修改失败：学生不存在");
        }
        boolean success = studentService.updateStudent(student);
        return success ? Result.success("修改学生成功") : Result.fail("修改学生失败");
    }

    // ========== 4. 查单个学生（GET：RESTful规范） ==========
    @GetMapping("/get/{id}")
    public Result<Student> getById(@NotNull(message = "学生ID不能为空") @PathVariable Integer id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return Result.fail("查询失败：学生不存在");
        }
        return Result.success(student); // 返回学生数据
    }

    // ========== 5. 分页+条件查询（GET：RESTful规范） ==========
    @GetMapping("/page")
    public Result<IPage<Student>> pageQuery(
            @RequestParam(defaultValue = "1") Integer current, // 默认第1页
            @RequestParam(defaultValue = "10") Integer size,   // 默认每页10条
            @RequestParam(required = false) String name,      // 可选：姓名模糊查询
            @RequestParam(required = false) Integer minAge,   // 可选：最小年龄
            @RequestParam(required = false) Integer maxAge    // 可选：最大年龄
    ) {
        // 1. 构建分页对象
        Page<Student> page = new Page<>(current, size);

        // 2. 构建条件查询器
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        // 姓名模糊查询（非空才拼接条件）
        if (StringUtils.hasText(name)) {
            wrapper.like(Student::getName, name);
        }
        // 年龄≥minAge（非空才拼接）
        if (minAge != null) {
            wrapper.ge(Student::getAge, minAge);
        }
        // 年龄≤maxAge（非空才拼接）
        if (maxAge != null) {
            wrapper.le(Student::getAge, maxAge);
        }
        // 按ID降序排序
        wrapper.orderByDesc(Student::getId);

        // 3. 调用Service分页查询
        IPage<Student> pageData = studentService.pageQueryStudent(page, wrapper);

        // 4. 返回分页数据
        return Result.success(pageData);
    }

    // ========== 兼容GET请求的新增接口（仅测试用，正式环境用POST） ==========
    @GetMapping("/add")
    public Result<String> addByGet(@Validated(Student.Add.class) Student student) {
        boolean success = studentService.addStudent(student);
        return success ? Result.success("新增学生成功") : Result.fail("新增学生失败");
    }
}