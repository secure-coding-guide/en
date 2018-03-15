package org.jssec.android.log.outputredirection;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import android.app.Application;

public class OutputRedirectApplication extends Application {

    // PrintStream which is not output anywhere
    private final PrintStream emptyStream = new PrintStream(new OutputStream() {
        public void write(int oneByte) throws IOException {
            // do nothing
        }
    });

    @Override
    public void onCreate() {
        // Redirect System.out/err to PrintStream which doesn't output anywhere, when release build.

        // Save original stream of System.out/err
        PrintStream savedOut = System.out;
        PrintStream savedErr = System.err;

        // Once, redirect System.out/err to PrintStream which doesn't output anywhere
        System.setOut(emptyStream);
        System.setErr(emptyStream);

        // Restore the original stream only when debugging. (In release build, the following 1 line is deleted byProGuard.)
        resetStreams(savedOut, savedErr);
    }

    // All of the following methods are deleted byProGuard when release.
    private void resetStreams(PrintStream savedOut, PrintStream savedErr) {
        System.setOut(savedOut);
        System.setErr(savedErr);
    }
}
