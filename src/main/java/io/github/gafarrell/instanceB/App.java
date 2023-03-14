package io.github.gafarrell.instanceB;

import io.github.gafarrell.shared.RekognitionDriver;
import io.github.gafarrell.shared.S3Driver;
import io.github.gafarrell.shared.SqsDriver;
import software.amazon.awssdk.services.rekognition.model.DetectTextResponse;
import software.amazon.awssdk.services.rekognition.model.TextDetection;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args){
        new App().StartApp();
    }

    private SqsDriver sqsDriver = new SqsDriver();
    private RekognitionDriver rekognitionDriver = new RekognitionDriver();
    private S3Driver s3Driver = new S3Driver();
    private List<String> analyzedImages = new ArrayList<String>();

    public void StartApp(){

        String message = "";
        int pollCount = 0;
        while (!message.equals("-1")) {
            pollCount++;
            ReceiveMessageResponse pollResponse = sqsDriver.pollMessages("ImageQueue");
            if (pollResponse.hasMessages()){
                for (Message msg : pollResponse.messages()){
                    message = msg.body();
                    if (message.equals("-1")) break;
                    analyzeAndSaveImageText(message);
                }
                pollCount = 0;
            }
        }
        saveResults();
    }

    private void analyzeAndSaveImageText(String imageKey){
        DetectTextResponse detectTextResponse = rekognitionDriver.detectTextWithinImage("cs442-unr", imageKey);

        for (TextDetection detection : detectTextResponse.textDetections()){
            analyzedImages.add("Key: " + imageKey + "\n- Confidence: " + detection.confidence() + "\n- Detected Text: " + detection.detectedText() + "\n\n");
        }
    }

    private void saveResults(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("analyzed-text.txt"));

            for (String s : analyzedImages){
                writer.write(s);
            }

            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
