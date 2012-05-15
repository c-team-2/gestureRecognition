// kNN demo
// Jerry Zhu, Carnegie Mellon University, 2000/12
// My first Java program.

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
                                    
    
public class KNN extends Applet implements ActionListener
{
    private TextField inputN, inputComplexity, inputK, inputKNN;
    private Button Step1Button, Step2Button, Step3Button;
	private Label errLabel; 
    private KNNCanvas theKNNCanvas;
	private Canvas theTruthCanvas;
    
    private int n, complexity, k, knn;
    private boolean[][] truth;
    private class Sample {
        int x;
        int y;
        boolean label;
    } 
    Sample[] samples;
    
    private class Distance {
        double d;
        boolean label;
    } 
    Distance[] distances;
    
    public void init()
    {
		GridBagLayout bag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
        this.setLayout(bag);

		// create input fields for 
        // N: the size of the area is N*N
		Label label = new Label("Step 1: Field size(10--80):");
		bag.setConstraints(label, c);
		this.add(label);
        inputN = new TextField("80", 2);
		bag.setConstraints(inputN, c);
        this.add(inputN);
		// Complexity: of the true distribution
		label = new Label("      complexity(1--100):");
		bag.setConstraints(label, c);
		this.add(label);
        inputComplexity = new TextField("5", 2);
		bag.setConstraints(inputComplexity, c);
        this.add(inputComplexity);
        Step1Button = new Button("Create Truth");
		Step1Button.addActionListener(this);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER; // last component in a row
		bag.setConstraints(Step1Button, c);
		c.anchor = GridBagConstraints.CENTER;
		this.add(Step1Button);
		
        // k: number of positive or negative examples
		label = new Label("Step 2: samples(1--2000):");
		c.gridwidth = 1; // reset to default
		bag.setConstraints(label, c);
		this.add(label);
        inputK = new TextField("10", 2);
		bag.setConstraints(inputK, c);
		this.add(inputK);
        Step2Button = new Button("Generate Samples");
		Step2Button.addActionListener(this);
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = GridBagConstraints.REMAINDER; // last component in a row
		bag.setConstraints(Step2Button, c);
		c.anchor = GridBagConstraints.CENTER;
		this.add(Step2Button);
		
		// kNN: the number of neighbors considered in kNN.
		label = new Label("Step 3: kNN(1--100):");
		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1; // reset to default
		bag.setConstraints(label, c);
		c.anchor = GridBagConstraints.CENTER;
		this.add(label);
        inputKNN = new TextField("1", 2);
		bag.setConstraints(inputKNN, c);
		this.add(inputKNN);
        Step3Button = new Button("Classify");
		Step3Button.addActionListener(this);
		this.add(Step3Button);
		errLabel = new Label(" ");
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER; // last component in a row
		bag.setConstraints(errLabel, c);
		this.add(errLabel);
		
		// the truth canvas
		theTruthCanvas = new TruthCanvas();
		theTruthCanvas.setSize(404, 404);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridwidth = GridBagConstraints.RELATIVE; // last component in a row
		c.gridheight = GridBagConstraints.REMAINDER; // last component in a column
		bag.setConstraints(theTruthCanvas, c);
		this.add(theTruthCanvas);
		// the KNN canvas
		theKNNCanvas = new KNNCanvas();
		theKNNCanvas.setSize(404, 404);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = GridBagConstraints.REMAINDER; // last component in a row
		c.gridheight = GridBagConstraints.REMAINDER; // last component in a column
		bag.setConstraints(theKNNCanvas, c);
		this.add(theKNNCanvas);

	}
    
    public void actionPerformed(ActionEvent e)
	{
	 if (e.getSource() == Step1Button)
		{
		 n = new Integer(inputN.getText()).intValue();
 		 complexity = new Integer(inputComplexity.getText()).intValue();
		 k=0; // remove previous samples
		 truth = new boolean[n][n];
		 int x, y;
		 for (x=0; x<n; x++)
			 for (y=0; y<n; y++)
				 truth[x][y] = true;
		 for (int i=0; i<complexity; i++)
			{double w1, w2, b;
			 w1 = Math.random()*2 - 1;
			 w2 = Math.random()*2 - 1;
			 b = Math.random()*n/2;
 			 for (x=0; x<n; x++)
				 for (y=0; y<n; y++)
					 if (w1*(x-n/2)+w2*(y-n/2)+b>0)
						 truth[x][y] = !truth[x][y];
			}
		 theTruthCanvas.repaint();
		}
	 else if (e.getSource() == Step2Button)
		{
		 k = new Integer(inputK.getText()).intValue();
		 samples = new Sample[k];
		 int i;
		 for (i=0; i<k; i++)
		 {   samples[i] = new Sample();
		     samples[i].x = (int)(Math.random()*n);
		     samples[i].y = (int)(Math.random()*n);
		     samples[i].label = truth[samples[i].x][samples[i].y];
		 }
 		 theTruthCanvas.repaint();
		}
	 else if (e.getSource() == Step3Button)
		{int i;	
		 knn = new Integer(inputKNN.getText()).intValue();
		 distances = new Distance[knn];
		 for (i=0; i<knn; i++)
		 {   distances[i] = new Distance();
		 }
		 theKNNCanvas.repaint();
		}
	}    

