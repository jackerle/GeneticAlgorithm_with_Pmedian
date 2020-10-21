import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller {

    static GUI gui;
    static String[] str;
    static String path = "";
    static String vertex_n = "";
    static int P = 0;
    static int popsize = -1;
    static int seed = -1;
    static int maxgen = -1;
    static int tournament_size = -1;
    static double crossoverrate = 0.5;
    static double mutationrate = 0.02;

    public static void main(String[] args) throws Exception{
        gui = new GUI();
    }

    public static void get_file_from_path(String path) throws FileNotFoundException {
        try{
            Scanner file = new Scanner(new File(path),"UTF-8");
            String _text = file.useDelimiter("\\A").next();
            String[] text = _text.split("\n");
            str =  text;
            vertex_n = text[0].split(" ")[0];
            P = Integer.parseInt(text[0].split(" ")[2]);
            gui.vertex_n.setText(vertex_n);
            gui.P_n.setText(String.valueOf(P));
        }
        catch (Exception e){
            gui.alert("File format not match!");
        }

    }

    public static void start_butt() throws Exception{
        if(popsize>0&&seed>0&&maxgen>0&&tournament_size>0){
            gui.GA_cal();
        }
        else if(tournament_size>popsize){
            gui.alert("tournament size cant most than popsize");
        }
        else{
            gui.alert("please type all input");
        }
    }



}
