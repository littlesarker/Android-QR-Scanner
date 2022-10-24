package my.app.qrscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class viewActivity extends AppCompatActivity {
    TextView receive_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        receive_txt=findViewById(R.id.receive_txt);

        Intent intent=getIntent();
        String data = intent.getStringExtra("message_key");
        receive_txt.setText(data);


        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";


        if(data.matches(regex)){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
            startActivity(browserIntent);
        }else
        {
            receive_txt.setText(data);
        }

    }
}