package com.jinyeob.myphoto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceContour;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RelativeLayout relativeLayout = findViewById(R.id.relativeLayout);

        FirebaseVisionFaceDetectorOptions options =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                        .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                        .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                        .build();

        final Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.photo);

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                .getVisionFaceDetector(options);

        Task<List<FirebaseVisionFace>> result =
                detector.detectInImage(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<FirebaseVisionFace>>() {
                                    @Override
                                    public void onSuccess(List<FirebaseVisionFace> faces) {
                                        // Task completed successfully
                                        // ...
                                        Point p = new Point();
                                        Display display = getWindowManager().getDefaultDisplay();
                                        display.getSize(p);


                                        for (FirebaseVisionFace face : faces) {

                                            // If landmark detection was enabled (mouth, ears, eyes, cheeks, and
                                            // nose available):
                                            FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                            float lefteyeX = leftEye.getPosition().getX();
                                            float lefteyeY = leftEye.getPosition().getY();

                                            FirebaseVisionFaceLandmark leftCheek = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_CHEEK);
                                            float leftcheekX = leftCheek.getPosition().getX();
                                            float leftcheekY = leftCheek.getPosition().getY();

                                            FirebaseVisionFaceLandmark rightCheek = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_CHEEK);
                                            float rightcheekX = rightCheek.getPosition().getX();
                                            float rightcheekY = rightCheek.getPosition().getY();

                                            ImageView imageMung = new ImageView(MainActivity.this);
                                            imageMung.setImageResource(R.drawable.mung);
                                            imageMung.setX(p.x * lefteyeX / bitmap.getWidth() - 100);
                                            imageMung.setY(p.y * lefteyeY / bitmap.getHeight() - 100);
                                            relativeLayout.addView(imageMung);
                                            //크기조정
                                            imageMung.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));

                                            ImageView imageLeft = new ImageView(MainActivity.this);
                                            imageLeft.setImageResource(R.drawable.left);
                                            imageLeft.setX(p.x * leftcheekX / bitmap.getWidth() - 100);
                                            imageLeft.setY(p.y * leftcheekY / bitmap.getHeight() - 100);
                                            relativeLayout.addView(imageLeft);
                                            //크기조정
                                            imageLeft.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));

                                            ImageView imageRight = new ImageView(MainActivity.this);
                                            imageRight.setImageResource(R.drawable.right);
                                            imageRight.setX(p.x * rightcheekX / bitmap.getWidth() - 100);
                                            imageRight.setY(p.y * rightcheekY / bitmap.getHeight() - 100);
                                            relativeLayout.addView(imageRight);
                                            //크기조정
                                            imageRight.setLayoutParams(new RelativeLayout.LayoutParams(200, 200));

                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });

    }
}
