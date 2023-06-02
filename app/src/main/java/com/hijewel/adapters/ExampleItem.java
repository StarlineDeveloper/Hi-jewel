package com.hijewel.adapters;

public class ExampleItem {

    private String mText1;
    private String PDFFilesPath;

    public ExampleItem(String text1,String test2) {
        mText1 = text1;
        PDFFilesPath = test2;
    }


    public String getText1() {
        return mText1;
    }

    public String getPDFFilesPath() {
        return PDFFilesPath;
    }

}
