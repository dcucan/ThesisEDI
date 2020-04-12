package s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.Scanner;

public class S3processor {

    private AmazonS3 s3Client;
    private static String SOURCEBUCKETFIRST;
    private static String SOURCEBUCKET;
    private static String DESTIONATIONBUCKET;
    private String file;

    public S3processor() {
        s3Client = AmazonS3ClientBuilder
                .standard()
                .withRegion("eu-central-1")
                .build();
        SOURCEBUCKETFIRST = "jsonfilesformybt";
        SOURCEBUCKET = "edimessage";
        DESTIONATIONBUCKET = "xmlfilesformybt";
        file = "";

    }


    public boolean upload(String content, String fileName) {
        s3Client.putObject(DESTIONATIONBUCKET, fileName, content);
        return false;
    }

    public String get(String fileName) {

        S3Object s3Object = s3Client.getObject(new GetObjectRequest(SOURCEBUCKET, fileName));
        InputStream objectData = s3Object.getObjectContent();
        Scanner scanner = new Scanner(objectData);
        while (scanner.hasNext()) {
            file += scanner.nextLine();
        }
        scanner.close();

        if (file != null) {
            return file;
        } else {
            return null;
        }
    }

    public boolean delete(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(SOURCEBUCKET, fileName));
        s3Client.deleteObject(new DeleteObjectRequest(SOURCEBUCKETFIRST, fileName));
        return true;
    }
}