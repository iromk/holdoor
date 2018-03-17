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


            final int blockSize = 10240;
            byte[] buffer = new byte[blockSize];
            long totalReceived = 0;

            int length;
            LogContext contextReceiving = App.context("Receiving file");
            for (length = inputStream.read(buffer); length >= 0; length = inputStream.read(buffer)) {
                outTemp.write(buffer, 0, length);
                App.log(contextReceiving).reset().add("Latest received block size is " + length);
                totalReceived += length;
                App.log(contextReceiving).add("total received " + totalReceived);
                if (totalReceived > expectedSize)
                    throw new RuntimeException("Sent file is bigger than were announced" + contextReceiving.produce());
            }
            if (totalReceived < expectedSize)
                throw new RuntimeException("Sent file is smaller than were announced" + contextReceiving.produce());
            App.log(contextReceiving).add("last length " + length);
            App.log(contextReceiving).add("Received " + totalReceived);
            outTemp.close();
            App.log().info(contextReceiving.produce());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            LOG.severe("Bad client or intruder detected. (" + e.getMessage() + ")");
        }


    }

}
