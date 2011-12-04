package umich.pitchcoach.demo;

/*
 * http://en.wikipedia.org/wiki/Scientific_pitch_notation for pitch values
 * 
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import umich.pitchcoach.R;
import umich.pitchcoach.RangePitchDetect;
import umich.pitchcoach.NotePlayer.NotePlayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RangeSelect extends Activity {
    /** Called when the activity is first created. */
  public static FileOutputStream fos;
  public static String FILENAME="singing_range.txt";
  private int minFreq, maxFreq;
  private NotePlayer noteplayer;
  
  TextView thisTxt;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    noteplayer = new NotePlayer();
    
    setContentView(R.layout.rangeselect);
    
    thisTxt = (TextView) findViewById(R.id.changeTxt);
    
    Button bassSelect = (Button) findViewById(R.id.bassBtn);    
    bassSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        minFreq = 82; // E2
        maxFreq = 330; // E4
        noteplayer.playNote(minFreq, 1).then(noteplayer.playNote(maxFreq, 1)).go();
        writeContent(minFreq, maxFreq);
      }
    });
    
    Button tenorSelect = (Button) findViewById(R.id.tenorBtn);    
    tenorSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        minFreq = 130; // C3
        maxFreq = 523; // C5
        noteplayer.playNote(minFreq, 1).then(noteplayer.playNote(maxFreq, 1)).go();
        writeContent(minFreq, maxFreq);
      }
    });
    
    Button altoSelect = (Button) findViewById(R.id.altoBtn);    
    altoSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        minFreq = 196; // G3
        maxFreq = 660; // E5
        noteplayer.playNote(minFreq, 1).then(noteplayer.playNote(maxFreq, 1)).go();
        writeContent(minFreq, maxFreq);
      }
    });
    
    Button autoDetect = (Button) findViewById(R.id.autoRange);
    autoDetect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent myIntent = new Intent(view.getContext(), RangePitchDetect.class);
        startActivityForResult(myIntent, 0);
        finish();
      }
    });
    
    Button sopranoSelect = (Button) findViewById(R.id.sopranoBtn);    
    sopranoSelect.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        minFreq = 262; // C4
        maxFreq = 1047; // C6
        noteplayer.playNote(minFreq, 1).then(noteplayer.playNote(maxFreq, 1)).go();
        writeContent(minFreq, maxFreq);
      }
    });
    
    Button done = (Button) findViewById(R.id.doneBtn);    
    done.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
    	  finish();
      }
    });
    
    /*
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
          thisTxt.setText("'"+inb+"'"+" was read from file");
          fis.close();
        } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    });*/
  }
        
  private void writeContent(int minFreq, int maxFreq) {
    try {
      String toWrite = Integer.toString(minFreq)+" "+Integer.toString(maxFreq);
      //thisTxt.setText("'"+toWrite+"' written to file");
      fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
      fos.write(toWrite.getBytes());
      fos.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}