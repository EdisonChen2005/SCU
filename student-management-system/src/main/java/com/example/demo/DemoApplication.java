package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 仅扫描Mapper包，不包含Service包（核心修正）
@MapperScan("com.example.demo.mapper")
@SpringBootApplication
public class DemoApplication {

	// 删掉这里的分页插件Bean定义（因为Config类里已有）
	// 👇 注释/删除这部分代码
    /*
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
    */

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}