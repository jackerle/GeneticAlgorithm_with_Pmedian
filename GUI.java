import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GUI extends JFrame {


    /*
    envi
     */
    int screen_width = 500;
    int screen_height = 300;
    int font_size = 16;

    static String path = "";
    Individual best_solution = null;
    /*
    jframe component
     */
    JLabel directory;

    String data_table[][] = {};


    JButton import_butt;

    GUI() throws Exception{

        super("GA_P-median");
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        setSize(screen_width,screen_height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        init_component();
        init_component2();
        init_input_component();
        init_result_panal();
        setLayout(null);
        setVisible(true);

    }

    public void init_component() {
        directory= new JLabel("path",SwingConstants.LEFT);
        directory.setBounds(0,0,400,30);
        directory.setForeground(Color.GRAY);
        directory.setBackground(Color.GRAY);
        directory.setBorder(BorderFactory.createRaisedBevelBorder());
        directory.setFont(new Font("Verdana", Font.PLAIN, 20));
        import_butt = new JButton("import");
        import_butt.setBounds(400,0,100,30);
        import_butt.setFont(new Font("Verdana", Font.PLAIN, 20));
        import_butt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.FILES_ONLY);
                f.showSaveDialog(null);

                path = f.getSelectedFile().toString();
                Controller.path = path;
                directory.setText(path);

                try {
                    Controller.get_file_from_path(path);
                    input_mutation.setEnabled(true);
                    input_popsize.setEnabled(true);
                    input_seed.setEnabled(true);
                    input_maxgen.setEnabled(true);
                    input_crossover.setEnabled(true);
                    input_tournament_size.setEnabled(true);
                    start_butt.setEnabled(true);
                }
                catch (FileNotFoundException e){
                    input_mutation.setEnabled(false);
                    input_popsize.setEnabled(false);
                    input_seed.setEnabled(false);
                    input_maxgen.setEnabled(false);
                    input_crossover.setEnabled(false);
                    input_tournament_size.setEnabled(false);
                    start_butt.setEnabled(false);
                    alert("File not found");
                }
            }
        });


        this.add(directory);
        this.add(import_butt);
    }

    JLabel vertex_l = new JLabel("Vertex:");
    JLabel vertex_n = new JLabel("");
    JLabel P_l = new JLabel("P:");
    JLabel P_n = new JLabel("");

    public void init_component2(){
        vertex_l.setBounds(0,30,85,30);
        vertex_l.setFont(new Font("Verdana", Font.PLAIN, font_size));
        vertex_n.setBounds(85,30,150,30);
        vertex_n.setFont(new Font("Verdana", Font.PLAIN, font_size));

        P_l.setBounds(235,30,50,30);
        P_l.setFont(new Font("Verdana", Font.PLAIN, font_size));
        P_n.setBounds(285,30,50,30);
        P_n.setFont(new Font("Verdana", Font.PLAIN, font_size));

        this.add(vertex_l);
        this.add(vertex_n);
        this.add(P_l);
        this.add(P_n);
    }

    JButton input_popsize = new JButton("popsize:");
    JButton input_seed = new JButton("seed:");
    JButton input_maxgen = new JButton("maxgen:");
    JButton input_tournament_size = new JButton("tour_size:");
    JButton input_crossover = new JButton("crossover:"+Controller.crossoverrate);
    JButton input_mutation = new JButton("mutation:"+Controller.mutationrate);
    JButton start_butt;

    public void init_input_component(){
        start_butt = new JButton("Start");
        input_popsize.setBounds(0,60,165,30);
        input_seed.setBounds(166,60,165,30);
        input_maxgen.setBounds(332,60,165,30);

        input_popsize.setFont(new Font("Verdana", Font.PLAIN, font_size));
        input_seed.setFont(new Font("Verdana", Font.PLAIN, font_size));
        input_maxgen.setFont(new Font("Verdana", Font.PLAIN, font_size));


        input_popsize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String popsize = (String)JOptionPane.showInputDialog("Input popsize","");
                try{
                    if(popsize.length()>0&&Integer.parseInt(popsize)>0){
                        Controller.popsize = Integer.parseInt(popsize);
                        success("Updated popsize");
                        input_popsize.setText("popsize:"+Controller.popsize);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new popsize");
                }
            }
        });

        input_seed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String seed = (String)JOptionPane.showInputDialog("Input seed","");
                try{
                    if(seed.length()>0&&Integer.parseInt(seed)>0){
                        Controller.seed = Integer.parseInt(seed);
                        success("Updated seed");
                        input_seed.setText("seed:"+Controller.seed);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new seed");
                }
            }
        });

        input_maxgen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String maxgen = (String)JOptionPane.showInputDialog("Input maxgen","");
                try{
                    if(maxgen.length()>0&&Integer.parseInt(maxgen)>0){
                        Controller.maxgen = Integer.parseInt(maxgen);
                        success("Updated maxgen");
                        input_maxgen.setText("maxgen:"+Controller.maxgen);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new maxgen");
                }
            }
        });

        input_tournament_size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String tournament_size = (String)JOptionPane.showInputDialog("Input tournament_size","");
                try{
                    if(tournament_size.length()>0&&Integer.parseInt(tournament_size)>0){
                        Controller.tournament_size = Integer.parseInt(tournament_size);
                        success("Updated tournament_size");
                        input_tournament_size.setText("tournament_size:"+Controller.tournament_size);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new tournament_size");
                }
            }
        });

        input_tournament_size.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String tournament_size = (String)JOptionPane.showInputDialog("Input tournament_size","");
                try{
                    if(tournament_size.length()>0&&Integer.parseInt(tournament_size)>0){
                        Controller.tournament_size = Integer.parseInt(tournament_size);
                        success("Updated tournament_size");
                        input_tournament_size.setText("tournament:"+Controller.tournament_size);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new tournament_size");
                }
            }
        });

        input_crossover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String crossoverrate = (String)JOptionPane.showInputDialog("Input crossoverrate","");
                try{
                    if(crossoverrate.length()>0&&Double.parseDouble(crossoverrate)>0){
                        Controller.crossoverrate = Double.parseDouble(crossoverrate);
                        success("Updated crossoverrate");
                        input_crossover.setText("crossover:"+Controller.crossoverrate);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new crossoverrate");
                }
            }
        });


        input_mutation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String mutation = (String)JOptionPane.showInputDialog("Input mutationrate","");
                try{
                    if(mutation.length()>0&&Double.parseDouble(mutation)>0){
                        Controller.mutationrate = Double.parseDouble(mutation);
                        success("Updated mutationrate");
                        input_mutation.setText("mutation:"+Controller.mutationrate);
                    }
                    else{
                        alert("Must have input or most than 0");
                    }
                }
                catch (NumberFormatException e){
                    alert("Error please type new mutation");
                }
            }
        });

        start_butt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    Controller.start_butt();
                }
                catch (Exception e){
                    System.out.println(e);
                }

            }
        });





        input_tournament_size.setBounds(0,90,165,30);
        input_crossover.setBounds(166,90,165,30);
        input_mutation.setBounds(332,90,165,30);

        start_butt.setBounds(150,120,200,30);

        input_tournament_size.setFont(new Font("Verdana", Font.PLAIN, font_size));
        input_crossover.setFont(new Font("Verdana", Font.PLAIN, font_size));
        input_mutation.setFont(new Font("Verdana", Font.PLAIN, font_size));


        start_butt.setFont(new Font("Verdana", Font.PLAIN, font_size+2));





        this.add(input_popsize);
        this.add(input_seed);
        this.add(input_maxgen);
        this.add(input_tournament_size);
        this.add(input_crossover);
        this.add(input_mutation);
        this.add(start_butt);

        input_mutation.setEnabled(false);
        input_popsize.setEnabled(false);
        input_seed.setEnabled(false);
        input_maxgen.setEnabled(false);
        input_crossover.setEnabled(false);
        input_tournament_size.setEnabled(false);

        start_butt.setEnabled(false);

    }

    JLabel itr = new JLabel("Itr :"+0+"/"+0);
    JLabel best = new JLabel("Best :"+0);
    JButton export = new JButton("Export");

    public void init_result_panal(){


        itr.setBounds(0,150,200,30);
        itr.setFont(new Font("Verdana", Font.PLAIN, font_size+2));

        best.setBounds(0,180,200,30);
        best.setFont(new Font("Verdana", Font.PLAIN, font_size+2));

        export.setBounds(350,210,100,30);
        export.setFont(new Font("Verdana", Font.PLAIN, font_size+2));
        export.setVisible(false);

        export.addActionListener(new ActionListener() {
            String path_export;
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.FILES_ONLY);
                f.showSaveDialog(null);

                path_export = f.getSelectedFile().toString();
                try{
                    System.out.println(best_solution);
                    Files.write(Paths.get(path_export),best_solution.toString().getBytes());
                }
                catch (Exception e){
                    alert("Cant save file");
                }

            }
        });

        this.add(itr);
        this.add(best);
        this.add(export);

    }


    public void GA_cal()throws Exception{
        if(GeneticAlgorithm.GA_cal(this.itr,this.best)){
            best_solution = GeneticAlgorithm.best_solution;
            success("Success");
            export.setVisible(true);
        }
    }



    public void alert(String msg){
        JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
    }
    public void success(String msg){
        JOptionPane.showMessageDialog(null,msg,"Success",JOptionPane.PLAIN_MESSAGE);
    }
}
