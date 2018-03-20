package common;

import common.core.App;
import common.loggers.LogContext;

import java.io.*;
import java.util.logging.Logger;

public class FileManager {

    public static void receiveBinary(Long expectedSize, InputStream socketInputStream) {
        final Logger LOG = App.log();
        File tempFile = new File("./data/tmp/file.pdf");
        try {
            BufferedOutputStream outTemp = new BufferedOutputStream(new FileOutputStream(tempFile));
            BufferedInputStream inputStream = new BufferedInputStream(socketInputStream);


            final int blockSize = 102400;
            byte[] buffer = new byte[blockSize];
            long totalReceived = 0;

            int length;
            LogContext contextReceiving = App.context("Receiving file");
            App.verbose("Receiving file");
            for (length = inputStream.read(buffer); length >= 0; length = inputStream.read(buffer)) {
                outTemp.write(buffer, 0, length);
                App.log(contextReceiving).reset().add("Latest received block size is " + length);
                App.verbose("Latest received block size is " + length);
                totalReceived += length;
                App.log(contextReceiving).add("total received " + totalReceived);
                App.verbose("total received " + totalReceived);
                if (totalReceived > expectedSize) {
                    App.verbose("totalReceived > expectedSize");
                    throw new RuntimeException("Sent file is bigger than were announced" + contextReceiving.produce());

                }
            }
            System.out.println("whyyyyyyyyyyy");
            App.verbose("before produce 1");
            if (totalReceived < expectedSize)
                throw new RuntimeException("Sent file is smaller than were announced" + contextReceiving.produce());
            App.log(contextReceiving).add("last length " + length);
            App.log(contextReceiving).add("Received " + totalReceived);
            App.verbose("before produce 8");
            outTemp.close();
            App.verbose("before produce 9");
            App.log().info(contextReceiving.produce());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            LOG.severe("Bad client or intruder detected. (" + e.getMessage() + ")");
        } finally {
            App.verbose("receiveBinary finally");
        }
        App.verbose("exit from receiveBinary");

        App.log().warning("wtf");
    }

}
