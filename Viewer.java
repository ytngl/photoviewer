package project3_yohana_yap;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



public class Viewer extends JPanel{
	private JPanel topPanel = new JPanel();
	private JPanel bottomPanel = new JPanel();
	static final int FRAME_WIDTH = 1200;
	static final int FRAME_HEIGHT = 800;
	public final int PICTURE_WIDTH = 1000;
	public final int PICTURE_HEIGHT = 700;
	private int pictureCounter = 0;

	private String path = System.getProperty("user.home") + "/Pictures/JavaImages";
	private File pictureFile = new File(path);
	private File[] allPictures = pictureFile.listFiles(new FilenameFilter() {
		public boolean accept(File directory, String fileName) {
			return fileName.endsWith(".jpg");
		}
	});

	private JLabel picLabel = new JLabel();

	////////////////////////////////////////////////////////////////////////////////////////
	public Viewer(){
		this.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		bottomPanel.setLayout(new GridLayout(2,0));
		buildPrevBtnPanel();
		buildNextBtnPanel();
		buildPictureDisplayPanel();
		buildAutoplayPanel();
		buildZoomPanel();
		add(topPanel, BorderLayout.CENTER);
		add(bottomPanel,BorderLayout.SOUTH);
	}

	////////////////////////////////////////////////////////////////////////////////////////
	private void setLabelImage(File imgFile, int width, int height) 
			throws IOException {
		Image img = ImageIO.read(imgFile);
		picLabel.setIcon(new ImageIcon(img.getScaledInstance(width, height,
				Image.SCALE_SMOOTH)));
	}

	private void buildPictureDisplayPanel(){
		JPanel pictureDisplayPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(pictureDisplayPanel);
		Dimension pictureSize = new Dimension(FRAME_WIDTH/2, FRAME_HEIGHT/2);
		try {
			setLabelImage(allPictures[pictureCounter], PICTURE_WIDTH, PICTURE_HEIGHT);
		} catch (IOException e){
			e.printStackTrace();
		}
		pictureDisplayPanel.add(picLabel);
		scrollPane.setPreferredSize(pictureSize);
		scrollPane.createHorizontalScrollBar();
		scrollPane.createVerticalScrollBar();
		topPanel.add(scrollPane, BorderLayout.CENTER);
	}
	////////////////////////////////////////////////////////////////////////////////////////
	private void buildNextBtnPanel(){
		JPanel nextBtnPanel = new JPanel(new GridLayout());
		JButton nextBtn = new JButton(">>>");
		nextBtn.addActionListener(new nextBtnListener());       
		nextBtn.setPreferredSize(new Dimension(50, 350));
		nextBtnPanel.add(nextBtn);
		topPanel.add(nextBtnPanel, BorderLayout.EAST);
	}
	private class nextBtnListener implements ActionListener{
		public void actionPerformed(ActionEvent e){  
			if (pictureCounter < allPictures.length - 1){
				pictureCounter++;
			} else {
				pictureCounter = 0;
			}
			try {
				setLabelImage(allPictures[pictureCounter], PICTURE_WIDTH, PICTURE_HEIGHT);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////
	private void buildPrevBtnPanel() {
		JPanel prevBtnPanel = new JPanel(new GridLayout());
		JButton prevBtn = new JButton("<<<");
		prevBtn.addActionListener(new prevBtnListener());       
		prevBtn.setPreferredSize(new Dimension(50, 350));
		prevBtnPanel.add(prevBtn);
		topPanel.add(prevBtnPanel, BorderLayout.WEST);



	}
	private class prevBtnListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{  
			if (pictureCounter == 0){
				pictureCounter = allPictures.length - 1;
			}else if (pictureCounter <= allPictures.length && pictureCounter != 0){
				pictureCounter--;
			 }else {
				pictureCounter = 0;
			  }
			try {
				setLabelImage(allPictures[pictureCounter], PICTURE_WIDTH, PICTURE_HEIGHT);
			}catch (IOException e1){
				e1.printStackTrace();
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////	
	private void buildAutoplayPanel(){
		JPanel autoplayPanel = new JPanel(new GridLayout(0,3));
		JCheckBox autoplay = new JCheckBox("autoplay");		
		JSlider autoplaySecSlider = new JSlider(0, 10, 1);
		JLabel secLabel = new JLabel("sec");
		int seconds = 1000;

		Timer timer = new Timer(seconds, new nextBtnListener());

		class autoplaySecSliderListener implements ChangeListener{
			@Override
			public void stateChanged(ChangeEvent e) {
				final int seconds = 1000 * (autoplaySecSlider.getValue());
				timer.setDelay(seconds);
			}
		}
		class autoplayChoiceListener implements ActionListener{
			@Override
			public void actionPerformed(ActionEvent g){
				if (autoplay.isSelected()){
					timer.start();
				}else{
					timer.stop();
				}
			}	
		}
		
		autoplay.addActionListener(new autoplayChoiceListener());

		autoplaySecSlider.addChangeListener(new autoplaySecSliderListener());
		autoplaySecSlider.setMinorTickSpacing(1);
		autoplaySecSlider.setMajorTickSpacing(5);
		autoplaySecSlider.setPaintTicks(true);
		autoplaySecSlider.setPaintLabels(true); 
		autoplaySecSlider.setName("sec");

		autoplayPanel.add(autoplay);
		autoplayPanel.add(autoplaySecSlider);
		autoplayPanel.add(secLabel);
		bottomPanel.add(autoplayPanel);
	}
	////////////////////////////////////////////////////////////////////////////////////////
	private void buildZoomPanel() {
		JPanel zoomPanel = new JPanel(new GridLayout(0,3));
		JCheckBox zoom = new JCheckBox("zoom");		
		JSlider zoomSlider = new JSlider(0, 10, 1);
		JLabel zoomLabel = new JLabel("+/-");


		class zoomSliderListener implements ChangeListener{
			@Override
			public void stateChanged(ChangeEvent e) {
				int zoomWidth = PICTURE_WIDTH * (zoomSlider.getValue());
				int zoomHeight = PICTURE_HEIGHT * (zoomSlider.getValue());
				if(zoom.isSelected()){
					try {
						setLabelImage(allPictures[pictureCounter], zoomWidth, zoomHeight);
					} catch (IOException e1){
						e1.printStackTrace();
					}
				}
			}
		}

		zoomSlider.addChangeListener(new zoomSliderListener());
		zoomSlider.setMinorTickSpacing(1);
		zoomSlider.setMajorTickSpacing(5);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true); 


		zoomPanel.add(zoom);
		zoomPanel.add(zoomSlider);
		zoomPanel.add(zoomLabel);

		bottomPanel.add(zoomPanel);
	}



	////////////////////////////////////////////////////////////////////////////////////////
	private static void startFrame(int width, int height) {

		JFrame frame = new JFrame();
		frame.setTitle("Picture Viewer");
		frame.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

		JPanel panel = new Viewer();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

	}
	////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		startFrame(FRAME_WIDTH, FRAME_HEIGHT);
	}


}
