package io.github.gafarrell.instanceA;

import io.github.gafarrell.shared.RekognitionDriver;
import io.github.gafarrell.shared.S3Driver;
import io.github.gafarrell.shared.SqsDriver;
import software.amazon.awssdk.services.rekognition.model.Label;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.util.List;

/**
 * Lambda function entry point. You can change to use other pojo type or implement
 * a different RequestHandler.
 *
 * @see <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda Java Handler</a> for more information
 */
public class App{
    public static void main(String[] args) {
        new App().RunApp();
    }

    private final SqsDriver sqsDriver = new SqsDriver();
    private final S3Driver s3Driver = new S3Driver();
    private final RekognitionDriver rekognitionDriver = new RekognitionDriver();

    public void RunApp() {
        System.out.println("Grabbing image file names from bucker cs442-unr");
        List<S3Object> objects = s3Driver.listObjects("cs442-unr");
        for (S3Object object : objects){
            System.out.println("Processing: " + object.key());
            List<Label> labels = rekognitionDriver.detectLabelsWithinImage("cs442-unr", object.key());
            for (Label l : labels){
                if (l.name().equalsIgnoreCase("car") && l.confidence() > 90F) {
                    sqsDriver.sendBasicMessage(object.key());
                    System.out.println("Rekognition detected car with confidence > 90% in image: " + object.key());
                }
            }
        }
        System.out.println("Done processing, sending -1 index.");
        sqsDriver.sendBasicMessage("-1");
    }
}
