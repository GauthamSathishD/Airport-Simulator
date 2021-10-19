//Gautham Sathish Airport Assignment

package sathishgauthamairport;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author gauthamsathish
 */
public class AirportSimulator extends javax.swing.JFrame {

        Timer t;//Timer
        public Queue<Integer> landing = new LinkedList<Integer>();//Landing Queue
        public Queue<Integer> takeOff = new LinkedList<Integer>();//Takeoff Queue
        int timeUnits = 3;//Keeps track of units of time, starts at 3
        int decision = 0;//Decides whether to land plane or takeoff plane, starts at 0
        int TAKEOFF = 2;//Constant variable set at 2
        Image planeTakeoff = Toolkit.getDefaultToolkit().getImage("PlaneTakeoff.png");//Imports image of plane taking off
        Image planeLanding = Toolkit.getDefaultToolkit().getImage("PlaneLanding.png");//Imports image of plane landing
        JPanel test = new JPanel(){//Creates new panel
           public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if(decision==TAKEOFF){//If decision is equal to TAKEOFF
                    g.drawImage(planeTakeoff, 100-(timeUnits*12), 30, this);//Animate the plane taking off
                }else{//Else
                g.drawImage(planeLanding, (timeUnits*15), 30, this);//Animate the plane landing
                }
            } 
        };

    

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(landing.size()==0 && takeOff.size()==0){//If landing size is 0 and takeOff size is 0
                message.setText("Waiting for entries");//Inform the user the program is waiting for entries
                return;//Return
            }
            if(timeUnits!=1){//If timeUnits is not 1
            timeUnits--;//Subtract timeUnits
            }
            if(decision==TAKEOFF && takeOff.size()!=0){//If decision equals TAKEOFF and takeOff size is not 0
                message.setText("Plane "+takeOff.peek()+" Taking Off In "+timeUnits+" seconds");//Tell the user in how many seconds the plane will be taking off
            }else{//Else
                message.setText("Plane "+landing.peek()+" Arriving In "+timeUnits+" seconds");//Tell the user in how many seconds the plane will be arriving
            }
            if(timeUnits==1 && (landing.size()==0 || decision == TAKEOFF)){//If timeUnits is 1 and landing is empty or decision is equal to takeoff
                if(takeOff.size()!=0){//If takeoff is not empty
                    updateQueue(takeOff);//Call update Queue on takeoff
                    if(timeUnits==1){//If timeUnits is 1
                         message.setText("Plane departed");//Send message that plane has departed
                    }
                    decision++;//Add to decision
                    if(decision == 3 || takeOff.size()==0){//If decision is 3 or takeoff is empty
                        decision = 0 ;//Set decision to 0
                    }
                    if(landing.size()==0){//If landing is empty
                        decision = TAKEOFF;//Set decision to TAKEOFF
                    }
                }else{//Else
                    decision = 0;//Set decision to 0
                    return;//return 
                }
                if(landing.size()==0 || decision==TAKEOFF){//If landing is empty or decision is equal to TAKEOFF
                    timeUnits = 5;//Set timeUnits to 5
                }else{//Else
                    timeUnits = 3;//Set timeUnits to 3
                }
            }else if(timeUnits == 1){//If timeUnits is 1
                if(landing.size()!=0){//If landing is not empty
                    updateQueue(landing);//Call updateQueue on landing
                    if(timeUnits==1){//If timeUnits is 1
                         message.setText("Plane arrived");//Send message that plane has arrived
                    }
                    decision++;//Add to decision
                    if(decision == 3 || takeOff.size()==0){//If decision is 3 or takeOff is empty
                        decision = 0 ;//Set decision to 0
                    }
                    if(landing.size()==0){//If landing is empty
                        decision = TAKEOFF;//Set decision to TAKEOFF
                    }
                }else{//Else
                    decision = TAKEOFF;//Set decision to TAKEOFF
                }
                if(landing.size()==0 || decision==TAKEOFF){//If landing is empty or decision is equal to TAKEOFF
                    timeUnits = 5;//Set timeUnits to 5
                }else{//Else
                    timeUnits = 3;//Set timeUnits to 3
                }
            }
            repaint();//Repaint
        }
        
    }
    
    
    public void updateQueue(Queue queue){//updateQueue method 
        if(queue.size()==0){//If queue is empty
            return;//Return 
        }
        if(queue==landing){//If queue is equal to landing
            if(landing.size()==1){//If landing size is 1
                arrivalsTextField.setText("");//Make arrivals text field nothing
            }else{//Else
                int index = arrivalsTextField.getText().indexOf("\n");//Set int index to the text in the arrivalstextField next line
                arrivalsTextField.setText(arrivalsTextField.getText().substring(index + 1));//Display arrivals textfield 
            }
        }else{//Else
            if(takeOff.size()==1){//If takeOff size is 1
                takeoffsTextField.setText("");//Make takeoffs text field nothing
            }else{//Else
                int index = takeoffsTextField.getText().indexOf("\n");//Set int index to the text in the takeoffstextField next line
                takeoffsTextField.setText(takeoffsTextField.getText().substring(index + 1));//Displau takeOffs textfield
            }
        }
        queue.remove();//Remove from queue
    }

    public void addToArrivalQueue(int arrivingFlightNumber){
        if(landing.size()==0 && takeOff.size()==0){//If landing is empty and takeoff is empty
            timeUnits = 3;//Set timeUnits to 3
        }
        landing.add(arrivingFlightNumber);//Add number to queue
        arrivalsTextField.setText(arrivalsTextField.getText()+arrivingFlightNumber+"\n");
    }
    
    public void addToTakeoffQueue(int takeoffFlightNumber){//Adding numbers to the takeoff queue
        if(landing.size()==0 && takeOff.size()==0){//If landing is empty and takeoff is empty
            timeUnits = 5;//Set timeUnits to 5
        }
        takeOff.add(takeoffFlightNumber);//Add number to queue
        takeoffsTextField.setText(takeoffsTextField.getText()+takeoffFlightNumber+"\n");//Add number to textField
    }
    /**
     * Creates new form AirportSimulator
     */
    public AirportSimulator() {
        int tDuration = 600;//Set tDuration to 600
        t = new Timer(tDuration, new TimerListener());//Initizaline new timer
        initComponents();
        planeAnimation.add(test);
        test.setSize(200,100);//Sets panel size
        File takeOffs = new File("takeoffs.txt");//create the file object
        File arrivals = new File("arrivals.txt");//create the file object
        
        Scanner sc = null;//Creating a scanner
        try{
            sc = new Scanner(arrivals);//Sets scanner to arrivals file
        }catch(Exception e){
        }
        while(sc.hasNextInt()){//As long as there are integers in the file
            addToArrivalQueue(sc.nextInt());//Put the integer into the queue
        }
        
        try{
            sc = new Scanner(takeOffs);//Sets scanner to takeoff file
        }catch(Exception e){
        }
        while(sc.hasNextInt()){//As long as there are integers in the file
            addToTakeoffQueue(sc.nextInt());//Put the integer into the queue
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        arrivalsTextField = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        takeoffsTextField = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        arrivingInput = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        takeoffInput = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        message = new javax.swing.JLabel();
        planeAnimation = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        warning = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Airport Simulator (Gautham)");

        jLabel2.setText("Press 'START' to begin simulation.");

        jLabel3.setText("Arrivals");

        jLabel4.setText("Takeoffs");

        arrivalsTextField.setColumns(20);
        arrivalsTextField.setRows(5);
        arrivalsTextField.setFocusable(false);
        jScrollPane2.setViewportView(arrivalsTextField);

        takeoffsTextField.setColumns(20);
        takeoffsTextField.setRows(5);
        takeoffsTextField.setFocusable(false);
        jScrollPane3.setViewportView(takeoffsTextField);

        jButton1.setText("Start");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Arriving Flight:");

        arrivingInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arrivingInputActionPerformed(evt);
            }
        });

        jLabel6.setText("Takeoff Flight");

        takeoffInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeoffInputActionPerformed(evt);
            }
        });

        jButton2.setText("Exit");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        message.setText(" ");

        javax.swing.GroupLayout planeAnimationLayout = new javax.swing.GroupLayout(planeAnimation);
        planeAnimation.setLayout(planeAnimationLayout);
        planeAnimationLayout.setHorizontalGroup(
            planeAnimationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        planeAnimationLayout.setVerticalGroup(
            planeAnimationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 87, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(18, 18, 18))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(arrivingInput, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(takeoffInput, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(0, 12, Short.MAX_VALUE))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(warning, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(48, 48, 48)
                                        .addComponent(planeAnimation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel2))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(warning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(planeAnimation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(message)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(19, 19, 19))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton1))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(arrivingInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(takeoffInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(72, 72, 72))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Start Button
        t.start();//Start timer
    }//GEN-LAST:event_jButton1ActionPerformed

    private void arrivingInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arrivingInputActionPerformed
        //Arriving Flight
        try{
            int arrivingFlightNumber = Integer.parseInt(arrivingInput.getText());////Add value to landing Queue
            addToArrivalQueue(arrivingFlightNumber);//Call addToArrivalQueue method
            warning.setText("");//Clear warning
        }catch(Exception e){
            warning.setText("Enter proper integers");//Prompt user for integers
        }
        arrivingInput.setText("");//Set text to nothing after entered
    }//GEN-LAST:event_arrivingInputActionPerformed

    private void takeoffInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeoffInputActionPerformed
        // Takeoff Flight
        try{
            int takeoffFlightNumber = Integer.parseInt(takeoffInput.getText());//Add value to takeOff Queue
            addToTakeoffQueue(takeoffFlightNumber);//Call addToTakeoffQueue method
            warning.setText("");//Clear warning
        }catch(Exception e){
            warning.setText("Enter proper integers");//Prompt user for integers
        }
        takeoffInput.setText("");//Set text to nothing after entered
    }//GEN-LAST:event_takeoffInputActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Exit Button
        System.exit(0);//Exit if exit is pressed
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AirportSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AirportSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AirportSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AirportSimulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AirportSimulator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea arrivalsTextField;
    private javax.swing.JTextField arrivingInput;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel message;
    private javax.swing.JPanel planeAnimation;
    private javax.swing.JTextField takeoffInput;
    private javax.swing.JTextArea takeoffsTextField;
    private javax.swing.JLabel warning;
    // End of variables declaration//GEN-END:variables
}
