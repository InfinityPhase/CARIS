package lavaplayer.filter;

import lavaplayer.format.transcoder.AudioChunkEncoder;
import lavaplayer.track.playback.AudioFrame;
import lavaplayer.track.playback.AudioProcessingContext;

import java.nio.ShortBuffer;

/**
 * Post processor which encodes audio chunks and passes them as audio frames to the frame buffer.
 */
public class BufferingPostProcessor implements AudioPostProcessor {
  private final AudioProcessingContext context;
  private final AudioChunkEncoder encoder;

  /**
   * @param context Processing context to determine the destination buffer from.
   * @param encoder Encoder to encode the chunk with.
   */
  public BufferingPostProcessor(AudioProcessingContext context, AudioChunkEncoder encoder) {
    this.encoder = encoder;
    this.context = context;
  }

  @Override
  public void process(long timecode, ShortBuffer buffer) throws InterruptedException {
    context.frameConsumer.consume(new AudioFrame(timecode, encoder.encode(buffer), context.volumeLevel.get(), context.outputFormat));
  }

  @Override
  public void close() {
    encoder.close();
  }
}
