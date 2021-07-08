package com.bas.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @SpringBootApplication 做了如下事情：
 * 
 * {
 * 1.@Configuration：将类标记为应用程序上下文的Bean定义的源。
 * 
 * 2.@EnableAutoConfiguration：告诉Spring Boot根据类路径设置，其他bean和各种属性设置开始添加bean。
 * 例如，如果spring-webmvc在类路径上，则此注释将应用程序标记为Web应用程序并激活关键行为，例如设置DispatcherServlet。
 * 
 * 3.@ComponentScan：告诉Spring在包中寻找其他组件，配置和服务com/lb/processing，让它找到控制器。
 * }
 * @author bas
 *
 */
@SpringBootApplication
public class SpringBatchProcessingApplication {

//	@Autowired
//	private JobLauncher jobLauncher;
//
//	@Autowired
//	private Job importUserJob;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBatchProcessingApplication.class, args);
		
		// 确保JVM在作业完成时退出
//		int state = SpringApplication.exit(context);
//		System.exit(state);

//		if(args[0].contains("true")){
//			JobParameters parameters = new JobParametersBuilder()
//					.addString("msg","asd")
//					.toJobParameters();
//
//
//		}
	}

}
