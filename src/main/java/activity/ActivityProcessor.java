package activity;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskRequest;
import com.amazonaws.services.stepfunctions.model.GetActivityTaskResult;
import com.amazonaws.services.stepfunctions.model.SendTaskFailureRequest;
import com.amazonaws.services.stepfunctions.model.SendTaskSuccessRequest;
import com.amazonaws.util.json.Jackson;
import com.fasterxml.jackson.databind.JsonNode;
import editoxml.EDItoXMLprocessor;
import s3.S3processor;

import java.util.concurrent.TimeUnit;

public class ActivityProcessor {


    private static String ARN = "arn:aws:states:eu-central-1:630997192154:activity:EDItoXML";
    private static String ACCESS_KEY = "AKIAZF2S5BXNKKVWODW4";
    private static String SECRET_KEY = "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";
    BasicAWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);


    public void listen() {
        final ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSocketTimeout((int) TimeUnit.SECONDS.toMillis(10000));

        final AWSStepFunctions client = AWSStepFunctionsClientBuilder
                .standard()
                .withRegion("eu-central-1")
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        String edi;
        String result;
        String fileName;

        System.out.println("Listening...");

        while (true) {
            GetActivityTaskResult getActivityTaskResult = client.getActivityTask(
                    new GetActivityTaskRequest().withActivityArn(ARN));

            if (getActivityTaskResult != null) {


                try {
                    final JsonNode json = Jackson.jsonNodeOf(getActivityTaskResult.getInput());
                    if (json != null) {
                        if (json.get("key") != null) {
                            System.out.println("Processing event...");
                            fileName = json.get("key").textValue();
                            S3processor s3 = new S3processor();
                            edi = s3.get(fileName);

                            s3.upload(new EDItoXMLprocessor().transform(edi), fileName);
                            s3.delete(fileName);

                            result = process(fileName);
                            client.sendTaskSuccess(new SendTaskSuccessRequest()
                                    .withOutput(result)
                                    .withTaskToken(getActivityTaskResult.getTaskToken()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    client.sendTaskFailure(new SendTaskFailureRequest()
                            .withTaskToken(getActivityTaskResult.getTaskToken()));
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String process(final String who) {
        System.out.println("Successful");
        return "{\"key\": \"" + who + "\"}";

    }
}


