package org.vosk;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class LibVosk {

    private static void unpackDll(File targetDir, String lib) throws IOException {
        try (InputStream source = LibVosk.class.getResourceAsStream("/win32-x86-64/" + lib + ".dll")) {
            Files.copy(source, new File(targetDir, lib + ".dll").toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    static {

        if (Platform.isWindows()) {
            // We have to unpack dependencies
            try {
                // To get a tmp folder we unpack small library and mark it for deletion
                File tmpFile = Native.extractFromResourcePath("/win32-x86-64/empty", LibVosk.class.getClassLoader());
                File tmpDir = tmpFile.getParentFile();
                new File(tmpDir, tmpFile.getName() + ".x").createNewFile();

                // Now unpack dependencies
                unpackDll(tmpDir, "libwinpthread-1");
                unpackDll(tmpDir, "libgcc_s_seh-1");
                unpackDll(tmpDir, "libstdc++-6");

            } catch (IOException e) {
                // Nothing for now, it will fail on next step
            } finally {
                Native.register(LibVosk.class, "libvosk");
            }
        } else {
            Native.register(LibVosk.class, "vosk");
        }
    }

    public static native void vosk_set_log_level(int level);

    public static native Pointer vosk_model_new(String path);

    public static native void vosk_model_free(Pointer model);

    public static native Pointer vosk_recognizer_new(Model model, float sample_rate);

    public static native void vosk_recognizer_accept_waveform(Pointer recognizer, byte[] data, int len);

    public static native void vosk_recognizer_accept_waveform_s(Pointer recognizer, short[] data, int len);

    public static native void vosk_recognizer_accept_waveform_f(Pointer recognizer, float[] data, int len);

    public static native String vosk_recognizer_result_front(Pointer recognizer);

    public static native void vosk_recognizer_result_pop(Pointer recognizer);

    public static native int vosk_recognizer_get_pending_results(Pointer recognizer);

    public static native void vosk_recognizer_reset(Pointer recognizer);

    public static native void vosk_recognizer_free(Pointer recognizer);

    /**
     * Set log level for Kaldi messages.
     *
     *  @param loglevel the level
     *     0 - default value to print info and error messages but no debug
     *     less than 0 - don't print info messages
     *     greater than 0 - more verbose mode
     */
    public static void setLogLevel(LogLevel loglevel) {
        vosk_set_log_level(loglevel.getValue());
    }
}