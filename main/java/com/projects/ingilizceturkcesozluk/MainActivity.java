package com.projects.ingilizceturkcesozluk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.sonuc)
    TextView sonuc;
    @BindView(R.id.text)
    EditText text;

    @BindView(R.id.btnbul)
    Button btnBul;
    @BindView(R.id.btntarget)
    Button btnTarget;
    @BindView(R.id.btnsource)
    Button btnSource;
    @BindView(R.id.btnkonus)
    ImageView btnKonus;
    @BindView(R.id.btnchange)
    ImageView btnDegis;
    @BindView(R.id.btnkopyala)
    ImageView btnKopya;
    @BindView(R.id.btnpaylas)
    ImageView btnPaylas;
    @BindView(R.id.btndinle)
    ImageView btnDinle;
    @BindView(R.id.btndinleust)
    ImageView btnDinleust;

    AdView adView, adView2;
    InterstitialAd mInterstitialAd;
    RewardedVideoAd mRewardedVideoAd;
    String cevrilen = "";
    TextToSpeech textToSpeech;
    Boolean degisim = true;
    String source = "en";
    String target = "tr";
    final int REQ_CODE_SPEECH_INPUT = 100;


    @OnClick(R.id.btnkopyala)
    void runbtnkopyala() {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", text.getText() + " = " + cevrilen);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), getString(R.string.kopyala), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnpaylas)
    void runpaylasbuton() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, "[ÇEVİRİ]:\n" + text.getText() + " = " + cevrilen);
        startActivity(Intent.createChooser(share, getString(R.string.ceviripaylas)));
    }

    @OnClick(R.id.btndinle)
    void rundinlebuton() {
        textToSpeech.speak(cevrilen, TextToSpeech.QUEUE_FLUSH, null);
    }

    @OnClick(R.id.btnbul)
    void runbulbuton() {
        klavyekapat();
        if (text.getText().length() > 0)
            executeGetMethod();
    }

    @OnClick(R.id.btnchange)
    void rundegisbuton() {
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

    @OnClick(R.id.btnkonus)
    void runkonusbuton() {
        SpeechInput();
    }

    @OnClick(R.id.btndinleust)
    void rundinleustbuton() {
        textToSpeech.speak(text.getText().toString().trim(), TextToSpeech.QUEUE_FLUSH, null);
    }

    @OnClick(R.id.sonuc)
    void runsonuctext() {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (Utils.isNetworkAvailable(MainActivity.this)) {
            Snackbar.make(coordinatorLayout, getString(R.string.hosgeldin), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(coordinatorLayout, getString(R.string.internetyok), Snackbar.LENGTH_LONG).show();
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        reklamyuklealt();
        reklamyukleust();
        reklamyuklegecis();
        reklamyuklevideo();
    }

    public void klavyekapat() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void executeGetMethod() {
        showdialog();

        StringRequest jsonForGetRequest = new StringRequest(
                Request.Method.GET, Utils.getServisUrl(source, target, text.getText().toString().trim()),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("hata").equals("true")) {
                                Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_LONG).show();
                            } else {
                                sonuc.setText(jsonObject.getString("cevap"));
                                cevrilen = jsonObject.getString("cevap");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    JSONObject jsonObject = null;
                    String errorMessage = null;

                    switch (response.statusCode) {
                        case 400:
                            errorMessage = new String(response.data);

                            try {
                                jsonObject = new JSONObject(errorMessage);
                                String serverResponseMessage = (String) jsonObject.get("hata");
                                Toast.makeText(getApplicationContext(), getString(R.string.bilinmeyenhata) + serverResponseMessage, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                return param;
            }
        };

        jsonForGetRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonForGetRequest);

    }

    private void SpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.kelimesoyle));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.bilinmeyenhata), Toast.LENGTH_SHORT).show();
        }
    }

    private ProgressDialog progressDialog;

    private void showdialog() {
        progressDialog = new ProgressDialog(MainActivity.this);
        //progressDialog.setTitle(getString(R.string.app_name));
        progressDialog.setMessage(getString(R.string.kelimeloading));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void reklamyuklealt() {
        adView2 = (AdView) this.findViewById(R.id.adViewalt);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);
    }

    public void reklamyukleust() {
        adView = (AdView) this.findViewById(R.id.adViewust);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void reklamyuklegecis() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.reklamkimliğigecis));

        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                //reklamyuklegecis();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mInterstitialAd.show();
            }
        });
    }

    public void reklamyuklevideo() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        //mRewardedVideoAd.setRewardedVideoAdListener(this);
        mRewardedVideoAd.loadAd(getString(R.string.reklamkimliğivideo), new AdRequest.Builder().build());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                mRewardedVideoAd.loadAd(getString(R.string.reklamkimliğivideo), new AdRequest.Builder().build());
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(getString(R.string.app_name));
        dialog.setMessage(getString(R.string.backbutonmesaj));
        dialog.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    finish();
                } else
                    finish();
            }
        });
        dialog.setNegativeButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
        //super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.uygulamapaylaş:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, getString(R.string.urluygulama));
                startActivity(Intent.createChooser(share, getString(R.string.uygpaylas)));
                return true;

            case R.id.storedaaç:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urluygulama))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urluygulama))));
                }
                return true;
            case R.id.ayarlar:
                //startActivity(new Intent(MainActivity.this, WebviewActivity.class));
                Toast.makeText(getApplicationContext(), getString(R.string.yenisürümde), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.hakkinda:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(getString(R.string.hakkindabaslik));
                dialog.setMessage(getString(R.string.hakkindaicerik));
                dialog.setPositiveButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.show();
                return true;
            case R.id.diğerapps:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsparametre))));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.appsurl))));
                }
                return true;
            case R.id.katkıdabulun:
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
                dialog2 = new AlertDialog.Builder(MainActivity.this);
                dialog2.setTitle(getString(R.string.app_name));
                dialog2.setMessage(getString(R.string.backbutonmesaj));
                dialog2.setPositiveButton(getString(R.string.btnevet), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        }
                    }
                });
                dialog2.setNegativeButton(getString(R.string.btnkapat), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog2.cancel();
                    }
                });
                dialog2.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
