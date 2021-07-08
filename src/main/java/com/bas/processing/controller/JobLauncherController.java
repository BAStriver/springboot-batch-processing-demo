package com.bas.processing.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class JobLauncherController{

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jobLauncherDemoJob;

    @GetMapping(value = "/job/{msg}")
    public String jobRun1(@PathVariable String msg){
        //将接受的参数传递给任务
        JobParameters parameters = new JobParametersBuilder()
                .addString("msg",msg)
                .toJobParameters();
        //启动任务 并把参数传给任务
        try {
            jobLauncher.run(jobLauncherDemoJob,parameters);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }

        return "job success";
    }
}
