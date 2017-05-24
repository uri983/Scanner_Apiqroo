package com.scann.apiqroo.scannerapiqroo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Usuario on 23/05/2017.
 */

public  class Fragment_scann extends Fragment implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scanner;
    private String toast;

    public Fragment_scann() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayToast();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        final Button scan = (Button) view.findViewById(R.id.scan_from_fragment);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scanFromFragment();


            }
        });
        return view;
    }

    public void scanFromFragment() {




        IntentIntegrator.forSupportFragment(this).setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES).setOrientationLocked(true).setBarcodeImageEnabled(true).setBeepEnabled(true).setPrompt("Enfoca el codigo de barras de la pulsera").initiateScan();
    }

    private void displayToast() {
        if(getActivity() != null && toast != null) {
            Toast.makeText(getActivity(), toast, Toast.LENGTH_LONG).show();
            toast = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                toast = "Cancelled from fragment";
            } else {
                toast = "Scanned from fragment: " + result.getContents();
            }

            // At this point we may or may not have a reference to the activity
            displayToast();
        }
    }






    @Override
    public void handleResult(Result result) {

    }


}
