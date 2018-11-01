package com.projects.ingilizceturkcesozluk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static final String requestid = "Sozluk7812278*";

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*String servisURL = "http://opencartexcel.hol.es/api/ingilizceturkcesozluk/index.php?requestid=" +
            requestid + "&text=" + text + "&source=" + source + "&target=" + target;*/

    public static String URL = "http://opencartexcel.hol.es/api/ingilizceturkcesozluk/index.php";

    public static String getServisUrl(String source, String target, String text) {
        return new String(URL + "?requestid=" + requestid + "&text=" + text + "&source=" + source + "&target=" + target);
    }
}
/*
EditText text;
ImageView btnKonus, btnDegis, btnKopya, btnPaylas, btnDinle, btnDinleust;
Button btnBul, btnTarget, btnSource;
TextView sonuc;
CoordinatorLayout coordinatorLayout;

 */

/*
        btnKopya = (ImageView) findViewById(R.id.btnkopyala);
        btnPaylas = (ImageView) findViewById(R.id.btnpaylas);
        btnDinle = (ImageView) findViewById(R.id.btndinle);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        btnDegis = (ImageView) findViewById(R.id.btnchange);
        text = (EditText) findViewById(R.id.text);
        sonuc = (TextView) findViewById(R.id.sonuc);
        btnBul = (Button) findViewById(R.id.btnbul);
        btnTarget = (Button) findViewById(R.id.btntarget);
        btnSource = (Button) findViewById(R.id.btnsource);
        btnKonus = (ImageView) findViewById(R.id.btnkonus);
        btnDinleust = (ImageView) findViewById(R.id.btndinleust);

        btnKopya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text", text.getText() + " = " + cevrilen);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), getString(R.string.kopyala), Toast.LENGTH_SHORT).show();
            }
        });
        btnPaylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "[ÇEVİRİ]:\n" + text.getText() + " = " + cevrilen);
                startActivity(Intent.createChooser(share, getString(R.string.ceviripaylas)));
            }
        });
        btnDinle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(cevrilen, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        btnBul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                klavyekapat();
                if (text.getText().length() > 0)
                    executeGetMethod();
            }
        });
        btnDegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (degisim) {
                    degisim = false;
                    btnSource.setText(getString(R.string.turkce));
                    btnTarget.setText(getString(R.string.ingilizce));
                    source = "tr";
                    target = "en";
                } else {
                    degisim = true;
                    btnSource.setText(getString(R.string.ingilizce));
                    btnTarget.setText(getString(R.string.turkce));
                    source = "en";
                    target = "tr";
                }

            }
        });
        btnKonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechInput();
            }
        });
        btnDinleust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(text.getText().toString().trim(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        sonuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(getString(R.string.aramasonuc));
                dialog.setMessage(cevrilen);
                dialog.setPositiveButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });*/
