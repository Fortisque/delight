package com.ieor.delight;

//import eu.livotov.zxscan.ZXScanHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MainActivity extends Activity implements OnClickListener {
	
	Button scanButton;
	public final static int QR_SCAN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button)findViewById(R.id.buttonScan);
        scanButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonScan){
			//ZXScanHelper.scan(this, QR_SCAN_REQUEST_CODE);
			Intent intent = new Intent(this, RestaurantHomeActivity.class);
			startActivity(intent);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == QR_SCAN_REQUEST_CODE) {
			/*String scannedCode = ZXScanHelper.getScannedCode(data);
			System.out.println(scannedCode);*/
			
			Intent intent = new Intent(this, RestaurantHomeActivity.class);
			startActivity(intent);
		}
	}
}
