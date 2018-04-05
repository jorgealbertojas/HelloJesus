package com.example.libjavafortest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class myClass {


    private static List<String> ListBB;

    public static void main(String[] arg) {

        String A = "How do you call the methods of a class that was created in another";
        String B = "Calling method from another class Treehouse Community";
        String C = "How to call Main Activity method in another class in Android Quora";

        String A1 = "How do you call the methods of a class that was created in another";
        String B2 = "Calling method from another class Treehouse Community";
        String C3 = "How to call Main Activity method in another class in Android Quora";

        String AA1 = "java How can I sort a List alphabetically Stack Overflow";
        String BB2 = "Java Program to Sort Names in an Alphabetical Order Sanfoundry";
        String CC3 = "How to sort an ArrayList in java Mkyong com";

        List<String> ListA = new ArrayList<String>();
        ListA.add(A);
        ListA.add(B);
        ListA.add(C);

/*        List<String> ListB = new ArrayList<String>();
        ListB.add(A1);
        ListB.add(B2);
        ListB.add(C3);*/

        ListBB = new ArrayList<String>();
        ListBB.add(AA1);
        ListBB.add(BB2);
        ListBB.add(CC3);

        List<String> result = new ArrayList<String>();

        ListA = verifyTheWord(ListA);

        ListBB = verifyTheWord(ListBB);

        result = countPoint(ListA,ListBB,0);
        result = countPoint(ListA,ListBB,0);

    }


    private static List<String> verifyTheWord(List<String> eeee) {

        List<String> listString = new ArrayList<String>();

        for (int i = 0; i < eeee.size(); i++) {
            listString.addAll(getWordInPhase(eeee.get(i)))
            ;
        }

        Collections.sort(listString);
        listString = EliminateDuplicate(listString);


        return listString;
    }


    private static List<String> getWordInPhase(String phrase) {
        List<String> mListResult = new ArrayList<String>();
        int index = 0;

        while (phrase.length() > 0) {


            index = phrase.toString().indexOf(" ");

            if ((index > 0)) {

                mListResult.add(phrase.substring(0, index).toUpperCase());
                phrase = phrase.toString().substring(index + 1,phrase.length());

            }else{
                mListResult.add(phrase.toString().toUpperCase());
                phrase = "";
            }

        }

        return mListResult;
    }

    private static  List<String> EliminateDuplicate(List<String> listString){

        int i = 0;
        while ( i < (listString.size() - 1)){
            if (listString.get(i).toString().equals(listString.get(i+1).toString())){
                listString.remove(i+1);
                i--;
            }
            i++;
        }

        return listString;
    }

    private static  List<String> countPoint(List<String> correctList, List<String> myList, int i) {

        if (correctList.size() == i) {
            return correctList;
        }
        else if (myList.size() == 0) {
            i ++;
            myList = ListBB;
            return countPoint(correctList, myList,i);
        } else if (correctList.get(i).toString().equals(myList.get(0).toString())) {
            correctList.remove(i);
            myList.remove(0);
            return countPoint(correctList, myList,i);
        } else if (myList.size() > 0) {
            return countPoint(correctList, myList.subList(1, myList.size()),i);
        } else {
            return countPoint(correctList, myList, i);

        }

    }


}
