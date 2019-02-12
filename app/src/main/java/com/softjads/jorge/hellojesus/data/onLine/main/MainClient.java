package com.softjads.jorge.hellojesus.data.onLine.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softjads.jorge.hellojesus.R;
import com.softjads.jorge.hellojesus.main.MainActivity;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jorge on 22/02/2018.
 */

public class MainClient {

    static String  base_url = "";

    public static final String BASE_URL_MATHEUS_1 = "https://private-8ea63-topicscourseenglih.apiary-mock.com/";
    public static final String BASE_URL_MARK_2 = "https://private-15b8c-topicscourseenglihmark.apiary-mock.com/";
    public static final String BASE_URL_LUKE_3 = "https://private-8d0bb-topicscourseenglihluke.apiary-mock.com/";
    public static final String BASE_URL_JOHN_4 = "https://private-e42e4-topicscourseenglihjohn.apiary-mock.com/";
    public static final String BASE_URL_ACTS_5 = "https://private-3be797-topicscourseenglihacts.apiary-mock.com/";
    public static final String BASE_URL_ROMANS_6 = "https://private-147d35-topicscourseenglihromans.apiary-mock.com/";
    public static final String BASE_URL_1CORINTHIANS_7 = "https://private-43e60a-topicscourseenglih1corinthians.apiary-mock.com/";
    public static final String BASE_URL_2CORINTHIANS_8 = "https://private-39267-topicscourseenglih2corinthians.apiary-mock.com/";
    public static final String BASE_URL_GALATIANS_9 = "https://private-067e82-topicscourseenglihgalatians.apiary-mock.com/";
    public static final String BASE_URL_EPHESIANS_10 = "https://private-8fc18-topicscourseenglihephesians.apiary-mock.com/";
    public static final String BASE_URL_PHILIPPIANS_11 = "https://private-a474da-topicscourseenglihphilippians.apiary-mock.com/";
    public static final String BASE_URL_COLOSSIANS_12 = "https://private-eaad4-topicscourseenglihcolossians.apiary-mock.com";
    public static final String BASE_URL_1THESSALONIANS_13 = "https://private-574b23-topicscourseenglih1thessalonians.apiary-mock.com/";
    public static final String BASE_URL_2THESSALONIANS_14 = "https://private-5c4532-topicscourseenglih2thessalonians.apiary-mock.com/";
    public static final String BASE_URL_1TIMOTHY_15 = "https://private-9db8d-topicscourseenglih1timothy.apiary-mock.com/";
    public static final String BASE_URL_2TIMOTHY_16 = "https://private-47302-topicscourseenglih2timothy.apiary-mock.com/";
    public static final String BASE_URL_TITUS_17 = "https://private-d043b9-topicscourseenglihtitus.apiary-mock.com/";
    public static final String BASE_URL_PHILEMON_18 = "https://private-693c3e-topicscourseenglihphilemon.apiary-mock.com/";
    public static final String BASE_URL_HEBREWS_19 = "https://private-5a656e-topicscourseenglihhebrews.apiary-mock.com/";
    public static final String BASE_URL_JAMES_20 = "https://private-39e4b-topicscourseenglihjames.apiary-mock.com/";
    public static final String BASE_URL_1PETER_21 = "https://private-dc7f21-topicscourseenglih1peter.apiary-mock.com/";
    public static final String BASE_URL_2PETER_22 = "https://private-f07ca3-topicscourseenglih2peter.apiary-mock.com/";
    public static final String BASE_URL_1JOHN_23 = "https://private-3e98c9-topicscourseenglih1john.apiary-mock.com/";
    public static final String BASE_URL_2JOHN_24 = "https://private-7a309d-topicscourseenglih2john.apiary-mock.com/";
    public static final String BASE_URL_3JOHN_25 = "https://private-c8f19-topicscourseenglih3john.apiary-mock.com";
    public static final String BASE_URL_JUDE_26 = "https://private-c8eb87-topicscourseenglihjude.apiary-mock.com/";
    public static final String BASE_URL_REVELATION_27 = "https://private-f00cba-topicscourseenglihrevelation.apiary-mock.com/";
    public static final String BASE_URL_GENESIS_28 = "https://private-fa1b3-topicscourseenglihgenesis.apiary-mock.com/";
    public static final String BASE_URL_EXODUS_29 = "https://private-85c27-topicscourseenglihexodus.apiary-mock.com/";
    public static final String BASE_URL_LEVITICUS_30 = "https://private-f50a1-topicscourseenglihleviticus.apiary-mock.com/";
    public static final String BASE_URL_NUMBERS_31 = "https://private-83d19b-topicscourseenglihnumbers.apiary-mock.com/";
    public static final String BASE_URL_DEUTERONOMY_32 = "https://private-c3afc4-topicscourseenglihdeuteronomy.apiary-mock.com/";
    public static final String BASE_URL_JOSHUA_33 = "https://private-44a21-topicscourseenglihjoshua.apiary-mock.com/";
    public static final String BASE_URL_JUDGES_34 = "https://private-6908c-topicscourseenglihjudges.apiary-mock.com/";
    public static final String BASE_URL_RUTH_35 = "https://private-ad947-topicscourseenglihruth.apiary-mock.com/";
    public static final String BASE_URL_1SAMUEL_36 = "https://private-f7e1a-topicscourseenglih1samuel.apiary-mock.com/";
    public static final String BASE_URL_2SAMUEL_37 = "https://private-d8f141-topicscourseenglih2samuel.apiary-mock.com/";
    public static final String BASE_URL_1KINGS_38 = "https://private-49c06-topicscourseenglih1kings.apiary-mock.com/";
    public static final String BASE_URL_2KINGS_39 = "https://private-73bbc-topicscourseenglih2kings.apiary-mock.com/";
    public static final String BASE_URL_1CHRONICLES_40 = "https://private-860130-topicscourseenglih1chronicles.apiary-mock.com/";
    public static final String BASE_URL_2CHRONICLES_41 = "https://private-3f6c5-topicscourseenglih2chronicles.apiary-mock.com/";
    public static final String BASE_URL_EZRA_42 = "https://private-99cfaa-topicscourseenglihezra.apiary-mock.com/";
    public static final String BASE_URL_NEHEMIAH_43 = "https://private-c639c1-topicscourseenglihnehemiah.apiary-mock.com/";
    public static final String BASE_URL_ESTHER_44 = "https://private-7a51f-topicscourseenglihesther.apiary-mock.com/";
    public static final String BASE_URL_JOB_45 = "https://private-c5a79-topicscourseenglihjob.apiary-mock.com/";
    public static final String BASE_URL_PSALMS_46 = "https://private-8728910-topicscourseenglihpsalms.apiary-mock.com/";
    public static final String BASE_URL_PROVERBS_47 = "https://private-15a9f-topicscourseenglihproverbs.apiary-mock.com/";
    public static final String BASE_URL_ECCLESIASTES_48 = "https://private-7d1d3-topicscourseenglihecclesiastes.apiary-mock.com/";
    public static final String BASE_URL_THESONGOFSOLOMON_49 = "https://private-d09d5a-topicscourseenglihthesongofsolomon.apiary-mock.com/";
    public static final String BASE_URL_ISAIAH_50 = "https://private-b2cd2-topicscourseenglihisaiah.apiary-mock.com/";
    public static final String BASE_URL_JEREMIAH_51 = "https://private-87c51f-topicscourseenglihjeremiah.apiary-mock.com/";
    public static final String BASE_URL_LAMENTATIONS_52 = "https://private-d358a-topicscourseenglihlamentations.apiary-mock.com/";
    public static final String BASE_URL_EZEKIEL_53 = "https://private-89d002-topicscourseenglihezekiel.apiary-mock.com/";
    public static final String BASE_URL_DANIEL_54 = "https://private-2ff3f-topicscourseenglihdaniel.apiary-mock.com/";
    public static final String BASE_URL_HOSEA_55 = "https://private-910533-topicscourseenglihhosea.apiary-mock.com/";
    public static final String BASE_URL_JOEL_56 = "https://private-63bf48-topicscourseenglihjoel.apiary-mock.com/";
    public static final String BASE_URL_AMOS_57 = "https://private-be9f0e-topicscourseenglihamos.apiary-mock.com/";
    public static final String BASE_URL_OBADIAH_58 = "https://private-c5922-topicscourseenglihobadiah.apiary-mock.com/";
    public static final String BASE_URL_JONAH_59 = "https://private-434648-topicscourseenglihjonah.apiary-mock.com/";
    public static final String BASE_URL_MICAH_60 = "https://private-c629c3-topicscourseenglihmicah.apiary-mock.com/";
    public static final String BASE_URL_NAHUM_61 = "https://private-ecfd79-topicscourseenglihnahum.apiary-mock.com/";
    public static final String BASE_URL_HABAKKUK_62 = "https://private-d521a-topicscourseenglihhabakkuk.apiary-mock.com/";
    public static final String BASE_URL_ZEPHANIAH_63 = "https://private-f096769-topicscourseenglihzechariah.apiary-mock.com/";
    public static final String BASE_URL_HAGGAI_64 = "https://private-4dfd9d-topicscourseenglihhaggai.apiary-mock.com/";
    public static final String BASE_URL_ZECHARIAH_65 = "https://private-fec2b-topicscourseenglihzephaniah.apiary-mock.com/";
    public static final String BASE_URL_MALACHI_66 = "https://private-4297b-topicscourseenglihmalachi.apiary-mock.com/";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .create();


