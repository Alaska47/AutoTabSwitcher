package cv;


import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import utils.ImageJPanel;


public class FacialRecognition {

	private ScheduledExecutorService timer;
	private org.opencv.highgui.VideoCapture capture;
	private boolean cameraActive;


	//TEMP
	ImageJPanel panel;

	// face cascade classifier
	private CascadeClassifier faceCascade;
	private int absoluteFaceSize;

	public FacialRecognition(ImageJPanel p) {
		panel = p;
		init();
	}

	private void init()
	{

		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.faceCascade.load("C:\\opencv\\build\\etc\\lbpcascades\\lbpcascade_frontalface.xml");
		this.absoluteFaceSize = 0;

		System.out.println("loaded everything");

		startCamera();
	}


	protected void startCamera()
	{

		if (!this.cameraActive)
		{

			// start the video capture
			this.capture.open(0);

			// is the video stream available?
			if (this.capture.isOpened())
			{
				this.cameraActive = true;

				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {

					@Override
					public void run()
					{
						BufferedImage imageToShow = grabFrame();
						panel.setImage(imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 100, TimeUnit.MILLISECONDS);

			}
			else
			{
				// log the error
				System.err.println("Failed to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;

			// stop the timer
			try
			{
				this.timer.shutdown();
				this.timer.awaitTermination(100, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log the exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}

			// release the camera
			this.capture.release();
		}
	}

	/**
	 * Get a frame from the opened video stream (if any)
	 * 
	 * @return the {@link Image} to show
	 */
	private BufferedImage grabFrame()
	{
		// init everything
		BufferedImage imageToShow = null;
		Mat frame = new Mat();

		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);

				// if the frame is not empty, process it
				if (!frame.empty())
				{
					// face detection
					this.detectAndDisplay(frame);

					// convert the Mat object (OpenCV) to Image (JavaFX)
					imageToShow = matToBufferedImage(frame, null);
				}

			}
			catch (Exception e)
			{
				// log the (full) error
				System.err.println("ERROR: " + e);
			}
		}

		return imageToShow;
	}

	/**
	 * Method for face detection and tracking
	 * 
	 * @param frame
	 *            it looks for faces in this frame
	 */
	private void detectAndDisplay(Mat frame)
	{
		MatOfRect faces = new MatOfRect();
		Mat grayFrame = new Mat();

		// convert the frame in gray scale
		Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
		// equalize the frame histogram to improve the result
		Imgproc.equalizeHist(grayFrame, grayFrame);

		// compute minimum face size (20% of the frame height, in our case)
		if (this.absoluteFaceSize == 0)
		{
			int height = grayFrame.rows();
			if (Math.round(height * 0.2f) > 0)
			{
				this.absoluteFaceSize = Math.round(height * 0.2f);
			}
		}

		// detect faces
		this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
				new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

		// each rectangle in faces is a face: draw them!
		Rect[] facesArray = faces.toArray();
		if(facesArray.length > 0) {
			System.out.println("face detected");
			int indexMax = 0;
			double areaMax = 0.0;
			for(int i = 0; i < facesArray.length; i++) {
				if(facesArray[i].area() > areaMax) {
					areaMax = facesArray[i].area();
					indexMax = i;
				}
			}
			Core.rectangle(frame, facesArray[indexMax].tl(), facesArray[indexMax].br(), new Scalar(0, 255, 0), 3);
		}	
	}



	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 * 
	 * @param frame
	 *            the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	public BufferedImage matToBufferedImage(Mat matrix, BufferedImage bimg)
	{
		if ( matrix != null ) { 
			int cols = matrix.cols();  
			int rows = matrix.rows();  
			int elemSize = (int)matrix.elemSize();  
			byte[] data = new byte[cols * rows * elemSize];  
			int type;  
			matrix.get(0, 0, data);  
			switch (matrix.channels()) {  
			case 1:  
				type = BufferedImage.TYPE_BYTE_GRAY;  
				break;  
			case 3:  
				type = BufferedImage.TYPE_3BYTE_BGR;  
				// bgr to rgb  
				byte b;  
				for(int i=0; i<data.length; i=i+3) {  
					b = data[i];  
					data[i] = data[i+2];  
					data[i+2] = b;  
				}  
				break;  
			default:  
				return null;  
			}  

			// Reuse existing BufferedImage if possible
			if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
				bimg = new BufferedImage(cols, rows, type);
			}        
			bimg.getRaster().setDataElements(0, 0, cols, rows, data);
		} else { // mat was null
			bimg = null;
		}
		return bimg;  
	}

}
