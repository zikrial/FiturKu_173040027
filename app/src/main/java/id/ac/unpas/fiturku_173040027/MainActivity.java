package id.ac.unpas.fiturku_173040027;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String arrMenu[] = {
            "1. Material Barcode",
            "2. Location",
            "3. Google Maps",
            "4. Camera",
            "5. SMS",
            "6. Telepon"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrMenu));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : materialBarcode(); break;
                    case 1 : location(); break;
                    case 2 : googleMaps(); break;
                    case 3 : camera(); break;
                    case 4 : sms(); break;
                    case 5 : telepon(); break;
                }
            }
        });
    }

    public Barcode barcodeResult;
    public TextView result;
    private void materialBarcode() {
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(MainActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withText("Scanning...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
            @Override
            public void onResult(Barcode barcode) {
                barcodeResult = barcode;
                result.setText(barcode.rawValue);
            }
        }).build();
        materialBarcodeScanner.startScan();
    }

    private void location() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Location").setMessage("lat : " + location.getLatitude() + ", long : " + location.getLongitude())
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                Toast.makeText(MainActivity.this, "Unknown Position", Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {
            Log.e("Masuk", "->" + e.getMessage());
        }
    }

    private void googleMaps() {
        startActivity(new Intent(MainActivity.this, MapsActivity.class));
    }

    private void camera() {
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0);
    }

    private void sms () {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SMS");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("input phone number");
        builder.setView(input);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(input.getText().toString(), null, "ini isi pesan dari sms", null, null);
                Toast.makeText(MainActivity.this, "Send SMS Success", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    private void telepon() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Telepon");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setHint("input phone number");
        builder.setView(input);
        builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+input.getText().toString()));
                    MainActivity.this.startActivity(intent);
                } catch (SecurityException e) {
                    Log.e("Masuk", "->" + e.getMessage());
                }
            }
        });
        builder.show();
    }
}
