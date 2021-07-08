package com.bas.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


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
