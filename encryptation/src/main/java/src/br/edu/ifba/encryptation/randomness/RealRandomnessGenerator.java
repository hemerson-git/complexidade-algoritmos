package src.br.edu.ifba.encryptation.randomness;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import src.br.edu.ifba.encryptation.exceptions.KeyGenerationFailure;

public class RealRandomnessGenerator extends SecureRandom {
    private static final int FRAME_CAPTURE_ATTEMPTS = 20;

    private FFmpegFrameGrabber grabber;

    @SuppressWarnings("UseSpecificCatch")
    public RealRandomnessGenerator(String videoPath) throws KeyGenerationFailure {
        Loader.load(org.bytedeco.opencv.global.opencv_core.class);

        grabber = new FFmpegFrameGrabber(videoPath);
        try {
            grabber.start();
        } catch (Exception e) {
            throw new KeyGenerationFailure("initialization failure: " + e.getMessage());
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private Frame nextFrame() throws KeyGenerationFailure {
        Frame frame = null;

        try {
            frame = grabber.grab();
        } catch (Exception e) {
            throw new KeyGenerationFailure("initialization failure: " + e.getMessage());
        }

        return frame;
    }

    private BufferedImage nextImage() throws KeyGenerationFailure {
        BufferedImage image = null;

        try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
            int attempts = 0;
            do {
                attempts++;
                
                Frame frame = nextFrame();
                image = converter.convert(frame);
            } while ((image == null) && (attempts < FRAME_CAPTURE_ATTEMPTS));
        }

        return image;
    }    
    @Override
    public int nextInt() {
        int val = 0;

        int[] randomness = getRandomness();
        if (randomness != null && randomness.length >= 4) {
            val |= randomness[0] << 24;
            val |= randomness[1] << 16;
            val |= randomness[2] << 8;
            val |= randomness[3];
        }

        return val;
    }

    @Override
    public long nextLong() {
        long val = 0;

        int[] randomness = getRandomness();
        if (randomness != null && randomness.length >= 8) {
            val |= (long) randomness[0] << 56;
            val |= (long) randomness[1] << 48;
            val |= (long) randomness[3] << 40;
            val |= (long) randomness[4] << 32;
            val |= (long) randomness[5] << 24;
            val |= (long) randomness[6] << 16;
            val |= (long) randomness[7] << 8;
            val |= (long) randomness[8];
        }

        return val;
    }

    private int[] getRandomness() {
        int[] randomness = null;

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            ImageIO.write(nextImage(), "jpg", stream);
            byte[] bytes = stream.toByteArray();
            
            randomness = new int[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                randomness[i] = bytes[i] & 0xff; 
            }
        } catch (IOException | KeyGenerationFailure e) {
            e.printStackTrace();
        }

        return randomness;
    }

    @SuppressWarnings("UseSpecificCatch")
    public void finalize() throws KeyGenerationFailure {
        try {
            grabber.stop();
            grabber.release();
        } catch (Exception e) {
            throw new KeyGenerationFailure("finalization failure: " + e.getMessage());
        }
    }

}