    public void start()
    {
    }
    
    class KNNCanvas extends Canvas {
        
        public void paint(Graphics g) {
            int m=5; // m*m 'pixel'
            
            // draw bounding box
            g.setColor(Color.black);
            System.out.println(n);            
            g.drawRect(0, 0, n*m+1, n*m+1);
            
             // for each point, classify it with kNN.
			 int error = 0; // number of misclassified points
             for (int x=0; x<n; x++)
                 for (int y=0; y<n; y++)
                    {
                     // find the knn
                     for (int i=0; i<k; i++){
                         double dist = (samples[i].x-x)*(samples[i].x-x)+(samples[i].y-y)*(samples[i].y-y);
                         if (i<knn)
                            {distances[i].d = dist;
                             distances[i].label = samples[i].label;
                            }
                         else
                            {// go through the knn list and replace the biggest one if possible
                             double biggestd = distances[0].d;
                             int biggestindex = 0;
                             for (int a=1; a<knn; a++)
                                 if (distances[a].d > biggestd)
                                 {biggestd = distances[a].d;
                                  biggestindex = a;
                                 }
                             if (dist < biggestd)
                                {distances[biggestindex].d = dist;
                                 distances[biggestindex].label = samples[i].label;
                                }
                            }
                     }
                     // count which label in knn occurs most, this is the classification of (x,y)
                     int nT=0, nF=0;
					 boolean classification;
                     for (int i=0; i<knn; i++)
                         if (distances[i].label == true)
                             nT++;
                         else
                             nF++;
					 if (nT<nF)
						 classification = false;
					 else if (nT>nF)
						 classification = true;
					 else
						 classification = Math.random() < 0.5; // if tie, randomly break it
					 
                     if (classification == false)
                         g.setColor(Color.white);
                     else
                         g.setColor(Color.green);
                     g.fillRect(m*x+1, m*y+1, m, m);
					 
					 // count classification errors
					 if (classification != truth[x][y])
						 error ++;
                    }
			 // update error label
			 if (n>0)
				errLabel.setText("    Error rate = " + (float)error/n/n*100 + "%");

             // draw labeled samples 
            g.setColor(Color.black);
            for (int i=0; i<k; i++)
            {if (samples[i].label == true)
                 g.drawOval(samples[i].x*m+1, samples[i].y*m+1, m, m);
             else
                {g.drawLine(samples[i].x*m+(m+1)/2, samples[i].y*m+1, samples[i].x*m+(m+1)/2, samples[i].y*m+m);
                 g.drawLine(samples[i].x*m+1, samples[i].y*m+(m+1)/2, samples[i].x*m+m, samples[i].y*m+(m+1)/2);
                }
            }

        }
    }

    class TruthCanvas extends Canvas {
        
        public void paint(Graphics g) {
            int m=5; // m*m 'pixel'
            
            // draw bounding box
            g.setColor(Color.black);
            System.out.println(n);            
            g.drawRect(0, 0, n*m+1, n*m+1);
            
        
             // for each point, draw its true classification
             for (int x=0; x<n; x++)
                 for (int y=0; y<n; y++)
                    {
                     if (truth[x][y] == false)
                         g.setColor(Color.white);
                     else
                         g.setColor(Color.green);
                     g.fillRect(m*x+1, m*y+1, m, m);
                    }
            // draw labeled samples
            g.setColor(Color.black);
            for (int i=0; i<k; i++)
            {if (samples[i].label == true)
                 g.drawOval(samples[i].x*m+1, samples[i].y*m+1, m, m);
             else
                {g.drawLine(samples[i].x*m+(m+1)/2, samples[i].y*m+1, samples[i].x*m+(m+1)/2, samples[i].y*m+m);
                 g.drawLine(samples[i].x*m+1, samples[i].y*m+(m+1)/2, samples[i].x*m+m, samples[i].y*m+(m+1)/2);
                }
            }
            
		}
    }

}