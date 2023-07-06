package com.example.warkopzara.providers;

import org.chromium.net.UploadDataProvider;
import org.chromium.net.UploadDataSink;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MyUploadDataProvider extends UploadDataProvider {
    private String data;

    public MyUploadDataProvider(String data) {
        this.data = data;
    }

    @Override
    public long getLength() throws IOException {
        return data.length();
    }

    @Override
    public void read(UploadDataSink uploadDataSink, ByteBuffer byteBuffer) throws IOException {

    }

    @Override
    public void rewind(UploadDataSink uploadDataSink) throws IOException {

    }
}