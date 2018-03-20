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


            final int blockSize = 11111;
            byte[] buffer = new byte[blockSize];
            long totalReceived = 0;

            int blockLength = 0;
            LogContext contextReceiving = App.context("Receiving file");
            while(blockLength >= 0 && totalReceived < expectedSize) {
                if(socketInputStream.available() > 0) {
                    blockLength = inputStream.read(buffer);
                    outTemp.write(buffer, 0, blockLength);
                    App.log(contextReceiving).reset().add("Latest received block size is " + blockLength);
                    totalReceived += blockLength;
                    App.log(contextReceiving).add("total received " + totalReceived);
                } // TODO do something _else_ if no data available
            }
            if (totalReceived > expectedSize)
                throw new RuntimeException("Sent file is bigger than were announced" + contextReceiving.produce());
            if (totalReceived < expectedSize)
                throw new RuntimeException("Sent file is smaller than were announced" + contextReceiving.produce());
            App.log(contextReceiving).add("last length " + blockLength);
            App.log(contextReceiving).add("Received " + totalReceived);
            outTemp.close();
            App.log().info(contextReceiving.produce());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            LOG.severe("Bad client or intruder detected. (" + e.getMessage() + ")");
        } finally {
        }
    }

}
