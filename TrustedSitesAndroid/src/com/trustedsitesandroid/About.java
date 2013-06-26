package com.trustedsitesandroid;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity {

	private TextView developers;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        developers = (TextView)findViewById(R.id.developers);

        developers.setText(getResources().getString(R.string.developers));
        developers.append("\n\nCarlos Pardo Duran (@CPardoDuran)\n");        
        developers.append("\n@TrustedSites");
  
        Button returnMap = (Button) findViewById(R.id.buttonReturn);

		returnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
    }
}