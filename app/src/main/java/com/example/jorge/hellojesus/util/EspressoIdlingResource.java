package com.example.jorge.hellojesus.util;

import android.support.test.espresso.IdlingResource;

/**
 * Created by jorge on 13/04/2018.
 */

public class EspressoIdlingResource{
       private static final String RESOURCE = "GLOBAL";

        private static SimpleCountingIdlingResource mCountingIdlingResource =
                new SimpleCountingIdlingResource(RESOURCE);

        public static void increment() {
            mCountingIdlingResource.increment();
        }

        public static void decrement() {
            mCountingIdlingResource.decrement();
        }

        public static IdlingResource getIdlingResource() {
            return mCountingIdlingResource;
        }
}

