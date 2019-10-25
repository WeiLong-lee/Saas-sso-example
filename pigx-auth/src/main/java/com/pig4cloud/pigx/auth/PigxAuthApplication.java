

package com.pig4cloud.pigx.auth;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author lengleng
 * @date 2018年06月21日
 * 认证授权中心
 */
@EnableCaching
@SpringBootApplication
@MapperScan("com.pig4cloud.pigx.auth.mapper")
public class PigxAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxAuthApplication.class, args);
	}
}
