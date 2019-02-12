package com.softjads.jorge.hellojesus.data.onLine.topic;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softjads.jorge.hellojesus.R;

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
 * Created by jorge on 21/02/2018.
 */

public class TopicClient {

    static String  base_url ="";


    public static final String BASE_URL_MATHEUS_1 = "https://private-68dc03-bibliematthew.apiary-mock.com/";
    public static final String BASE_URL_MARK_2 = "https://private-42870-bibliemark.apiary-mock.com/";
    public static final String BASE_URL_LUKE_3 = "https://private-21d1b7-biblieluke.apiary-mock.com/";
    public static final String BASE_URL_JOHN_4 = "https://private-625324-bibliejohn.apiary-mock.com/";
    public static final String BASE_URL_ACTS_5 = "https://private-a7d2e-biblieacts.apiary-mock.com/";
    public static final String BASE_URL_ROMANS_6 = "https://private-a7bb1c-biblieromans.apiary-mock.com/";
    public static final String BASE_URL_1CORINTHIANS_7 = "https://private-ebfa9-biblie1corinthians.apiary-mock.com/";
    public static final String BASE_URL_2CORINTHIANS_8 = "https://private-0647f7-biblie2corinthians.apiary-mock.com/";
    public static final String BASE_URL_GALATIANS_9 = "https://private-02a070-bibliegalatians.apiary-mock.com/";
    public static final String BASE_URL_EPHESIANS_10 = "https://private-507523-biblieephesians.apiary-mock.com/";
    public static final String BASE_URL_PHILIPPIANS_11 = "https://private-2aebe-bibliephilippians.apiary-mock.com/";
    public static final String BASE_URL_COLOSSIANS_12 = "https://private-b2c57a-bibliecolossians.apiary-mock.com/";
    public static final String BASE_URL_1THESSALONIANS_13 = "https://private-324c09-biblie1thessalonians.apiary-mock.com/";
    public static final String BASE_URL_2THESSALONIANS_14 = "https://private-f137a-biblie2thessalonians.apiary-mock.com/";
    public static final String BASE_URL_1TIMOTHY_15 = "https://private-f0b4c9-biblie1timothy.apiary-mock.com/";
    public static final String BASE_URL_2TIMOTHY_16 = "https://private-8e0e00-biblie2timothy.apiary-mock.com/";
    public static final String BASE_URL_TITUS_17 = "https://private-311423-biblietitus.apiary-mock.com/";
    public static final String BASE_URL_PHILEMON_18 = "https://private-c1475-bibliephilemon.apiary-mock.com/";
    public static final String BASE_URL_HEBREWS_19 = "https://private-a09533-bibliehebrews.apiary-mock.com/";
    public static final String BASE_URL_JAMES_20 = "https://private-39e4b-topicscourseenglihjames.apiary-mock.com/";
    public static final String BASE_URL_1PETER_21 = "https://private-9df441-biblie1peter.apiary-mock.com/";
    public static final String BASE_URL_2PETER_22 = "https://private-615280-biblie2peter.apiary-mock.com/";
    public static final String BASE_URL_1JOHN_23 = "https://private-03e5ac-biblie1john.apiary-mock.com/";
    public static final String BASE_URL_2JOHN_24 = "https://private-34b99-biblie2john.apiary-mock.com/";
    public static final String BASE_URL_3JOHN_25 = "https://private-353fe-biblie3john.apiary-mock.com/";
    public static final String BASE_URL_JUDE_26 = "https://private-70286-bibliejude.apiary-mock.com/";
    public static final String BASE_URL_REVELATION_27 = "https://private-8cd7fd-biblierevelation.apiary-mock.com/";
    public static final String BASE_URL_GENESIS_28 = "https://private-0b208-bibliegenesis.apiary-mock.com/";
    public static final String BASE_URL_EXODUS_29 = "https://private-558c0-biblieexodus.apiary-mock.com/";
    public static final String BASE_URL_LEVITICUS_30 = "https://private-06649f-biblieleviticus.apiary-mock.com/";
    public static final String BASE_URL_NUMBERS_31 = "https://private-422821-biblienumbers.apiary-mock.com/";
    public static final String BASE_URL_DEUTERONOMY_32 = "https://private-6e60df-bibliedeuteronomy.apiary-mock.com/";
    public static final String BASE_URL_JOSHUA_33 = "https://private-d393b0-bibliejoshua.apiary-mock.com/";
    public static final String BASE_URL_JUDGES_34 = "https://private-ad718-bibliejudges.apiary-mock.com/";
    public static final String BASE_URL_RUTH_35 = "https://private-309f8-biblieruth.apiary-mock.com/";
    public static final String BASE_URL_1SAMUEL_36 = "https://private-a1ba65-biblie1samuel.apiary-mock.com/";
    public static final String BASE_URL_2SAMUEL_37 = "https://private-f350f-biblie2samuel.apiary-mock.com/";
    public static final String BASE_URL_1KINGS_38 = "https://private-eaa633-biblie1kings.apiary-mock.com/";
    public static final String BASE_URL_2KINGS_39 = "https://private-f725df-biblie2kings.apiary-mock.com/";
    public static final String BASE_URL_1CHRONICLES_40 = "https://private-df007f-biblie1chronicles.apiary-mock.com/";
    public static final String BASE_URL_2CHRONICLES_41 = "https://private-30cd4b-biblie2chronicles.apiary-mock.com/";
    public static final String BASE_URL_EZRA_42 = "https://private-2ec19-biblieezra.apiary-mock.com/";
    public static final String BASE_URL_NEHEMIAH_43 = "https://private-b5707-biblienehemiah.apiary-mock.com/";
    public static final String BASE_URL_ESTHER_44 = "https://private-604e9c-biblieesther.apiary-mock.com/";
    public static final String BASE_URL_JOB_45 = "https://private-890bf-bibliejob.apiary-mock.com/";
    public static final String BASE_URL_PSALMS_46 = "https://private-458513-bibliepsalms.apiary-mock.com/";
    public static final String BASE_URL_PROVERBS_47 = "https://private-688e0a-biblieproverbs.apiary-mock.com/";
    public static final String BASE_URL_ECCLESIASTES_48 = "https://private-4fc21-biblieecclesiastes.apiary-mock.com/";
    public static final String BASE_URL_THESONGOFSOLOMON_49 = "https://private-87a888-bibliethesongofsolomon.apiary-mock.com/";
    public static final String BASE_URL_ISAIAH_50 = "https://private-034ca-biblieisaiah.apiary-mock.com/";
    public static final String BASE_URL_JEREMIAH_51 = "https://private-dc1a82-bibliejeremiah.apiary-mock.com/";
    public static final String BASE_URL_LAMENTATIONS_52 = "https://private-3eb633-biblielamentations.apiary-mock.com/";
    public static final String BASE_URL_EZEKIEL_53 = "https://private-272ec-biblieezekiel.apiary-mock.com/";
    public static final String BASE_URL_DANIEL_54 = "https://private-5241d-bibliedaniel.apiary-mock.com/";
    public static final String BASE_URL_HOSEA_55 = "https://private-7493dc-bibliehosea.apiary-mock.com/";
    public static final String BASE_URL_JOEL_56 = "https://private-14c8d-bibliejoel.apiary-mock.com/";
    public static final String BASE_URL_AMOS_57 = "https://private-5b0cb2-biblieamos.apiary-mock.com/";
    public static final String BASE_URL_OBADIAH_58 = "https://private-ac7232-biblieobadiah.apiary-mock.com/";
    public static final String BASE_URL_JONAH_59 = "https://private-ec9fe-bibliejonah.apiary-mock.com/";
    public static final String BASE_URL_MICAH_60 = "https://private-43fed-bibliemicah.apiary-mock.com/";
    public static final String BASE_URL_NAHUM_61 = "https://private-1223ed-biblienahum.apiary-mock.com/";
    public static final String BASE_URL_HABAKKUK_62 = "https://private-6e179-bibliehabakkuk.apiary-mock.com/";
    public static final String BASE_URL_ZEPHANIAH_63 = "https://private-c07b49-bibliezephaniah.apiary-mock.com/";
    public static final String BASE_URL_HAGGAI_64 = "https://private-0f444f-bibliehaggai.apiary-mock.com/";
    public static final String BASE_URL_ZECHARIAH_65 = "https://private-e2753e-bibliezechariah.apiary-mock.com/";
    public static final String BASE_URL_MALACHI_66 = "https://private-61c79c-bibliemalachi.apiary-mock.com/";

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .create();


    public static Retrofit getClient(Activity activity) {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);

        String preferenceKeyBible =  sharedPref.getString(activity.getString(R.string.preference_key_bible),null);



        base_url = BASE_URL_MATHEUS_1;
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
