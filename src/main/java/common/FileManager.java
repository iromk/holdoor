package common;

import java.io.*;
import java.util.logging.Logger;

public class FileManager {

    public static void receiveBinary(Long expectedSize, InputStream socketInputStream) {
        final Logger LOG = App.log(); //Environment.getInstance().getLogger();
        File tempFile = new File("./data/tmp/file.pdf");
        try {
            BufferedOutputStream outTemp = new BufferedOutputStream(new FileOutputStream(tempFile));
            BufferedInputStream inputStream = new BufferedInputStream(socketInputStream);


            final int blockSize = 10240;
            byte[] buffer = new byte[blockSize];
            long totalReceived = 0;

            int length;
            for (length = inputStream.read(buffer); length >= 0; length = inputStream.read(buffer)) {
                outTemp.write(buffer, 0, length);
                LOG.info("Block of size received " + length);
                totalReceived += length;
                if (totalReceived > expectedSize) throw new RuntimeException("Sent file is bigger than were announced");
            }
            if (totalReceived < expectedSize) throw new RuntimeException("Sent file is smaller than were announced");
            LOG.info("last length " + length);
            LOG.info("Received " + totalReceived);
            outTemp.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            LOG.severe("Bad client or intruder detected. (" + e.getMessage() + ")");
        }


    }

}
