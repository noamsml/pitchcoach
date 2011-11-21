package umich.pitchcoach.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import umich.pitchcoach.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RangeSelect extends Activity {
    /** Called when the activity is first created. */
  FileOutputStream fos;
  String FILENAME="singing_range.txt";
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.rangeselect);
    
    final TextView thisTxt = (TextView) findViewById(R.id.changeTxt);
    
    Button tenorSelect = (Button) findViewById(R.id.tenorBtn);    
    tenorSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        writeContent("tenor");
        thisTxt.setText("'tenor' written to file");
      }
    });
    
    Button altoSelect = (Button) findViewById(R.id.altoBtn);    
    altoSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        writeContent("alto");
        thisTxt.setText("'alto' written to file");
      }
    });
    
    Button sopranoSelect = (Button) findViewById(R.id.sopranoBtn);    
    sopranoSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        writeContent("soprano");
        thisTxt.setText("'soprano' written to file");
      }
    });
    
    Button loadMe = (Button) findViewById(R.id.loadMe);    
    loadMe.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        StringBuilder inb = new StringBuilder();
        try {
          FileInputStream fis = openFileInput(FILENAME);
          int ch;
          while((ch = fis.read()) != -1)
            inb.append((char)ch);
          thisTxt.setText(inb);
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });
  }
  private void writeContent(String toWrite) {
    try {
      fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
      fos.write(toWrite.getBytes());
      fos.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}