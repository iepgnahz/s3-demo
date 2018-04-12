package com.s3.controller;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/s3")
public class S3Controller {
    private AWSCredentials credentials = new BasicAWSCredentials(
            "*********",
            "**********"
    );

    private AmazonS3 s3client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.CN_NORTH_1)
            .build();
    //请注意此处必须设置Region否则会出现报错找不到你的accessKey

    @PostMapping(value = "")
    public void createBucket(){
        String bucketName = "some-tests-for-bucket";
        if(s3client.doesBucketExistV2(bucketName)){
            System.out.println("Bucket name is not available."
                    + " Try again with a different Bucket name.");
            return;
        }
        s3client.createBucket(bucketName);
        return;
    }

    @PutMapping(value = "/object")
    public void uploadObject(){
        String bucketName = "some-tests-for-bucket";
        s3client.putObject(
                bucketName,
                "test/test.png",   //代表即将要上传的文件在buket的什么位置
                new File("/Users/pezhang/Demo/aws/s3-demo/src/main/java/com/s3/test.png")
                //文件在本地的那个位置 绝对路径！
        );
        return;
    }

    @GetMapping(value = "/object")
    public void downloadObject() throws IOException {
        String bucketName = "some-tests-for-bucket";
        S3Object s3object = s3client.getObject(bucketName, "test/test.png");
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        FileUtils.copyInputStreamToFile(inputStream, new File("/Users/pezhang/Demo/aws/s3-demo/src/main/resources/test.png"));

        return;
    }

    @DeleteMapping(value = "/object")
    public void deleteObject() throws IOException {
        String bucketName = "some-tests-for-bucket";
        s3client.deleteObject(bucketName,"test/test.png");
        return;
    }

    @DeleteMapping(value = "")
    public void deleteBucket() throws IOException {
        String bucketName = "some-tests-for-bucket";
        s3client.deleteBucket(bucketName);
        return;
    }




}