    public static Retrofit getClient(Activity activity) {


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

       String preferenceKeyBible =  sharedPref.getString(activity.getString(R.string.preference_key_bible),null);



        base_url  = BASE_URL_MATHEUS_1;
        if (preferenceKeyBible != null) {
            if (preferenceKeyBible.equals("0")) {
                base_url = BASE_URL_MATHEUS_1;
            } else if (preferenceKeyBible.equals("1")) {
                base_url = BASE_URL_MARK_2;
            } else if (preferenceKeyBible.equals("2")) {
                base_url = BASE_URL_LUKE_3;
            } else if (preferenceKeyBible.equals("3")) {
                base_url = BASE_URL_JOHN_4;
            } else if (preferenceKeyBible.equals("4")) {
                base_url = BASE_URL_ACTS_5;
            } else if (preferenceKeyBible.equals("5")) {
                base_url = BASE_URL_ROMANS_6;
            } else if (preferenceKeyBible.equals("6")) {
                base_url = BASE_URL_1CORINTHIANS_7;
            } else if (preferenceKeyBible.equals("7")) {
                base_url = BASE_URL_2CORINTHIANS_8;
            } else if (preferenceKeyBible.equals("8")) {
                base_url = BASE_URL_GALATIANS_9;
            } else if (preferenceKeyBible.equals("9")) {
                base_url = BASE_URL_EPHESIANS_10;
            } else if (preferenceKeyBible.equals("10")) {
                base_url = BASE_URL_PHILIPPIANS_11;
            } else if (preferenceKeyBible.equals("11")) {
                base_url = BASE_URL_COLOSSIANS_12;
            } else if (preferenceKeyBible.equals("12")) {
                base_url = BASE_URL_1THESSALONIANS_13;
            } else if (preferenceKeyBible.equals("13")) {
                base_url = BASE_URL_2THESSALONIANS_14;
            } else if (preferenceKeyBible.equals("14")) {
                base_url = BASE_URL_1TIMOTHY_15;
            } else if (preferenceKeyBible.equals("15")) {
                base_url = BASE_URL_2TIMOTHY_16;
            } else if (preferenceKeyBible.equals("16")) {
                base_url = BASE_URL_TITUS_17;
            } else if (preferenceKeyBible.equals("17")) {
                base_url = BASE_URL_PHILEMON_18;
            } else if (preferenceKeyBible.equals("18")) {
                base_url = BASE_URL_HEBREWS_19;
            } else if (preferenceKeyBible.equals("19")) {
                base_url = BASE_URL_JAMES_20;
            } else if (preferenceKeyBible.equals("20")) {
                base_url = BASE_URL_1PETER_21;
            } else if (preferenceKeyBible.equals("21")) {
                base_url = BASE_URL_2PETER_22;
            } else if (preferenceKeyBible.equals("22")) {
                base_url = BASE_URL_1JOHN_23;
            } else if (preferenceKeyBible.equals("23")) {
                base_url = BASE_URL_2JOHN_24;
            } else if (preferenceKeyBible.equals("24")) {
                base_url = BASE_URL_3JOHN_25;
            } else if (preferenceKeyBible.equals("25")) {
                base_url = BASE_URL_JUDE_26;
            } else if (preferenceKeyBible.equals("26")) {
                base_url = BASE_URL_REVELATION_27;
            } else if (preferenceKeyBible.equals("27")) {
                base_url = BASE_URL_GENESIS_28;
            } else if (preferenceKeyBible.equals("28")) {
                base_url = BASE_URL_EXODUS_29;
            } else if (preferenceKeyBible.equals("29")) {
                base_url = BASE_URL_LEVITICUS_30;
            } else if (preferenceKeyBible.equals("30")) {
                base_url = BASE_URL_NUMBERS_31;
            } else if (preferenceKeyBible.equals("31")) {
                base_url = BASE_URL_DEUTERONOMY_32;
            } else if (preferenceKeyBible.equals("32")) {
                base_url = BASE_URL_JOSHUA_33;
            } else if (preferenceKeyBible.equals("33")) {
                base_url = BASE_URL_JUDGES_34;
            } else if (preferenceKeyBible.equals("34")) {
                base_url = BASE_URL_RUTH_35;
            } else if (preferenceKeyBible.equals("35")) {
                base_url = BASE_URL_1SAMUEL_36;
            } else if (preferenceKeyBible.equals("36")) {
                base_url = BASE_URL_2SAMUEL_37;
            } else if (preferenceKeyBible.equals("37")) {
                base_url = BASE_URL_1KINGS_38;
            } else if (preferenceKeyBible.equals("38")) {
                base_url = BASE_URL_2KINGS_39;
            } else if (preferenceKeyBible.equals("39")) {
                base_url = BASE_URL_1CHRONICLES_40;
            } else if (preferenceKeyBible.equals("40")) {
                base_url = BASE_URL_2CHRONICLES_41;
            } else if (preferenceKeyBible.equals("41")) {
                base_url = BASE_URL_EZRA_42;
            } else if (preferenceKeyBible.equals("42")) {
                base_url = BASE_URL_NEHEMIAH_43;
            } else if (preferenceKeyBible.equals("43")) {
                base_url = BASE_URL_ESTHER_44;
            } else if (preferenceKeyBible.equals("44")) {
                base_url = BASE_URL_JOB_45;
            } else if (preferenceKeyBible.equals("45")) {
                base_url = BASE_URL_PSALMS_46;
            } else if (preferenceKeyBible.equals("46")) {
                base_url = BASE_URL_PROVERBS_47;
            } else if (preferenceKeyBible.equals("47")) {
                base_url = BASE_URL_ECCLESIASTES_48;
            } else if (preferenceKeyBible.equals("48")) {
                base_url = BASE_URL_THESONGOFSOLOMON_49;
            } else if (preferenceKeyBible.equals("49")) {
                base_url = BASE_URL_ISAIAH_50;
            } else if (preferenceKeyBible.equals("50")) {
                base_url = BASE_URL_JEREMIAH_51;
            } else if (preferenceKeyBible.equals("51")) {
                base_url = BASE_URL_LAMENTATIONS_52;
            } else if (preferenceKeyBible.equals("52")) {
                base_url = BASE_URL_EZEKIEL_53;
            } else if (preferenceKeyBible.equals("53")) {
                base_url = BASE_URL_DANIEL_54;
            } else if (preferenceKeyBible.equals("54")) {
                base_url = BASE_URL_HOSEA_55;
            } else if (preferenceKeyBible.equals("55")) {
                base_url = BASE_URL_JOEL_56;
            } else if (preferenceKeyBible.equals("56")) {
                base_url = BASE_URL_AMOS_57;
            } else if (preferenceKeyBible.equals("57")) {
                base_url = BASE_URL_OBADIAH_58;
            } else if (preferenceKeyBible.equals("58")) {
                base_url = BASE_URL_JONAH_59;
            } else if (preferenceKeyBible.equals("59")) {
                base_url = BASE_URL_MICAH_60;
            } else if (preferenceKeyBible.equals("60")) {
                base_url = BASE_URL_NAHUM_61;
            } else if (preferenceKeyBible.equals("61")) {
                base_url = BASE_URL_HABAKKUK_62;
            } else if (preferenceKeyBible.equals("62")) {
                base_url = BASE_URL_ZEPHANIAH_63;
            } else if (preferenceKeyBible.equals("63")) {
                base_url = BASE_URL_HAGGAI_64;
            } else if (preferenceKeyBible.equals("64")) {
                base_url = BASE_URL_ZECHARIAH_65;
            } else if (preferenceKeyBible.equals("65")) {
                base_url = BASE_URL_MALACHI_66;
            }
        }



       // if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .client(getUnsafeOkHttpClient().build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
       // }
        return retrofit;
    }


    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
