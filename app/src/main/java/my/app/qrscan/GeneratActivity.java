package my.app.qrscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;

public class GeneratActivity extends AppCompatActivity {


    Context context;
    String TAG = "GenerateQRCode";
    EditText edtValue;
    ImageView qrImage;
    Button start, save;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/Image";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generat);


        getSupportActionBar().hide();

        qrImage = (ImageView) findViewById(R.id.imageView1);
        edtValue = (EditText) findViewById(R.id.qrInput);
        start = (Button) findViewById(R.id.button1);
        save = (Button) findViewById(R.id.saveID);

        save.setVisibility(View.GONE);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setVisibility(View.VISIBLE);
                inputValue = edtValue.getText().toString();
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    qrgEncoder = new QRGEncoder(inputValue, null,QRGContents.Type.TEXT, smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v(TAG, e.toString());
                    }
                } else {
                    edtValue.setError("Required");
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable bitmapDrawable = (BitmapDrawable) qrImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageandText(bitmap);

            }

        });


    }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // setting type to image
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share Via"));
    }
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this,"my.app.qrscan",file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }






}