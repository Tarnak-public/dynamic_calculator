package com.example.calculator;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileUtilities {
    static byte[] bufferGlobal = new byte[8 * 1024];
    static Lock bufferGlobalLock = new ReentrantLock();

    public static void copyFromAssetsToAppPrivate(Context context, String sourceAssetsFile) throws IOException {

        String DestinationFile = context.getFilesDir().getPath() + File.separator + sourceAssetsFile;
        copyFromAssetsToStorage(context, sourceAssetsFile, DestinationFile);
    }

    public static void copyFromAssetsToStorage(Context context, String sourceFile, String destinationFile) throws IOException {

        InputStream IS = context.getAssets().open(sourceFile);
        OutputStream OS = new FileOutputStream(destinationFile);
        copyStream(IS, OS);
        OS.flush();
        OS.close();
        IS.close();
    }

    private static void copyStream(InputStream Input, OutputStream Output) throws IOException {

        if (bufferGlobalLock.tryLock()) {
            try {
                copyStreams(Input, Output, bufferGlobal);
            } finally {
                bufferGlobalLock.unlock();
            }
        } else {
            byte[] tmpBuffer;
            tmpBuffer = new byte[8 * 1024];
            copyStreams(Input, Output, tmpBuffer);
        }
    }

    private static void copyStreams(InputStream Input, OutputStream Output, byte[] buffer) throws IOException {
        int length = Input.read(buffer);

        while (length > 0) {
            Output.write(buffer, 0, length);
            length = Input.read(buffer);
        }
    }
}
